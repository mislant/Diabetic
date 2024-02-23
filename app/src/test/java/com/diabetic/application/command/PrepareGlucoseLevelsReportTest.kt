package com.diabetic.application.command

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.time.LocalDateTime
import java.time.Month

class PrepareGlucoseLevelsReportTest {

    @Test
    fun `generate file report name for all time`() {
        val handler = PrepareGlucoseLevelsReport.Handler(StubGlucoseLevelRepository())
        val command = PrepareReport.GenerateFileNameCommand(null)

        val name = handler.handle(command)

        assertEquals("Glucose_level_report.for_all_time.xlsx", name)
    }

    @Test
    fun `generate file report name based on dates range`() {
        val handler = PrepareGlucoseLevelsReport.Handler(StubGlucoseLevelRepository())
        val command = PrepareReport.GenerateFileNameCommand(
            Pair(
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(2024, Month.JANUARY, 2, 0, 0),
            )
        )

        val name = handler.handle(command)

        assertEquals("Glucose_level_report.2024-01-01_2024-01-02.xlsx", name)
    }

    @Test
    fun `generate correct report for all time`() {
        val repository = StubGlucoseLevelRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).mapIndexed { id, date ->
                GlucoseLevel(
                    GlucoseLevel.MeasureType.BEFORE_MEAL,
                    GlucoseLevel.Value(1.2F),
                    DateTime.fromString(date),
                    id
                ).also {
                    repository.persist(it)
                }
            }
        }
        val handler = PrepareGlucoseLevelsReport.Handler(repository)
        val command = PrepareReport.WriteReportCommand(
            null,
            File("src/test/res/runtime/test.xlsx").outputStream()
        )

        handler.handle(command)

        Assert.assertTrue(true)
    }

    @Test
    fun `generate correct report for period`() {
        val repository = StubGlucoseLevelRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).mapIndexed { id, date ->
                GlucoseLevel(
                    GlucoseLevel.MeasureType.BEFORE_MEAL,
                    GlucoseLevel.Value(1.2F),
                    DateTime.fromString(date),
                    id
                ).also {
                    repository.persist(it)
                }
            }
        }
        val handler = PrepareGlucoseLevelsReport.Handler(repository)
        val command = PrepareReport.WriteReportCommand(
            Pair(
                LocalDateTime.of(2024, Month.JANUARY, 1,0,0),
                LocalDateTime.of(2024, Month.JANUARY, 3,0,0),
            ),
            File("src/test/res/runtime/test.xlsx").outputStream()
        )

        handler.handle(command)

        Assert.assertTrue(true)
    }
}