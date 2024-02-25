package com.diabetic.infrastructure.room

import com.diabetic.domain.model.LongInsulin
import com.diabetic.infrastructure.persistent.room.LongInsulinRoomRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
            .fetch(1)

        Assert.assertEquals(longInsulin.id, savedEntity.id)
        Assert.assertEquals(longInsulin.value, savedEntity.value)
        Assert.assertEquals(longInsulin.datetime.format().iso(), savedEntity.date)
    }
}