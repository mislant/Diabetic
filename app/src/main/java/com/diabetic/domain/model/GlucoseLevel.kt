package com.diabetic.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class GlucoseLevel(
    var type: MeasureType,
    var value: Value,
    var date: DateTime
) {
    var id: Int? = null

    constructor(
        type: MeasureType,
        value: Value,
        date: DateTime,
        id: Int
    ) : this(type, value, date) {
        this.id = id
    }

    enum class MeasureType {
        BEFORE_MEAL,
        AFTER_MEAL;

        companion object {
            fun from(string: String): MeasureType {
                return MeasureType.valueOf(string.uppercase())
            }
        }

        override fun toString(): String {
            return when (this) {
                BEFORE_MEAL -> "before_meal"
                AFTER_MEAL -> "after_meal"
            }
        }
    }

    data class Value(
        val level: Float
    ) {
        init {
            if (level <= 0.0) {
                throw IllegalArgumentException("Glucose level can not be less or equal zero")
            }
        }
    }

    data class DateTime(private var value: LocalDateTime) {
        companion object {
            const val ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            const val READABLE = "yyyy-MM-dd HH:mm"
        }

        constructor() : this(LocalDateTime.now())

        constructor(value: String) : this() {
            try {
                this.value = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(ISO))
            } catch (e: DateTimeParseException) {
                throw IllegalArgumentException("Invalid date time value")
            }
        }

        class Format(private val date: DateTime) {
            fun iso(): String = date.value.format(formatter(ISO));

            fun readable(): String = date.value.format(formatter(READABLE))

            private fun formatter(pattern: String): DateTimeFormatter =
                DateTimeFormatter.ofPattern(pattern)
        }

        fun format(): Format = Format(this)
    }
}
