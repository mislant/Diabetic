package com.diabetic.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime

class DateTimeTest {

    @Test
    fun `date constructing from ISO string`() {
        val time = "2024-01-17 13:30:30.000"

        val datetime = DateTime.fromString(time)

        assertEquals(time, datetime.format().iso())
    }

    @Test
    fun `exception on invalid date string`() {
        val time = "12351234123"

        assertThrows(IllegalArgumentException::class.java) {
            DateTime.fromString(time)
        }
    }

    @Test
    fun `creating current datetime value from empty constructor`() {
        val current = LocalDateTime.now()

        val datetime = DateTime()

        assertEquals(
            current.readable(),
            datetime.format().readable()
        )
    }
}