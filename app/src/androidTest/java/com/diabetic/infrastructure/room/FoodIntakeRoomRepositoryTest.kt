package com.diabetic.infrastructure.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
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
        val date = DateTime()
        val foodIntake = FoodIntake(
            BreadUnit(10),
            date,
            GlucoseLevel.beforeMeal(
                GlucoseLevel.Value(1.2F),
                date
            )
        )

        val savedFoodIntake = repository.persist(foodIntake)

        assertEquals(1, savedFoodIntake.id)
    }

    @Test
    fun `fetch food intake with glucose level`() {
        val date = DateTime()
        val prepared = FoodIntake(
            BreadUnit(10),
            date,
            GlucoseLevel.beforeMeal(
                GlucoseLevel.Value(1.2F),
                date
            )
        )
        val repository = FoodIntakeRoomRepository(db.foodIntakeDao()).apply {
            persist(prepared)
        }

        val fetched = repository.getById(1)

        assertNotNull(fetched)
        fetched!!.apply {
            assertEquals(id, prepared.id!!)
            assertEquals(date.format().iso(), prepared.date.format().iso())

            glucoseBeforeMeal.apply {
                assertEquals(type, prepared.glucoseBeforeMeal.type)
                assertEquals(value.level, prepared.glucoseBeforeMeal.value.level)
                assertEquals(
                    date.format().iso(),
                    prepared.glucoseBeforeMeal.date.format().iso()
                )
            }
        }
    }
}