package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.diabetic.domain.model.FoodIntake

@Dao
interface FoodIntakeDao {
    @Transaction
    fun newFoodIntake(foodIntake: FoodIntake): Long {
        val levelEntityId = insertGlucoseLevel(
            GlucoseLevelEntity(
                measureType = foodIntake.glucoseBeforeMeal.type.toString(),
                value = foodIntake.glucoseBeforeMeal.value.level,
                date = foodIntake.glucoseBeforeMeal.date.format().iso()
            )
        )

        val foodIntakeEntity = FoodIntakeEntity(
            breadUnit = foodIntake.breadUnit.value,
            date = foodIntake.date.format().iso(),
            glucoseBeforeMeal = levelEntityId.toInt(),
            glucoseAfterMeal = null
        )
        return insertFoodIntake(foodIntakeEntity)
    }

    @Insert
    fun insertGlucoseLevel(glucoseLevel: GlucoseLevelEntity): Long

    @Insert
    fun insertFoodIntake(foodIntake: FoodIntakeEntity): Long
}