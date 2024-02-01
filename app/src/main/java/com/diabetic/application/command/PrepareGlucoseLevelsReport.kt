package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.readable
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.excel
import java.io.File
import java.time.LocalDateTime

class PrepareGlucoseLevelsReport {
    class Command(
        val from: LocalDateTime,
        val to: LocalDateTime,
    )

    class Handler(
        private val repository: GlucoseLevelRepository,
        private val storage: ExcelReportStorage
    ) {
        fun handle(command: Command): File {
            val range = ReportMeta.Range(Pair(command.from, command.to))
            val glucoseLevels = repository.fetchRange(
                range.from,
                range.to
            )

            val report = "Glucose_report.${command.from.readable()}-${command.to.readable()}"
                .replace(' ', '_')
            val meta = ReportMeta(range)

            return storage.new(report).apply {
                outputStream()
                    .excel(glucoseLevels, meta)
                    .close()
            }
        }
    }

    interface ExcelReportStorage {
        fun new(name: String): File
    }
}