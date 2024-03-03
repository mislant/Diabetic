package com.diabetic.application.command

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.service.InsulinCalculator
import com.diabetic.infrastructure.persistent.stub.StubCarbohydrateStorage
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BeginFoodIntakeTest {
    @Mock
    private lateinit var repository: FoodIntakeRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `adding food intake`() {
        val handler = BeginFoodIntake.Handler(
            StubFoodIntakeRepository(),
            StubCarbohydrateStorage().apply {
                set(Carbohydrate(1.1F))
            },
        )
        val command = BeginFoodIntake.Command(1)

        handler.handle(command)

        assertTrue(true)
    }

    @Test
    fun `errors on trying to add invalid food intake`() {
        val handler = BeginFoodIntake.Handler(repository, StubCarbohydrateStorage().apply {
            set(Carbohydrate(1.1F))
        })
        val invalidCommands = mapOf(
            Pair("Bread units can not be less or equal zero", BeginFoodIntake.Command(0)),
        )

        invalidCommands.forEach {
            try {
                handler.handle(it.value)
            } catch (e: IllegalArgumentException) {
                assertEquals(it.key, e.message)
            }
        }
    }
}