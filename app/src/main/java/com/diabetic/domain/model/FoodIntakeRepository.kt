package com.diabetic.domain.model

interface FoodIntakeRepository {
    fun persist(foodIntake: FoodIntake): FoodIntake
}