package com.diabetic.ui

import android.content.Context
import androidx.room.Room
import com.diabetic.application.command.AddCarbohydrate
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.application.command.BeginFoodIntake
import com.diabetic.application.command.CalculateInsulinBeforeFoodIntake
import com.diabetic.application.command.PrepareGlucoseLevelsReport
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.infrastructure.persistent.file.android.InternalExcelReportStorage
import com.diabetic.infrastructure.persistent.room.AppDatabase
import com.diabetic.infrastructure.persistent.room.CarbohydrateDataStoreStorage
import com.diabetic.infrastructure.persistent.room.FoodIntakeRoomRepository
import com.diabetic.infrastructure.persistent.room.GlucoseLevelRoomRepository
import kotlin.reflect.KClass

object ServiceLocator {
    private val container: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun init(ctx: Context) {
        initDb(ctx)
        initReportStorage(ctx)
    }

    private fun initDb(ctx: Context) {
        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "diabetic-database"
        )
            .allowMainThreadQueries()
            .build()

        container[AppDatabase::class] = db
    }

    private fun initReportStorage(ctx: Context) {
        container[PrepareGlucoseLevelsReport.ExcelReportStorage::class] =
            InternalExcelReportStorage(
                ctx.filesDir
            )
    }

    fun glucoseLevelRepository(): GlucoseLevelRepository {
        return GlucoseLevelRoomRepository(
            get(AppDatabase::class).glucoseLevelDao()
        )
    }

    fun addGlucoseLevelHandler(): AddGlucoseLevel.Handler {
        return AddGlucoseLevel.Handler(
            glucoseLevelRepository()
        )
    }

    fun prepareGlucoseLevelReport(): PrepareGlucoseLevelsReport.Handler {
        return PrepareGlucoseLevelsReport.Handler(
            glucoseLevelRepository(),
            get(PrepareGlucoseLevelsReport.ExcelReportStorage::class)
        )
    }

    fun beginFoodIntakeHandler(): BeginFoodIntake.Handler {
        return BeginFoodIntake.Handler(
            FoodIntakeRoomRepository(get(AppDatabase::class).foodIntakeDao())
        )
    }

    fun addCarbohydrateHandler(): AddCarbohydrate.Handler {
        return AddCarbohydrate.Handler(
            carbohydrateStorage()
        )
    }

    fun carbohydrateStorage(): CarbohydrateStorage {
        return CarbohydrateDataStoreStorage(
            get(AppDatabase::class).keyValueDao()
        )
    }

    fun calculateInsulinBeforeFoodIntakeHandler(): CalculateInsulinBeforeFoodIntake.Handler {
        return CalculateInsulinBeforeFoodIntake.Handler(
            FoodIntakeRoomRepository(get(AppDatabase::class).foodIntakeDao()),
            carbohydrateStorage()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> get(what: KClass<T>): T {
        val obj = container[what] ?: throw RuntimeException("Service not initialized")
        return obj as T
    }
}