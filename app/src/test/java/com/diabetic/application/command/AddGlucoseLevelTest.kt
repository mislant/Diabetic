package com.diabetic.application.command

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddGlucoseLevelTest {
    @Mock
    private lateinit var repository: GlucoseLevelRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `adding valid glucose level`() {
        val handler = AddGlucoseLevel.Handler(repository)
        val command = AddGlucoseLevel.Command(
            1.2F,
            GlucoseLevel.MeasureType.BEFORE_MEAL
        )

        handler.handle(command);

        assertTrue(true)
    }

    @Test
    fun `throws exception on invalid glucose level`() {
        val handler = AddGlucoseLevel.Handler(repository)
        val command = AddGlucoseLevel.Command(
            -1.2F,
            GlucoseLevel.MeasureType.BEFORE_MEAL
        )

        assertThrows(IllegalArgumentException::class.java) {
            handler.handle(command);
        }
    }
}