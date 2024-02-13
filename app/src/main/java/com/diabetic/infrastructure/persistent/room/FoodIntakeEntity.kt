package com.diabetic.infrastructure.persistent.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "food_intake")
class FoodIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "bread_unit") val breadUnit: Int,
    @ColumnInfo(name = "insulin") val insulin: Float,
    @ColumnInfo(name = "date") val date: String,
)