package com.diabetic.domain.service

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
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
                )
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
            .excel(levels, meta)
            .close()

        assertTrue(true)
    }
}