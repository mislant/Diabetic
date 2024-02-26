package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.dateTime
import com.diabetic.infrastructure.persistent.room.LongInsulinRoomRepository
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LongInsulinRoomRepositoryTest : RoomRepositoryTest() {
    @Before
    fun before() {
        initDb()
    }

    @After
    fun after() {
        closeDb()
    }

    @Test
    fun `save long insulin`() {
        val repository = LongInsulinRoomRepository(db.longInsulinDao())
        val longInsulin = LongInsulin(value = 1.2F)

        repository.persist(longInsulin)
        val savedEntity = db
            .longInsulinDao()
            .fetch(1)!!

        assertEquals(longInsulin.id, savedEntity.id)
        assertEquals(longInsulin.value, savedEntity.value)
        assertEquals(longInsulin.datetime.format().iso(), savedEntity.date)
    }

    @Test
    fun `fetching all long insulin`() {
        val repository = LongInsulinRoomRepository(db.longInsulinDao())
        val prepared = List(3) {
            LongInsulin(value = 1.2F).also {
                repository.persist(it)
            }
        }

        val fetched = repository.fetch()

        assertEquals(3, fetched.count())
        repeat(3) {
            assertEquals(prepared[it].id, fetched[it].id)
            assertEquals(prepared[it].value, fetched[it].value)
            assertEquals(
                prepared[it].datetime.format().iso(),
                fetched[it].datetime.format().iso()
            )
        }
    }

    @Test
    fun `filtering long insulin`() {
        val repository = LongInsulinRoomRepository(db.longInsulinDao())
        val dates = listOf(
            "2024-01-01 00:00:00.000",
            "2024-01-02 01:00:00.000",
            "2024-01-02 02:00:00.000",
            "2024-01-03 00:00:00.000",
        ).onEach { date ->
            LongInsulin(
                value = 1.2F,
                datetime = date.dateTime
            ).also {
                repository.persist(it)
            }
        }

        val fetched = repository.fetch(
            "2024-01-02 00:00:00.000".dateTime.localDataTime(),
            "2024-01-02 23:59:59.000".dateTime.localDataTime(),
        )

        assertEquals(2, fetched.count())
        assertEquals(dates[1], fetched[0].datetime.format().iso())
        assertEquals(dates[2], fetched[1].datetime.format().iso())
    }

    @Test
    fun `delete long insulin record`() {
        val savedId: Int
        val repository = LongInsulinRoomRepository(db.longInsulinDao()).apply {
            savedId = persist(LongInsulin(value = 1.2F)).id!!
        }

        repository.delete(savedId)
        val result = repository.fetch(savedId)

        Assert.assertNull(result)
    }
}