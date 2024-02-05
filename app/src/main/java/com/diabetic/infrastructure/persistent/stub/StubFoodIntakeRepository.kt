package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository

class StubFoodIntakeRepository : InMemoryStorage<FoodIntake>(), FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake {
        storage.add(foodIntake)
        foodIntake.id = storage.indexOf(foodIntake)
        return foodIntake
    }

    override fun getById(id: Int): FoodIntake? {
        return storage.find {
            it.id == id
        }
    }
}