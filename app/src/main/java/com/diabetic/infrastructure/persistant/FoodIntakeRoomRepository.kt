package com.diabetic.infrastructure.persistant

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository

class FoodIntakeRoomRepository(private val dao: FoodIntakeDao) : FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake {
        val id = dao.newFoodIntake(foodIntake)
        foodIntake.id = id.toInt()
        return foodIntake
    }
}