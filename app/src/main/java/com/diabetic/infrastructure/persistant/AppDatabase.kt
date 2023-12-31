package com.diabetic.infrastructure.persistant

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GlucoseLevelEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun glucoseLevelDao(): GlucoseLevelDAO
}