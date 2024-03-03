package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.ui.screen.statistic.viewmodel.ReportState.FoodIntakes
import com.diabetic.ui.screen.statistic.viewmodel.ReportState.GlucoseLevels
import com.diabetic.ui.screen.statistic.viewmodel.ReportState.LongInsulin

class Strategies(
    private val glucoseReport: GlucoseReport,
    private val foodIntakeReport: FoodIntakeReport,
    private val longInsulinReport: LongInsulinReport
) {
    fun of(state: ReportState<Any>): ReportStrategy<Any> {
        return when (state) {
            is FoodIntakes -> foodIntakeReport
            is GlucoseLevels -> glucoseReport
            is LongInsulin -> longInsulinReport
        }
    }
}