package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.iso
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
        assertEquals(longInsulin.datetime.iso, savedEntity.datetime.datetime.iso)
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
                prepared[it].datetime.iso,
                fetched[it].datetime.iso
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
                datetime = date.datetime
            ).also {
                repository.persist(it)
            }
        }

        val fetched = repository.fetch(
            "2024-01-02 00:00:00.000".datetime,
            "2024-01-02 23:59:59.000".datetime,
        )

        assertEquals(2, fetched.count())
        assertEquals(dates[1], fetched[0].datetime.iso)
        assertEquals(dates[2], fetched[1].datetime.iso)
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