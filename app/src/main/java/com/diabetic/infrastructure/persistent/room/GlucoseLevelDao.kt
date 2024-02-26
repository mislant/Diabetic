package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GlucoseLevelDao {
    @Insert
    fun insert(glucoseLevel: GlucoseLevelEntity)

    @Query("SELECT * FROM glucose_level WHERE id=:id")
    fun fetch(id: Int): GlucoseLevelEntity?

    @Query("SELECT * FROM glucose_level")
    fun fetch(): List<GlucoseLevelEntity>

    @Query("SELECT * FROM glucose_level WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun fetch(from: String, to: String): List<GlucoseLevelEntity>

    @Delete
    fun delete(glucoseLevel: GlucoseLevelEntity)
}