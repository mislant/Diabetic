package com.diabetic.domain.model

import java.time.LocalDateTime

data class GlucoseLevel(
    var type: MeasureType,
    var value: Value,
    var datetime: LocalDateTime = LocalDateTime.now(),
    var id: Int? = null
) {
    enum class MeasureType {
        BEFORE_MEAL,
        AFTER_MEAL,
        UNSPECIFIED;

        companion object {
            fun from(string: String): MeasureType {
                return MeasureType.valueOf(string.uppercase())
            }
        }

        override fun toString(): String {
            return when (this) {
                BEFORE_MEAL -> "before_meal"
                AFTER_MEAL -> "after_meal"
                UNSPECIFIED -> "unspecified"
            }
        }
    }

    @JvmInline
    value class Value(
        val level: Float
    ) {
        init {
            if (level <= 0.0) {
                throw IllegalArgumentException("Glucose level can not be less or equal zero")
            }
        }
    }
}
