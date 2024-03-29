package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevel.MeasureType
import com.diabetic.domain.model.GlucoseLevel.Value
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.readable
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
            MeasureType.BEFORE_MEAL,
            Value(1.2F),
        )

        repository.persist(glucoseLevel)
        val savedGlucoseLevel = db.glucoseLevelDao().fetch(1)!!

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
                    MeasureType.BEFORE_MEAL,
                    Value(1.2F),
                ),
            )
        }
        prepared.forEach { repository.persist(it) }

        val currents = repository.fetch()

        assertEquals(3, currents.count())
        for (i in prepared.indices) {
            val concretePrepared = prepared[i]
            val concreteCurrent = currents[i]

            assertEquals(
                concretePrepared.datetime.readable,
                concreteCurrent.datetime.readable
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
                MeasureType.BEFORE_MEAL,
                Value(1.2F),
                dates[i].datetime
            ).also { repository.persist(it) }
        }

        val fetched = repository.fetch(
            "2024-01-01 00:00:00.000".datetime,
            "2024-01-02 02:00:00.000".datetime
        )

        assertEquals(3, fetched.count())
        fetched.forEachIndexed { i, concreteFetched ->
            val concretePrepared = stored[i]
            assertEquals(
                concretePrepared.datetime.readable,
                concreteFetched.datetime.readable
            )
            assertEquals(concretePrepared.value, concreteFetched.value)
            assertEquals(concretePrepared.type, concreteFetched.type)
        }
    }

    @Test
    fun `delete glucose record`() {
        val savedId: Int = 0
        val repository = GlucoseLevelRoomRepository(db.glucoseLevelDao()).apply {
            persist(
                GlucoseLevel(
                    type = MeasureType.BEFORE_MEAL,
                    value = Value(1.2F),
                )
            )
        }

        repository.delete(savedId)
        val result = repository.fetch(savedId)

        assertNull(result)
    }
}