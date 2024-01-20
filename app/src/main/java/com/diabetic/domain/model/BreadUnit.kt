package com.diabetic.domain.model

@JvmInline
value class BreadUnit(
    val value: Int
) {
    init {
        if (value <= 0) {
            throw IllegalArgumentException("Bread units can not be less or equal zero")
        }
    }
}