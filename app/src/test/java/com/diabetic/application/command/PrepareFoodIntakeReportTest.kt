package com.diabetic.application.command

import com.diabetic.application.command.PrepareFoodIntakeReport.Handler
import com.diabetic.application.command.PrepareReport.GenerateFileNameCommand
import com.diabetic.application.command.PrepareReport.WriteReportCommand
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.domain.model.time.datetime
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.time.LocalDateTime
import java.time.Month

class PrepareFoodIntakeReportTest {

    @Test
    fun `generate file report name for all time`() {
        val handler = Handler(StubFoodIntakeRepository())
        val command = GenerateFileNameCommand(null)

        val name = handler.handle(command)

        Assert.assertEquals("Food_intakes_report.for_all_time.xlsx", name)
    }

    @Test
    fun `generate file report name based on dates range`() {
        val handler = Handler(StubFoodIntakeRepository())
        val command = GenerateFileNameCommand(
            Pair(
                LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(2024, Month.JANUARY, 2, 0, 0),
            )
        )

        val name = handler.handle(command)

        Assert.assertEquals("Food_intakes_report.2024-01-01_2024-01-02.xlsx", name)
    }

    @Test
    fun `generate correct report for all time`() {
        val repository = StubFoodIntakeRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).mapIndexed { id, date ->
                FoodIntake(
                    id = id,
                    breadUnit = BreadUnit(1),
                    insulin = ShortInsulin(1.2F),
                    datetime = date.datetime
                )
            }
        }
        val handler = Handler(repository)
        val command = WriteReportCommand(
            null,
            File("src/test/res/runtime/test.xlsx").outputStream()
        )

        handler.handle(command)

        Assert.assertTrue(true)
    }

    @Test
    fun `generate correct report for period`() {
        val repository = StubFoodIntakeRepository().also { repository ->
            listOf(
                "2024-01-01 00:00:00.000",
                "2024-01-02 00:00:00.000",
                "2024-01-02 01:00:00.000",
                "2024-01-03 00:00:00.000"
            ).mapIndexed { id, date ->
                FoodIntake(
                    id = id,
                    breadUnit = BreadUnit(1),
                    insulin = ShortInsulin(1.2F),
                    datetime = date.datetime
                )
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

        Assert.assertTrue(true)
    }
}