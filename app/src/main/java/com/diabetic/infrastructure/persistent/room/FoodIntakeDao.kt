package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodIntakeDao {
    @Insert
    fun insert(foodIntake: FoodIntakeEntity): Long

    @Query("SELECT * FROM food_intake WHERE id=:id")
    fun fetch(id: Int): FoodIntakeEntity?

    @Query("SELECT * FROM food_intake")
    fun fetch(): List<FoodIntakeEntity>
}