package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.milliseconds
import com.diabetic.infrastructure.persistent.room.FoodIntakeRoomRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

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
        val date = LocalDateTime.now()
        val prepared = FoodIntake(
            breadUnit = BreadUnit(10),
            insulin = ShortInsulin(1F),
            datetime = date
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
            assertEquals(date, prepared.datetime)
        }
    }

    @Test
    fun `food intakes filtering`() {
        val repository = FoodIntakeRoomRepository(db.foodIntakeDao()).apply {
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-02 02:00:00.000",
                "2024-01-03 00:00:00.000",
            ).forEach {
                persist(
                    FoodIntake(
                        BreadUnit(1),
                        insulin = ShortInsulin(1.2F),
                        it.datetime
                    )
                )
            }
        }

        val fetched = repository.fetch(
            "2024-01-02 00:00:00.000".datetime,
            "2024-01-02 23:59:59.000".datetime
        )

        assertTrue(fetched.isNotEmpty())
        assertEquals(2, fetched.count())
    }

    @Test
    fun `delete food intake record`() {
        val savedId: Int
        val repository = FoodIntakeRoomRepository(db.foodIntakeDao()).apply {
            savedId = persist(FoodIntake(breadUnit = BreadUnit(1), insulin = ShortInsulin(1.2F)))
                .id!!
        }

        repository.delete(savedId)
        val result = repository.fetch(savedId)

        assertNull(result)
    }
}