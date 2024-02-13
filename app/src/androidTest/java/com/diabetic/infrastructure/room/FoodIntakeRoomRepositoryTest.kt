package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.infrastructure.persistent.room.FoodIntakeRoomRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FoodIntakeRoomRepositoryTest : RoomRepositoryTest() {

    @Before
    fun before() {
        initDb()
    }

    @After
    @Throws(IOException::class)
    fun after() {
        closeDb()
    }

    @Test
    fun `save new food intake`() {
        val repository = FoodIntakeRoomRepository(db.foodIntakeDao())
        val foodIntake = FoodIntake(
            BreadUnit(10),
            ShortInsulin(1F)
        )

        val savedFoodIntake = repository.persist(foodIntake)

        assertEquals(1, savedFoodIntake.id)
    }

    @Test
    fun `fetch food intake with glucose level`() {
        val date = DateTime()
        val prepared = FoodIntake(
            BreadUnit(10),
            ShortInsulin(1F),
            date
        )
        val repository = FoodIntakeRoomRepository(db.foodIntakeDao()).apply {
            persist(prepared)
        }

        val fetched = repository.fetch(1)

        assertNotNull(fetched)
        fetched!!.apply {
            assertEquals(id, prepared.id!!)
            assertEquals(breadUnit.value, prepared.breadUnit.value)
            assertEquals(insulin.value, prepared.insulin.value)
            assertEquals(date.format().iso(), prepared.date.format().iso())
        }
    }
}