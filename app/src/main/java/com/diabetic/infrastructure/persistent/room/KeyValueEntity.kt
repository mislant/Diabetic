package com.diabetic.infrastructure.persistent.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "key_value",
    indices = [
        Index(
            value = arrayOf("key"),
            name = "key-index",
            unique = true
        )
    ]
)
class KeyValueEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "value") var value: String
)