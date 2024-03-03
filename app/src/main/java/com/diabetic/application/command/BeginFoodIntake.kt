package com.diabetic.application.command

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.CarbohydrateRequired
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.domain.service.InsulinCalculator

class BeginFoodIntake {
    data class Command(
        val breadUnit: Int,
    )

    class Handler(
        private val repository: FoodIntakeRepository,
        private val storage: CarbohydrateStorage,
        private val calculator: InsulinCalculator = InsulinCalculator()
    ) {
        /** @throws CarbohydrateRequired */
        fun handle(command: Command): ShortInsulin {
            val breadUnit = BreadUnit(command.breadUnit)
            val carbohydrate = storage.get() ?: throw CarbohydrateRequired()

            val foodIntake = FoodIntake(
                breadUnit,
                calculator.calculateInsulin(breadUnit, carbohydrate),
            )

            repository.persist(foodIntake)

            return foodIntake.insulin
        }
    }
}