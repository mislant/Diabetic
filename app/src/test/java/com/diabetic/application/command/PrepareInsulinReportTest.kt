package com.diabetic.application.command

import com.diabetic.application.command.PrepareLongInsulinReport.Handler
import com.diabetic.application.command.PrepareReport.GenerateFileNameCommand
import com.diabetic.application.command.PrepareReport.WriteReportCommand
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.time.datetime
import com.diabetic.infrastructure.persistent.stub.StubLongInsulinRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.time.LocalDateTime
import java.time.Month

class PrepareInsulinReportTest {
    @Test
    fun `generate file report name for all time`() {
        val handler = Handler(StubLongInsulinRepository())
        val command = GenerateFileNameCommand(null)

        val name = handler.handle(command)

        assertEquals("Long_insulin_report.for_all_time.xlsx", name)
    }

    @Test
    fun `generate file report name based on dates range`() {
        val handler = Handler(StubLongInsulinRepository())
        val command = PrepareReport.GenerateFileNameCommand(
            Pair(
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(2024, Month.JANUARY, 2, 0, 0),
            )
        )

        val name = handler.handle(command)

        assertEquals("Long_insulin_report.2024-01-01_2024-01-02.xlsx", name)
    }

    @Test
    fun `generate correct report for all time`() {
        val repository = StubLongInsulinRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).onEach { date ->
                LongInsulin(
                    value = 1.2F,
                    datetime = date.datetime,
                ).also {
                    repository.persist(it)
                }
            }
        }
        val handler = Handler(repository)
        val command = WriteReportCommand(
            null,
            File("src/test/res/runtime/test.xlsx").outputStream()
        )

        handler.handle(command)

        assertTrue(true)
    }

    @Test
    fun `generate correct report for period`() {
        val repository = StubLongInsulinRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).onEach { date ->
                LongInsulin(
                    value = 1.2F,
                    datetime = date.datetime,
                ).also {
                    repository.persist(it)
                }
            }
        }
        val handler = Handler(repository)
        val command = WriteReportCommand(
            Pair(
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(2024, Month.JANUARY, 3, 0, 0),
            ),
            File("src/test/res/runtime/test.xlsx").outputStream()
        )

        handler.handle(command)

        assertTrue(true)
    }
}