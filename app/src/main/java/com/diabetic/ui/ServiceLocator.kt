package com.diabetic.ui

import android.content.Context
import androidx.room.Room
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.infrastructure.persistent.room.AppDatabase
import com.diabetic.infrastructure.persistent.room.GlucoseLevelRoomRepository

class ServiceLocator private constructor() {
    private lateinit var db: AppDatabase

    companion object {
        @Volatile
        private var instance: ServiceLocator? = null

        fun init(context: Context) {
            if (instance != null) {
                return
            }

            instance = ServiceLocator()
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

    fun glucoseLevelRepository(): GlucoseLevelRepository {
        return GlucoseLevelRoomRepository(
            db.glucoseLevelDao()
        )
    }

    fun addGlucoseLevelHandler(): AddGlucoseLevel.Handler {
        return AddGlucoseLevel.Handler(
            GlucoseLevelRoomRepository(
                db.glucoseLevelDao()
            )
        )
    }
}