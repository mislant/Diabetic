package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareFoodIntakeReport
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository

class FoodIntakeReport(
    private val repository: FoodIntakeRepository,
    override val handler: PrepareFoodIntakeReport.Handler
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

    override fun delete(element: FoodIntake) {
        repository.delete(element.id!!)
    }
}