package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.IllegalArgumentException

class AddGlucoseLevelTest {
    @Mock
    private lateinit var repository: GlucoseLevelRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun adding_valid_glucose_level() {
        val handler = AddGlucoseLevel.Handler(repository)
        val command = AddGlucoseLevel.Command(
            1.2F,
            GlucoseLevel.MeasureType.BEFORE_MEAL
        )

        handler.handle(command);

        assertTrue(true)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalid_glucose_level() {
        val handler = AddGlucoseLevel.Handler(repository)
        val command = AddGlucoseLevel.Command(
            -1.2F,
            GlucoseLevel.MeasureType.BEFORE_MEAL
        )

        handler.handle(command);
    }
}