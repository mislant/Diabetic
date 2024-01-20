package com.diabetic.application.command

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository

class AddGlucoseLevel {
    data class Command(
        val value: Float,
        val type: GlucoseLevel.MeasureType
    )

    class Handler(
        private val repository: GlucoseLevelRepository
    ) {
        fun handle(command: Command) {
            val level = GlucoseLevel(
                command.type,
                GlucoseLevel.Value(command.value),
                DateTime()
            )

            repository.persist(level)
        }
    }
}