package com.diabetic.application.command

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.readable
import com.diabetic.infrastructure.persistent.file.stub.TestExcelReportStorage
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files

class PrepareGlucoseReportTest {

    @Test
    fun `preparing glucose excel report`() {
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
        val storage = TestExcelReportStorage()
        val handler = PrepareGlucoseLevelsReport.Handler(repository, storage)
        val command = PrepareGlucoseLevelsReport.Command(
            DateTime.fromString("2024-01-01 00:00:00.000").localDataTime(),
            DateTime.fromString("2024-01-02 23:59:59.000").localDataTime()
        )

        val file = handler.handle(command)

        assertTrue(file.exists())
        assertTrue(file.length() > 0)
        assertEquals(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            Files.probeContentType(file.toPath())
        )
        assertEquals(
            "Glucose_report.2024-01-01_00:00-2024-01-02_23:59.xlsx",
            file.name
        )
    }
}