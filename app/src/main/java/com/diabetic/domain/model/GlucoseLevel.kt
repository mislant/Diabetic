package com.diabetic.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GlucoseLevel(
    private var type: MeasureType,
    private var value: Value,
    private var date: LocalDateTime
) {
    private var id: Int? = null

    enum class MeasureType {
        BEFORE_MEAL,
        AFTER_MEAL;

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
            if (level < 0.0) {
                throw IllegalArgumentException("Glucose level can not be less zero")
            }
        }
    }

    fun setId(new: Int) {
        id = new;
    }

    fun type(): MeasureType {
        return type
    }

    fun value(): Float {
        return value.level
    }

    fun date(): String {
        val iso = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return date.format(iso)
    }
}
