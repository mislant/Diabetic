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

    @Query("SELECT * FROM glucose_level ORDER BY datetime")
    fun fetch(): List<GlucoseLevelEntity>

    @Query("SELECT * FROM glucose_level WHERE datetime BETWEEN :from AND :to  ORDER BY datetime")
    fun fetch(from: Long, to: Long): List<GlucoseLevelEntity>

    @Delete
    fun delete(glucoseLevel: GlucoseLevelEntity)
}