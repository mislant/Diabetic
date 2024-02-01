package com.diabetic.infrastructure.persistent.file.stub

import com.diabetic.application.command.PrepareGlucoseLevelsReport
import java.io.File
import java.io.FileOutputStream

class TestExcelReportStorage : PrepareGlucoseLevelsReport.ExcelReportStorage {
    override fun new(name: String): File {
        return TestStorage
            .get("${name}.xlsx")
    }
}