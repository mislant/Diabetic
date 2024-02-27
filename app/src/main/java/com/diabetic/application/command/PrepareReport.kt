package com.diabetic.application.command

import com.diabetic.domain.model.time.readableDate
import com.diabetic.domain.service.ReportMeta
import java.io.OutputStream
import java.time.LocalDateTime

abstract class PrepareReport {
    data class GenerateFileNameCommand(
        val range: Pair<LocalDateTime, LocalDateTime>?
    )

    data class WriteReportCommand(
        val range: Pair<LocalDateTime, LocalDateTime>?,
        val stream: OutputStream
    )

    abstract class Handler {
        abstract val template: String
        abstract val sheetName: String

        fun handle(command: GenerateFileNameCommand): String {
            val range = command.range

            return if (range == null) {
                template.format("for_all_time")
            } else {
                template.format(
                    "${range.from.readableDate}_${range.to.readableDate}"
                )
            }
        }

        fun handle(command: WriteReportCommand) {
            val meta = ReportMeta(
                range = if (command.range == null) null else ReportMeta.Range(
                    command.range
                ),
                sheetName
            )

            return command
                .stream
                .writeReport(command, meta)
                .close()
        }

        protected abstract fun OutputStream.writeReport(
            command: WriteReportCommand,
            meta: ReportMeta
        ): OutputStream

        protected val Pair<LocalDateTime, LocalDateTime>.from: LocalDateTime get() = this.first
        protected val Pair<LocalDateTime, LocalDateTime>.to: LocalDateTime get() = this.second
    }
}