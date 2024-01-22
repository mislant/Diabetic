package com.diabetic.infrastructure.persistent.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        GlucoseLevelEntity::class,
        FoodIntakeEntity::class,
        KeyValueEntity::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun glucoseLevelDao(): GlucoseLevelDAO
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun keyValueDao(): KeyValueDao
}