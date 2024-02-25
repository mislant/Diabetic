package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LongInsulinDao {
    @Insert
    fun insert(longInsulin: LongInsulinEntity): Long?

    @Query("SELECT * FROM long_insulin WHERE id=:id")
    fun fetch(id: Int): LongInsulinEntity
}