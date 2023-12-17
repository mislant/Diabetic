package com.diabetic.infrastructure.persistant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GlucoseLevelDAO {
    @Insert
    fun inset(glucoseLevel: GlucoseLevelEntity)

    @Query("SELECT * FROM glucose_level WHERE id=:id")
    fun byId(id: Int): GlucoseLevelEntity
}