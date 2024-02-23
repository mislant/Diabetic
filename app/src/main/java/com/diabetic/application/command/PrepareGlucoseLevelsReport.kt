package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.readableDate
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.generateGlucoseReport
import java.io.OutputStream
import java.time.LocalDateTime

class PrepareGlucoseLevelsReport {
    data class GenerateFileNameCommand(
        val range: Pair<LocalDateTime, LocalDateTime>?
    )

    data class WriteReportCommand(
        val range: Pair<LocalDateTime, LocalDateTime>?,
        val stream: OutputStream
    )

    class Handler(
        private val repository: GlucoseLevelRepository,
    ) {

        fun handle(command: GenerateFileNameCommand): String {
            val range = command.range

            val template = "Glucose_level_report.%s.xlsx"
            return if (range == null) {
                template.format("for_all_time")
            } else {
                template.format(
                    "${range.from.readableDate()}_${range.to.readableDate()}"
                )
            }
        }

        fun handle(command: WriteReportCommand) {
            val levels = repository.run {
                if (command.range === null) fetch() else fetch(
                    command.range.from,
                    command.range.to
                )
            }


            val meta = ReportMeta(
                range = if (command.range == null) null else ReportMeta.Range(
                    command.range
                ),
                "Отчет по глюкозе"
            )

            return command.stream
                .generateGlucoseReport(levels, meta)
                .close()
        }

        private val Pair<LocalDateTime, LocalDateTime>.from: LocalDateTime get() = this.first
        private val Pair<LocalDateTime, LocalDateTime>.to: LocalDateTime get() = this.second
    }
}