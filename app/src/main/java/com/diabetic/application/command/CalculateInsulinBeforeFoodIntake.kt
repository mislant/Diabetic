package com.diabetic.application.command

import com.diabetic.domain.model.CarbohydrateRequired
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.Insulin
import com.diabetic.domain.model.NotFound
import com.diabetic.domain.service.InsulinCalculator

class CalculateInsulinBeforeFoodIntake {
    class Command(
        val foodIntake: Int
    )

    class Handler(
        private val repository: FoodIntakeRepository,
        private val store: CarbohydrateStorage
    ) {
        private val calculator = InsulinCalculator()

        /**
         * @throws CarbohydrateRequired
         * @throws NotFound
         */
        fun handle(command: Command): Insulin {
            val carbohydrate = store.get() ?: throw CarbohydrateRequired()
            val foodIntake = repository.getById(command.foodIntake) ?: throw NotFound.foodIntake()

            return calculator.calculateBeforeFoodIntake(
                foodIntake.breadUnit,
                carbohydrate
            )
        }
    }
}