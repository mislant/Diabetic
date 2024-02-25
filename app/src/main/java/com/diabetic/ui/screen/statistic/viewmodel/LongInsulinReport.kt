package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareLongInsulinReport.Handler
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository

class LongInsulinReport(
    private val repository: LongInsulinRepository,
    override val handler: Handler
) : ReportStrategy<LongInsulin>() {
    override fun fetch(filter: LongRange?): List<LongInsulin> {
        return if (filter == null) fetch() else repository.fetch(
            filter.from.asFromLocalDateTime(),
            filter.to.asToLocalDateTime()
        )
    }

    private fun fetch(): List<LongInsulin> {
        return repository.fetch()
    }
}