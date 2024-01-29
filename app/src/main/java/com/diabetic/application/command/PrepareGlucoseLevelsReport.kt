package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevelRepository
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
        fun handle(command: Command) {
            TODO()
        }
    }
}