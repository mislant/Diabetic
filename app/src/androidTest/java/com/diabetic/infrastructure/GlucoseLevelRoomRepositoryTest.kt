package com.diabetic.infrastructure

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistant.AppDatabase
import com.diabetic.infrastructure.persistant.GlucoseLevelRoomRepository
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class GlucoseLevelRoomRepositoryTest {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test()
    fun save_glucose_level() {
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao())
        val glucoseLevel = GlucoseLevel(
            GlucoseLevel.MeasureType.BEFORE_MEAL,
            GlucoseLevel.Value(1.2F),
            LocalDateTime.now()
        )

        repository.persist(glucoseLevel)
        val savedGlucoseLevel = db.glucoseLevelDao().byId(1)

        assertEquals(1, savedGlucoseLevel.id);
        assertEquals("before_meal", savedGlucoseLevel.measureType)
        assertEquals(1.2F, savedGlucoseLevel.value)
    }

    @Test
    fun read_glucose_levels() {
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao())
        val prepared = listOf(
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                LocalDateTime.now()
            ),
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                LocalDateTime.now()
            ),
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                LocalDateTime.now()
            ),
        )
        prepared.forEach { repository.persist(it) }

        val currents = repository.fetchAll()

        assertEquals(3, currents.count())
        for (i in prepared.indices) {
            val concretePrepared = prepared[i]
            val concreteCurrent = currents[i];

            assertEquals(concretePrepared.date(), concreteCurrent.date())
            assertEquals(concretePrepared.value(), concreteCurrent.value())
            assertEquals(concretePrepared.type(), concreteCurrent.type())
        }
    }
}