package com.diabetic.application.command

import com.diabetic.domain.model.CarbohydrateStorage
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddCarbohydrateTest {
    @Mock
    private lateinit var storage: CarbohydrateStorage

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `add valid carbohydrate`() {
        val handler = AddCarbohydrate.Handler(storage)
        val command = AddCarbohydrate.Command(1F)

        handler.handle(command)

        assertTrue(true)
    }
}