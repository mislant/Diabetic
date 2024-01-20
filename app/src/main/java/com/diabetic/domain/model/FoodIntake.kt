package com.diabetic.domain.model

data class FoodIntake(
    var breadUnit: BreadUnit,
    var date: DateTime,
    var glucoseBeforeMeal: GlucoseLevel,
    var glucoseAfterMeal: GlucoseLevel? = null
) {
    var id: Int? = null
}