package com.diabetic.application.command

import com.diabetic.domain.model.LongInsulinRepository
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.generateLongInsulinReport
import java.io.OutputStream

class PrepareLongInsulinReport : PrepareReport() {
    class Handler(
        private val repository: LongInsulinRepository
    ) : PrepareReport.Handler() {
        override val template: String = "Long_insulin_report.%s.xlsx"
        override val sheetName: String = "Отчет по длинному инсулину"

        override fun OutputStream.writeReport(
            command: WriteReportCommand,
            meta: ReportMeta
        ): OutputStream {
            val insulinLevel = repository.run {
                if (command.range === null) fetch() else fetch(
                    command.range.from,
                    command.range.to
                )
            }

            return generateLongInsulinReport(insulinLevel, meta)
        }
    }
}