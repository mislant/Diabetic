package com.diabetic.application.command

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.toDateTime
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
            val datetime = if (command.datetime == null) DateTime()
            else command.datetime.toDateTime()

            val level = GlucoseLevel(
                command.type,
                GlucoseLevel.Value(command.value),
                datetime
            )

            repository.persist(level)
        }
    }
}