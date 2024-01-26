package com.diabetic.domain.model

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

    companion object {
        fun beforeMeal(value: Value, datetime: DateTime): GlucoseLevel {
            return GlucoseLevel(MeasureType.BEFORE_MEAL, value, datetime)
        }

        fun afterMeal(value: Value, datetime: DateTime): GlucoseLevel {
            return GlucoseLevel(MeasureType.BEFORE_MEAL, value, datetime)
        }
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
