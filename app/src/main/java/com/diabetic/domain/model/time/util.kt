package com.diabetic.domain.model.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

private enum class Format(
    val value: String
) {
    ISO("yyyy-MM-dd HH:mm:ss.SSS"),
    READABLE("yyyy-MM-dd HH:mm"),
    DATE("yyyy-MM-dd"),
    TIME("HH:mm");

    companion object {
        val datetime: Array<Format> get() = arrayOf(ISO, READABLE)
    }

    val formatter: DateTimeFormatter
        get() =
            DateTimeFormatter.ofPattern(this.value)
}

val String.datetime: LocalDateTime
    get() {
        for (format in Format.datetime) {
            try {
                return LocalDateTime.parse(
                    this,
                    format.formatter
                )
            } catch (_: Throwable) {
            }
        }

        throw IllegalArgumentException("$this is not in appropriate format")
    }

val Long.datetime: LocalDateTime
    get() = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId()
    )

val String.milliseconds: Long
    get() = this.datetime.milliseconds

val LocalDateTime.milliseconds: Long
    get() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

val LocalDateTime.iso: String get() = this.format(Format.ISO.formatter)
val LocalDateTime.readable: String get() = this.format(Format.READABLE.formatter)
val LocalDateTime.readableDate: String get() = this.format(Format.DATE.formatter)
val LocalDateTime.readableTime: String get() = this.format(Format.TIME.formatter)