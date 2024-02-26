package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareGlucoseLevelsReport.Handler
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository

class GlucoseReport(
    private val repository: GlucoseLevelRepository,
    override val handler: Handler
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

    override fun delete(element: GlucoseLevel) {
        repository.delete(element.id!!)
    }
}