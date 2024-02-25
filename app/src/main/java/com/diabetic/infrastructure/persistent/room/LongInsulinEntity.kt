package com.diabetic.infrastructure.persistent.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "long_insulin")
class LongInsulinEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "datetime") val date: String
)