package com.diabetic.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@JvmInline
value class DateTime(private val value: LocalDateTime = LocalDateTime.now()) {
    companion object {
        const val ISO = "yyyy-MM-dd HH:mm:ss.SSS"
        const val READABLE = "yyyy-MM-dd HH:mm"
        const val READABLE_DATE = "yyyy-MM-dd"

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

    fun localDataTime(): LocalDateTime {
        return value
    }

    fun format(): Format = Format(this)

    class Format(private val datetime: DateTime) {
        fun iso(): String = datetime.value.format(ISO)

        fun readable(): String = datetime.value.format(READABLE)

        fun readableDate(): String = datetime.value.format(READABLE_DATE)

        private fun LocalDateTime.format(pattern: String): String =
            this.format(DateTimeFormatter.ofPattern(pattern))
    }
}

val String.dateTime: DateTime get() = DateTime.fromString(this)

fun LocalDateTime.toDateTime(): DateTime = DateTime(this)

fun LocalDateTime.iso(): String = DateTime(this).format().iso()

fun LocalDateTime.readable(): String = DateTime(this).format().readable()

fun LocalDateTime.readableDate(): String = DateTime(this).format().readableDate()