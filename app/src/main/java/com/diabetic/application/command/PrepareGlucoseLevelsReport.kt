package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.excel
import java.io.OutputStream
import java.time.LocalDateTime

class PrepareGlucoseLevelsReport {
    class Command(
        val from: LocalDateTime,
        val to: LocalDateTime,
        val stream: OutputStream
    )

    class Handler(
        private val repository: GlucoseLevelRepository,
    ) {
        fun handle(command: Command): OutputStream {
            val glucoseLevels = repository.fetchRange(
                command.from,
                command.to
            )

            return command.stream.excel(
                glucoseLevels, ReportMeta(
                    ReportMeta.Range(Pair(command.to, command.from))
                )
            )
        }
    }
}