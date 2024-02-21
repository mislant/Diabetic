package com.diabetic.domain.model

import java.time.LocalDateTime

interface FoodIntakeRepository {
    fun persist(foodIntake: FoodIntake): FoodIntake
    fun fetch(id: Int): FoodIntake?
    fun fetch(): List<FoodIntake>
    fun fetch(from: LocalDateTime, to: LocalDateTime): List<FoodIntake>
}