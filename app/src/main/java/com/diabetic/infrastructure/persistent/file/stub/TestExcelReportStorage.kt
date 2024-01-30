package com.diabetic.infrastructure.persistent.file.stub

import com.diabetic.application.command.PrepareGlucoseLevelsReport
import java.io.FileOutputStream

class TestExcelReportStorage : PrepareGlucoseLevelsReport.ExcelReportStorage {
    override fun new(name: String): FileOutputStream {
        return TestStorage
            .get("${name}.xlsx")
            .outputStream()
    }
}