package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.infrastructure.persistent.room.CarbohydrateDataStoreStorage
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarbohydrateDataStoreStorageTest : RoomRepositoryTest() {

    @Before
    fun before() {
        initDb()
    }

    @After
    fun after() {
        closeDb()
    }

    @Test
    fun `carbohydrate saving works`() {
        val repository = CarbohydrateDataStoreStorage(db.keyValueDao())
        val carbohydrate = Carbohydrate(1F)

        repository.set(carbohydrate)
        val saved = repository.get()

        assertTrue(carbohydrate == saved)
    }

    @Test
    fun `rewrite existed carbohydrate without errors`() {
        val carbohydrate = Carbohydrate(2F)
        val repository = CarbohydrateDataStoreStorage(db.keyValueDao()).apply {
            set(Carbohydrate(1F))
        }

        repository.set(carbohydrate)
        val saved = repository.get()

        assertNotNull(saved)
        assertEquals(carbohydrate.value, saved!!.value)
    }
}