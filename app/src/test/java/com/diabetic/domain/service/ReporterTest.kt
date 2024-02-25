package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.ShortInsulin
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.time.LocalDateTime

class ReporterTest {
    @Test
    fun `creating glucose levels excel report`() {
        val meta =
            ReportMeta(
                ReportMeta.Range(
                    Pair(
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    )
                ),
                "Отчет по глюкозе"
            )
        val levels = MutableList(3) { id ->
            GlucoseLevel(
                id = id,
                value = GlucoseLevel.Value(1.2F),
                type = GlucoseLevel.MeasureType.BEFORE_MEAL,
                date = DateTime()
            )
        }
        val report = File("src/test/res/runtime/glucose_test_report.xlsx")

        report
            .outputStream()
            .generateGlucoseReport(levels, meta)
            .close()

        assertTrue(true)
    }

    @Test
    fun `creating food intakes excel report`() {
        val meta = ReportMeta(
            ReportMeta.Range(
                Pair(
                    LocalDateTime.now(),
                    LocalDateTime.now()
                )
            ),
            "Отчет по приемам пищи"
        )
        val foodIntakes = List(3) { id ->
            FoodIntake(
                id,
                BreadUnit(1),
                ShortInsulin(1.2F),
                DateTime()
            )
        }
        val report = File("src/test/res/runtime/food_intake_test_report.xlsx")

        report
            .outputStream()
            .generateFoodIntakeReport(foodIntakes, meta)
            .close()
    }

    @Test
    fun `creating long insulin excel report`() {
        val meta = ReportMeta(
            ReportMeta.Range(
                Pair(
                    LocalDateTime.now(),
                    LocalDateTime.now()
                )
            ),
            "Отчет по длинному инсулину"
        )
        val longInsulin = List(3) { id ->
            LongInsulin(
                id = id,
                value = 1.2F,
                datetime = DateTime()
            )
        }
        val report = File("src/test/res/runtime/insulin_test_report.xlsx")

        report
            .outputStream()
            .generateLongInsulinReport(longInsulin, meta)
            .close()
    }
}