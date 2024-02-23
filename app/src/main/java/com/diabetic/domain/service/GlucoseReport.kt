package com.diabetic.domain.service

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.readable
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream
import java.time.LocalDateTime

fun OutputStream.generateGlucoseReport(
    glucoseLevels: List<GlucoseLevel>,
    meta: ReportMeta
): OutputStream {
    val report = GlucoseReport().excel(glucoseLevels, meta)
    report.write(this)
    return this
}

abstract class Report<T> {
    abstract val headerNames: List<String>

    fun excel(data: List<T>, meta: ReportMeta): Workbook {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(meta.sheetName)

        title(sheet, workbook, meta)
        header(sheet, workbook)
        content(sheet, workbook, data)

        return workbook
    }

    private fun title(sheet: Sheet, workbook: Workbook, meta: ReportMeta) {
        val lightGreen = XSSFColor(byteArrayOf(190.toByte(), 255.toByte(), 175.toByte()))
        val style = workbook.createCellStyle().also {
            it.setFillForegroundColor(lightGreen)
            it.fillPattern = FillPatternType.SOLID_FOREGROUND
            it.setFont(workbook.createFont().also {
                it.fontName = "Arial"
                it.fontHeightInPoints = 15
            })
            it.alignment = HorizontalAlignment.CENTER
            it.setBorder(BorderStyle.THIN)
        }

        val title = sheet.let {
            val cell = it.createRow(0).createCell(0)
            it.addMergedRegion(
                CellRangeAddress(
                    0, 0,
                    0, headerNames.count() - 1
                )
            )
            cell
        }
        title.cellStyle = style
        title.setCellValue(
            "${meta.sheetName} %s ".format(
                if (meta.range == null) {
                    "за все время"
                } else {
                    "с ${meta.range.from.readable()} по ${meta.range.to.readable()}"
                }
            )
        )
    }

    private fun header(sheet: Sheet, workbook: Workbook) {
        val grey = XSSFColor(byteArrayOf(246.toByte(), 246.toByte(), 246.toByte()))
        val style = workbook.createCellStyle().also {
            it.setFillForegroundColor(grey)
            it.fillPattern = FillPatternType.SOLID_FOREGROUND
            it.setFont(workbook.createFont().also {
                it.fontName = "Arial"
                it.fontHeightInPoints = 14
                it.bold = true
            })
            it.alignment = HorizontalAlignment.CENTER
            it.setBorder(BorderStyle.THIN)
        }

        val header = sheet.createRow(1)
        headerNames.forEachIndexed { index, name ->
            header.createCell(index).also {
                it.cellStyle = style
                it.setCellValue(name)
            }

            val characterWidth = 2F
            sheet.setColumnWidth(index, (name.length * characterWidth * 256).toInt())
        }
    }

    fun content(sheet: Sheet, workbook: Workbook, data: List<T>) {
        val style = workbook.createCellStyle().also {
            it.setFont(workbook.createFont().also {
                it.fontName = "Arial"
                it.fontHeightInPoints = 12
            })
            it.alignment = HorizontalAlignment.CENTER
            it.setBorder(BorderStyle.THIN)
        }

        val offset = 2
        data.forEachIndexed { index, item ->
            sheet
                .createRow(index + offset)
                .writeLine(item, style)
        }
    }

    protected abstract fun Row.writeLine(data: T, style: CellStyle)
}

class GlucoseReport : Report<GlucoseLevel>() {
    override val headerNames: List<String>
        get() = listOf(
            " # ", "Уровень глюкозы", "Дата и время", "До\\После еды"
        )

    override fun Row.writeLine(data: GlucoseLevel, style: CellStyle) {
        createCell(0).also {
            it.cellStyle = style
            it.setCellValue(data.id!!.toString())
        }
        createCell(1).also {
            it.cellStyle = style
            it.setCellValue("%.2f".format(data.value.level))
        }
        createCell(2).also {
            it.cellStyle = style
            it.setCellValue(data.date.format().readable())
        }
        createCell(3).also {
            it.cellStyle = style
            it.setCellValue(
                when (data.type) {
                    GlucoseLevel.MeasureType.BEFORE_MEAL -> "До"
                    GlucoseLevel.MeasureType.AFTER_MEAL -> "После"
                    GlucoseLevel.MeasureType.UNSPECIFIED -> "-"
                }
            )
        }
    }
}

data class ReportMeta(
    val range: Range?,
    val sheetName: String
) {
    @JvmInline
    value class Range(private val range: Pair<LocalDateTime, LocalDateTime>) {
        val from
            get() = range.first

        val to
            get() = range.second
    }
}

private fun CellStyle.setBorder(style: BorderStyle) {
    this.borderTop = style
    this.borderBottom = style
    this.borderLeft = style
    this.borderRight = style
}
