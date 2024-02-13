package com.diabetic.domain.model

interface FoodIntakeRepository {
    fun persist(foodIntake: FoodIntake): FoodIntake
    fun fetch(id: Int): FoodIntake?
    fun fetch(): List<FoodIntake>
}