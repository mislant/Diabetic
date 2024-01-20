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
}
