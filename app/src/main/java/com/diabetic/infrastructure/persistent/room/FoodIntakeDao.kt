package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FoodIntakeDao {
    @Insert
    fun insert(foodIntake: FoodIntakeEntity): Long

    @Query("SELECT * FROM food_intake WHERE id=:id")
    fun fetch(id: Int): FoodIntakeEntity?

    @Query("SELECT * FROM food_intake")
    fun fetch(): List<FoodIntakeEntity>

    @Query("SELECT * FROM food_intake WHERE date BETWEEN :from AND :to ORDER BY date")
    fun fetch(from: String, to: String): List<FoodIntakeEntity>

    @Delete
    fun delete(foodIntake: FoodIntakeEntity)
}