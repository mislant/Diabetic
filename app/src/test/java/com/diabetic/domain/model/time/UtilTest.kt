package com.diabetic.domain.model.time

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UtilTest {
    @Test
    fun `parsing string in appropriate formats to datetime`() {
        val dates = arrayOf(
            "2024-01-01 00:00:00.000",
            "2024-01-01 00:00"
        )

        dates.forEach {
            it.datetime
            assertTrue(true)
        }
    }

    @Test
    fun `convert datetime to milliseconds`() {
        val datetime = "2024-01-01 00:00:00.000".datetime
        val prepared = 1704045600000

        val result = datetime.milliseconds

        assertEquals(prepared, result)
    }

    @Test
    fun `convert string to milliseconds`() {
        val datetime = "2024-01-01 00:00:00.000"
        val prepared = 1704045600000

        val result = datetime.milliseconds

        assertEquals(prepared, result)
    }

    @Test
    fun `convert long to local date time`() {
        val long = 1704045600000
        val prepared = "2024-01-01 00:00:00.000".datetime

        val result = long.datetime

        assertEquals(prepared, result)
    }
}