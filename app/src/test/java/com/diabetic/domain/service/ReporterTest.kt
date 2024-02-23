package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
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
        val report = File("src/test/res/runtime/test_report.xlsx")

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
        val report = File("src/test/res/runtime/test_report.xlsx")

        report
            .outputStream()
            .generateFoodIntakeReport(foodIntakes, meta)
            .close()
    }
}