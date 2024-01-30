package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.readable
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.excel
import java.io.FileOutputStream
import java.time.LocalDateTime

class PrepareGlucoseLevelsReport {
    class Command(
        val from: LocalDateTime,
        val to: LocalDateTime,
    )

    interface ExcelReportStorage {
        fun new(name: String): FileOutputStream
    }

    class Handler(
        private val repository: GlucoseLevelRepository,
        private val storage: ExcelReportStorage
    ) {
        fun handle(command: Command) {
            val range = ReportMeta.Range(Pair(command.from, command.to))
            val glucoseLevels = repository.fetchRange(
                range.from,
                range.to
            )

            val report = "Glucose_report.${command.from.readable()}-${command.to.readable()}"
                .replace(' ', '_')
            val meta = ReportMeta(range)

            storage.new(report)
                .excel(glucoseLevels, meta)
                .close()
        }
    }
}