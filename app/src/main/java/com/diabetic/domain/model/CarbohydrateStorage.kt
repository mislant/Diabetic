package com.diabetic.domain.model

interface CarbohydrateStorage {
    fun set(rate: Carbohydrate)
    fun get(): Carbohydrate?
}