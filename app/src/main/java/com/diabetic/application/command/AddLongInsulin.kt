package com.diabetic.application.command

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import java.time.LocalDateTime

class AddLongInsulin {
    data class Command(
        val value: Float,
        val datetime: LocalDateTime? = null
    )

    class Handler(
        private val repository: LongInsulinRepository
    ) {
        fun handle(command: Command) {
            val datetime = command.datetime ?: LocalDateTime.now()

            val insulin = LongInsulin(
                value = command.value,
                datetime = datetime
            )

            repository.persist(insulin)
        }
    }
}