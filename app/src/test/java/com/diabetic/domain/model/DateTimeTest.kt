package com.diabetic.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeTest {

    @Test
    fun construction_from_string() {
        val time = "2024-01-17T13:30:30.000Z"

        val datetime = DateTime.fromString(time)

        assertEquals(time, datetime.format().iso())
    }

    @Test
    fun invalid_string_value_for_datetime() {
        val time = "12351234123"

        try {
            val datetime = DateTime.fromString(time)

            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun construction_from_empty_parameters() {
        val current = LocalDateTime.now()

        val datetime = DateTime()

        assertEquals(
            current.format(DateTimeFormatter.ofPattern(DateTime.READABLE)),
            datetime.format().readable()
        )
    }
}