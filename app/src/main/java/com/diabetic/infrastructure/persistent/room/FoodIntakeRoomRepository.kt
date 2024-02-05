package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.GlucoseLevel

class FoodIntakeRoomRepository(private val dao: FoodIntakeDao) : FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake {
        val id = dao.newFoodIntake(foodIntake)
        foodIntake.id = id.toInt()
        return foodIntake
    }

    override fun getById(id: Int): FoodIntake? {
        return dao.fetchFoodIntakeGlucose(id).run {
            if (this === null) null else FoodIntake(
                id,
                BreadUnit(foodIntake.breadUnit),
                DateTime.fromString(foodIntake.date),
                GlucoseLevel(
                    GlucoseLevel.MeasureType.from(glucoseBeforeMeal.measureType),
                    GlucoseLevel.Value(glucoseBeforeMeal.value),
                    DateTime.fromString(glucoseBeforeMeal.date),
                    glucoseBeforeMeal.id
                )
            )
        }
    }
}