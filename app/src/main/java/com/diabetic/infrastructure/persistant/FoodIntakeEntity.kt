package com.diabetic.infrastructure.persistant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intake",
    indices = [
        Index(value = arrayOf("glucose_before_meal")),
        Index(value = arrayOf("glucose_after_meal"))
    ],
    foreignKeys = [
        ForeignKey(
            entity = GlucoseLevelEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("glucose_before_meal")
        ),
        ForeignKey(
            entity = GlucoseLevelEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("glucose_after_meal")
        ),
    ]
)
class FoodIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "bread_unit") val breadUnit: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "glucose_before_meal") val glucoseBeforeMeal: Int,
    @ColumnInfo(name = "glucose_after_meal") val glucoseAfterMeal: Int?
)