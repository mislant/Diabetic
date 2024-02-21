package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import java.io.OutputStream

class FoodIntakeReport(
    private val repository: FoodIntakeRepository
) : ReportStrategy<FoodIntake>() {
    override fun fetch(filter: LongRange?): List<FoodIntake> {
        return if (filter == null) fetch() else repository.fetch(
            filter.from.asFromLocalDateTime(),
            filter.to.asToLocalDateTime()
        )
    }

    private fun fetch(): List<FoodIntake> {
        return repository.fetch()
    }

    override fun generateReportName(filter: LongRange?): String {
        TODO("Not yet implemented")
    }

    override fun generateReport(filter: LongRange?, stream: OutputStream) {
        TODO("Not yet implemented")
    }
}