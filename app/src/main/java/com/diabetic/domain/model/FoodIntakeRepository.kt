package com.diabetic.domain.model

interface FoodIntakeRepository {
    fun persist(foodIntake: FoodIntake): FoodIntake
    fun getById(id: Int): FoodIntake?
}