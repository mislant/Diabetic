package com.diabetic.application.command

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.GlucoseLevel

class BeginFoodIntake {
    data class Command(
        val glucoseLevelBeforeMeal: Float,
        val breadUnit: Int,
    )

    class Handler(
        private val repository: FoodIntakeRepository
    ) {
        fun handle(command: Command) {
            val time = DateTime()
            val foodIntake = FoodIntake(
                BreadUnit(command.breadUnit),
                time,
                GlucoseLevel.beforeMeal(
                    GlucoseLevel.Value(command.glucoseLevelBeforeMeal),
                    time
                ),
            )

            repository.persist(foodIntake)
        }
    }
}