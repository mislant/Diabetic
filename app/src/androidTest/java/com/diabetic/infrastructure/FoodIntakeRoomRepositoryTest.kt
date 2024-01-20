package com.diabetic.infrastructure

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistant.FoodIntakeRoomRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

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
    fun save_new_food_intake() {
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

        Assert.assertEquals(1, savedFoodIntake.id)
    }
}