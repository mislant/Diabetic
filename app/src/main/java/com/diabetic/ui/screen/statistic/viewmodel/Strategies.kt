package com.diabetic.ui.screen.statistic.viewmodel

class Strategies(
    private val glucoseReport: GlucoseReport,
    private val foodIntakeReport: FoodIntakeReport
) {
    fun of(state: ReportState<out Any>): ReportStrategy<out Any> {
        return when (state) {
            is ReportState.FoodIntakes -> foodIntakeReport
            is ReportState.GlucoseLevels -> glucoseReport
        }
    }
}