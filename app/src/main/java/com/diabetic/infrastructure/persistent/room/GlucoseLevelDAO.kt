package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GlucoseLevelDAO {
    @Insert
    fun insert(glucoseLevel: GlucoseLevelEntity)

    @Query("SELECT * FROM glucose_level WHERE id=:id")
    fun byId(id: Int): GlucoseLevelEntity

    @Query("SELECT * FROM glucose_level")
    fun all(): List<GlucoseLevelEntity>
}