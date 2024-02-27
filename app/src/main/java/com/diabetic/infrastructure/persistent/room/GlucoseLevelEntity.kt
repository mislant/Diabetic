package com.diabetic.infrastructure.persistent.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glucose_level")
class GlucoseLevelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "measure_type") val measureType: String,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "datetime") val datetime: Long
)