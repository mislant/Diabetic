package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import java.time.LocalDateTime

class StubFoodIntakeRepository : InMemoryStorage<FoodIntake>(), FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake {
        storage.add(foodIntake)
        foodIntake.id = storage.indexOf(foodIntake)
        return foodIntake
    }

    override fun fetch(id: Int): FoodIntake? {
        return storage.find {
            it.id == id
        }
    }

    override fun fetch(): List<FoodIntake> {
        return storage.toList()
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<FoodIntake> {
        return storage.filter {
            it.datetime > from &&
                    it.datetime < to
        }
    }

    override fun delete(id: Int) {
        storage.removeAt(id)
    }
}