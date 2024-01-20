package com.diabetic.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@JvmInline
value class DateTime(private val value: LocalDateTime = LocalDateTime.now()) {
    companion object {
        const val ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val READABLE = "yyyy-MM-dd HH:mm"

        fun fromString(value: String): DateTime {
            try {
                return DateTime(
                    LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern(ISO)
                    )
                )
            } catch (e: DateTimeParseException) {
                throw IllegalArgumentException("Invalid date time value")
            }
        }

    }

    fun format(): Format = Format(this)

    class Format(private val datetime: DateTime) {
        fun iso(): String = datetime.value.format(formatter(ISO))

        fun readable(): String = datetime.value.format(formatter(READABLE))

        private fun formatter(pattern: String): DateTimeFormatter =
            DateTimeFormatter.ofPattern(pattern)
    }
}