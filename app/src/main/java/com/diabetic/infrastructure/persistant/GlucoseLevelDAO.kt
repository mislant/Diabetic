package com.diabetic.infrastructure.persistant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.diabetic.domain.model.GlucoseLevel

@Dao
interface GlucoseLevelDAO {
    @Insert
    fun inset(glucoseLevel: GlucoseLevelEntity)

    @Query("SELECT * FROM glucose_level WHERE id=:id")
    fun byId(id: Int): GlucoseLevelEntity

    @Query("SELECT * FROM glucose_level")
    fun all(): List<GlucoseLevelEntity>
}