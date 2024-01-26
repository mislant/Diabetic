package com.diabetic.domain.model

data class FoodIntake(
    var breadUnit: BreadUnit,
    var date: DateTime,
    var glucoseBeforeMeal: GlucoseLevel,
    var glucoseAfterMeal: GlucoseLevel? = null
) {
    var id: Int? = null

    constructor(
        id: Int,
        breadUnit: BreadUnit,
        date: DateTime,
        glucoseBeforeMeal: GlucoseLevel,
        glucoseAfterMeal: GlucoseLevel? = null
    ) : this(breadUnit, date, glucoseBeforeMeal, glucoseAfterMeal) {
        this.id = id
    }
}