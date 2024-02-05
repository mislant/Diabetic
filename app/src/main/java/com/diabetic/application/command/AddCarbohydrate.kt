package com.diabetic.application.command

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateStorage

class AddCarbohydrate {
    class Command(
        val rate: Float
    )

    class Handler(
        private val storage: CarbohydrateStorage
    ) {
        fun handle(command: Command) {
            val savedRate = storage.get()

            if (savedRate == null || savedRate.value != command.rate) {
                storage.set(Carbohydrate(command.rate))
            }
        }
    }
}