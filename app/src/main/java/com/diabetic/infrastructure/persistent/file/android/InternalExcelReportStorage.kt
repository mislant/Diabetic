package com.diabetic.infrastructure.persistent.file.android

import com.diabetic.application.command.PrepareGlucoseLevelsReport
import java.io.File

class InternalExcelReportStorage(private var dir: File) :
    PrepareGlucoseLevelsReport.ExcelReportStorage {

    init {
        val reportDir = File(dir, "/report")

        if (!reportDir.exists()) {
            reportDir.mkdir()
        }

        dir = reportDir
    }

    override fun new(name: String): File {
        val report = File(dir, "/${name}.xlsx")

        if (report.exists()) {
            report.delete()
        }

        return report
    }
}