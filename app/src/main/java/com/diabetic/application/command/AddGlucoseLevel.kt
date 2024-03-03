package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import java.time.LocalDateTime

class AddGlucoseLevel {
    data class Command(
        val value: Float,
        val type: GlucoseLevel.MeasureType = GlucoseLevel.MeasureType.UNSPECIFIED,
        val datetime: LocalDateTime? = null
    )

    class Handler(
        private val repository: GlucoseLevelRepository
    ) {
        fun handle(command: Command) {
            val datetime = command.datetime ?: LocalDateTime.now()

            val level = GlucoseLevel(
                type = command.type,
                value = GlucoseLevel.Value(command.value),
                datetime = datetime
            )

            repository.persist(level)
        }
    }
}