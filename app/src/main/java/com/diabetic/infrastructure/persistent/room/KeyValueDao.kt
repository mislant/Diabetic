package com.diabetic.infrastructure.persistent.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface KeyValueDao {
    @Insert
    fun insert(keyValue: KeyValueEntity)

    @Update
    fun update(keyValue: KeyValueEntity)

    @Query("SELECT * FROM key_value WHERE `key`=:key")
    fun get(key: String): KeyValueEntity?

    @Query("SELECT EXISTS (SELECT * FROM key_value WHERE `key`=:key)")
    fun exists(key: String): Boolean
}