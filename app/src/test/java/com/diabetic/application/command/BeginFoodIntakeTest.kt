package com.diabetic.application.command

import com.diabetic.domain.model.FoodIntakeRepository
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
        val handler = BeginFoodIntake.Handler(StubFoodIntakeRepository())
        val command = BeginFoodIntake.Command(
            1.2F,
            10
        )

        handler.handle(command)

        assertTrue(true)
    }

    @Test
    fun `errors on trying to add invalid food intake`() {
        val handler = BeginFoodIntake.Handler(repository)
        val invalidCommands = mapOf(
            Pair("Glucose level can not be less or equal zero", BeginFoodIntake.Command(0.0F, 10)),
            Pair("Glucose level can not be less or equal zero", BeginFoodIntake.Command(-1.0F, 10)),
            Pair("Bread units can not be less or equal zero", BeginFoodIntake.Command(1.2F, 0)),
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