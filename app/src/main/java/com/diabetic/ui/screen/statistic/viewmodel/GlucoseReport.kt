package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareGlucoseLevelsReport.GenerateFileNameCommand
import com.diabetic.application.command.PrepareGlucoseLevelsReport.Handler
import com.diabetic.application.command.PrepareGlucoseLevelsReport.WriteReportCommand
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import java.io.OutputStream

class GlucoseReport(
    private val repository: GlucoseLevelRepository,
    private val handler: Handler
) : ReportStrategy<GlucoseLevel>() {

    override fun fetch(filter: LongRange?): List<GlucoseLevel> {
        return if (filter == null) fetch() else repository.fetch(
            filter.from.asFromLocalDateTime(),
            filter.to.asToLocalDateTime()
        )
    }

    private fun fetch(): List<GlucoseLevel> {
        return repository.fetch()
    }

    override fun generateReportName(filter: LongRange?): String {
        return handler.handle(
            GenerateFileNameCommand(
                if (filter == null) null else Pair(
                    filter.from.asLocalDateTime(),
                    filter.to.asLocalDateTime()
                )
            )
        )
    }

    override fun generateReport(filter: LongRange?, stream: OutputStream) {
        return handler.handle(
            WriteReportCommand(
                if (filter == null) null else Pair(
                    filter.from.asLocalDateTime(),
                    filter.to.asLocalDateTime()
                ),
                stream
            )
        )
    }
}