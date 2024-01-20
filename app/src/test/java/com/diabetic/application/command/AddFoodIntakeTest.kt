package com.diabetic.application.command

import com.diabetic.domain.model.FoodIntakeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddFoodIntakeTest {
    @Mock
    private lateinit var repository: FoodIntakeRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun adding_valid_food_intake() {
        val handler = AddFoodIntake.Handler(repository)
        val command = AddFoodIntake.Command(
            1.2F,
            10
        )

        handler.handle(command);

        assertTrue(true)
    }

    @Test
    fun trying_to_add_invalid_food_intake() {
        val handler = AddFoodIntake.Handler(repository)
        val invalidCommands = mapOf(
            Pair("Glucose level can not be less or equal zero", AddFoodIntake.Command(0.0F, 10)),
            Pair("Glucose level can not be less or equal zero", AddFoodIntake.Command(-1.0F, 10)),
            Pair("Bread units can not be less or equal zero", AddFoodIntake.Command(1.2F, 0)),
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