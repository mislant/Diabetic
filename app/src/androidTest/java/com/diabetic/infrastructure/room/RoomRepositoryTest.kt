package com.diabetic.infrastructure.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.diabetic.infrastructure.persistent.room.AppDatabase
import java.io.IOException

abstract class RoomRepositoryTest {
    protected lateinit var db: AppDatabase

    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
    }

    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}