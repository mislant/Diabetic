package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KeyValueDao {
    @Insert
    fun insert(keyValue: KeyValueEntity)

    @Query("SELECT * FROM key_value WHERE `key`=:key")
    fun get(key: String): KeyValueEntity?
}