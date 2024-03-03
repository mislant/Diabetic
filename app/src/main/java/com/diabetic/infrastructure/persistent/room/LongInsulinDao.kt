package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LongInsulinDao {
    @Insert
    fun insert(longInsulin: LongInsulinEntity): Long?

    @Query("SELECT * FROM long_insulin WHERE id=:id")
    fun fetch(id: Int): LongInsulinEntity?

    @Query("SELECT * FROM long_insulin ORDER BY datetime")
    fun fetch(): List<LongInsulinEntity>

    @Query("SELECT * FROM long_insulin WHERE datetime BETWEEN :from AND :to ORDER BY datetime")
    fun fetch(from: Long, to: Long): List<LongInsulinEntity>

    @Delete
    fun delete(longInsulin: LongInsulinEntity)
}