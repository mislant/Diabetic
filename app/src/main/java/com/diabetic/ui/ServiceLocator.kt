package com.diabetic.ui

import android.content.Context
import androidx.room.Room
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.infrastructure.persistant.AppDatabase
import com.diabetic.infrastructure.persistant.GlucoseLevelRoomRepository

class ServiceLocator private constructor() {
    private lateinit var db: AppDatabase

    companion object {
        @Volatile
        private var instance: ServiceLocator? = null

        fun init(context: Context) {
            if (instance != null) {
                return;
            }

            instance = ServiceLocator();
            instance!!.db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "diabetic-database"
            )
                .allowMainThreadQueries()
                .build()
        }

        fun get(): ServiceLocator {
            if (instance == null) {
                throw RuntimeException("Call init method before using service locator!")
            }

            return instance as ServiceLocator
        }
    }

    fun addGlucoseLevelHandler(): AddGlucoseLevel.Handler {
        return AddGlucoseLevel.Handler(
            GlucoseLevelRoomRepository(
                db.glucoseLevelDao()
            )
        )
    }
}