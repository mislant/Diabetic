package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.generateGlucoseReport
import java.io.OutputStream

class PrepareGlucoseLevelsReport : PrepareReport() {
    class Handler(
        private val repository: GlucoseLevelRepository,
    ) : PrepareReport.Handler() {
        override val template: String = "Glucose_level_report.%s.xlsx"
        override val sheetName: String = "Отчет по глюкозе"

        override fun OutputStream.writeReport(
            command: WriteReportCommand,
            meta: ReportMeta
        ): OutputStream {
            val levels = repository.run {
                if (command.range === null) fetch() else fetch(
                    command.range.from,
                    command.range.to
                )
            }

            return this.generateGlucoseReport(
                levels,
                meta
            )
        }
    }
}