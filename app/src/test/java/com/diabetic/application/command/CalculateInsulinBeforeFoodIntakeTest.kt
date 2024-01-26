package com.diabetic.application.command

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateRequired
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.NotFound
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CalculateInsulinBeforeFoodIntakeTest {

    @Test
    fun success_calculating_insulin() {
        val handler = CalculateInsulinBeforeFoodIntake.Handler(
            Preset.repository(),
            Preset.storage()
        )
        val command = CalculateInsulinBeforeFoodIntake.Command(
            Preset.foodIntake.id!!
        )

        val insulin = handler.handle(command)

        assertEquals(2.2F, insulin.value)
    }

    @Test
    fun expect_food_intake_not_found() {
        val handler = CalculateInsulinBeforeFoodIntake.Handler(
            Preset.emptyRepository(),
            Preset.storage()
        )
        val command = CalculateInsulinBeforeFoodIntake.Command(
            Preset.foodIntake.id!!
        )

        assertThrows(NotFound::class.java) {
            handler.handle(command)
        }
    }

    @Test
    fun expect_carbohydrate_required() {
        val handler = CalculateInsulinBeforeFoodIntake.Handler(
            Preset.repository(),
            Preset.emptyStorage()
        )
        val command = CalculateInsulinBeforeFoodIntake.Command(
            Preset.foodIntake.id!!
        )

        assertThrows(CarbohydrateRequired::class.java) {
            handler.handle(command)
        }
    }
}

private object Preset {
    val foodIntake: FoodIntake
        get() {
            return FoodIntake(
                1,
                BreadUnit(2),
                DateTime(),
                GlucoseLevel.beforeMeal(
                    GlucoseLevel.Value(1.2F),
                    DateTime()
                ),
            )
        }

    val carbohydrate: Carbohydrate
        get() {
            return Carbohydrate(1.1F)
        }

    fun repository(): FoodIntakeRepository {
        val repository = mock(FoodIntakeRepository::class.java)
        `when`(repository.getById(foodIntake.id!!))
            .thenReturn(foodIntake)
        return repository;
    }

    fun emptyRepository(): FoodIntakeRepository {
        val repository = mock(FoodIntakeRepository::class.java)
        `when`(repository.getById(foodIntake.id!!))
            .thenReturn(null)
        return repository
    }

    fun storage(): CarbohydrateStorage {
        val storage = mock(CarbohydrateStorage::class.java)
        `when`(storage.get())
            .thenReturn(carbohydrate)
        return storage
    }

    fun emptyStorage(): CarbohydrateStorage {
        val storage = mock(CarbohydrateStorage::class.java)
        `when`(storage.get()).thenReturn(null)
        return storage
    }
}