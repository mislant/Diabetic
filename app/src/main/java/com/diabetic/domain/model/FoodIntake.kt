package com.diabetic.domain.model

data class FoodIntake(
    var breadUnit: BreadUnit,
    var glucoseBeforeMeal: GlucoseLevel,
    var glucoseAfterMeal: GlucoseLevel? = null
) {
    var id: Int? = null
}