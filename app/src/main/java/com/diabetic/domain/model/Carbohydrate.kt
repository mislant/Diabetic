package com.diabetic.domain.model

@JvmInline
value class Carbohydrate(val value: Float) {
    init {
        if (value <= 0) {
            throw IllegalArgumentException("Carbohydrate can not be less or equal zero")
        }
    }
}