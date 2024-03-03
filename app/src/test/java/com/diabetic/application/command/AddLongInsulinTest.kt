package com.diabetic.application.command

import com.diabetic.domain.model.LongInsulinRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddLongInsulinTest {
    @Mock
    private lateinit var repository: LongInsulinRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `adding valid long insulin`() {
        val handler = AddLongInsulin.Handler(repository)
        val command = AddLongInsulin.Command(
            1.2F,
        )

        handler.handle(command)

        assertTrue(true)
    }
}