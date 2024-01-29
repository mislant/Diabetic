package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistent.room.GlucoseLevelRoomRepository
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GlucoseLevelRoomRepositoryTest : RoomRepositoryTest() {

    @Before
    fun before() {
        initDb()
    }

    @After
    @Throws(IOException::class)
    fun after() {
        closeDb()
    }

    @Test()
    fun `save glucose level`() {
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao())
        val glucoseLevel = GlucoseLevel(
            GlucoseLevel.MeasureType.BEFORE_MEAL,
            GlucoseLevel.Value(1.2F),
            DateTime()
        )

        repository.persist(glucoseLevel)
        val savedGlucoseLevel = db.glucoseLevelDao().byId(1)

        assertEquals(1, savedGlucoseLevel.id)
        assertEquals("before_meal", savedGlucoseLevel.measureType)
        assertEquals(1.2F, savedGlucoseLevel.value)
    }

    @Test
    fun `read glucose levels`() {
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao())
        val prepared = mutableListOf<GlucoseLevel>()
        repeat(3) {
            prepared.add(
                GlucoseLevel(
                    GlucoseLevel.MeasureType.BEFORE_MEAL,
                    GlucoseLevel.Value(1.2F),
                    DateTime()
                ),
            )
        }
        prepared.forEach { repository.persist(it) }

        val currents = repository.fetchAll()

        assertEquals(3, currents.count())
        for (i in prepared.indices) {
            val concretePrepared = prepared[i]
            val concreteCurrent = currents[i]

            assertEquals(
                concretePrepared.date.format().readable(),
                concreteCurrent.date.format().readable()
            )
            assertEquals(concretePrepared.value, concreteCurrent.value)
            assertEquals(concretePrepared.type, concreteCurrent.type)
        }
    }

    @Test
    fun `read levels from time range`() {
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao())
        val dates = listOf(
            "2024-01-01 00:00:00.000",
            "2024-01-02 01:00:00.000",
            "2024-01-02 02:00:00.000",
            "2024-01-03 00:00:00.000",
        )
        val stored = List(4) { i ->
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                DateTime.fromString(dates[i])
            ).also { repository.persist(it) }
        }

        val fetched = repository.fetchRange(
            DateTime.fromString("2024-01-01 00:00:00.000").localDataTime(),
            DateTime.fromString("2024-01-02 02:00:00.000").localDataTime()
        )

        assertEquals(3, fetched.count())
        fetched.forEachIndexed { i, concreteFetched ->
            val concretePrepared = stored[i]
            assertEquals(
                concretePrepared.date.format().readable(),
                concreteFetched.date.format().readable()
            )
            assertEquals(concretePrepared.value, concreteFetched.value)
            assertEquals(concretePrepared.type, concreteFetched.type)
        }
    }
}