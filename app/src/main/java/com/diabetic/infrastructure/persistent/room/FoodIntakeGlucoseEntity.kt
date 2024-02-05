package com.diabetic.infrastructure.persistent.room

import androidx.room.Embedded
import androidx.room.Relation

data class FoodIntakeGlucoseEntity(
    @Embedded val foodIntake: FoodIntakeEntity,
    @Relation(
        parentColumn = "glucose_before_meal",
        entityColumn = "id"
    )
    val glucoseBeforeMeal: GlucoseLevelEntity
)