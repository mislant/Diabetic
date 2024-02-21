package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel

sealed class ReportState<T>(
    val of: Report,
    val data: List<T> = listOf(),
    val filter: LongRange? = null,
) {
    enum class Report(val title: String) {
        GLUCOSE("По глюкозе") {
            override fun state(): ReportState<GlucoseLevel> = GlucoseLevels()
        },
        FOOD_INTAKE("По приемам пищи") {
            override fun state(): ReportState<FoodIntake> = FoodIntakes()
        };

        abstract fun state(): ReportState<out Any>
    }

    abstract fun copy(data: List<T> = this.data, filter: LongRange? = this.filter): ReportState<T>

    override fun toString(): String {
        return "ReportState(of=$of, data=$data, filter=$filter)"
    }

    class GlucoseLevels(
        data: List<GlucoseLevel> = listOf(),
        filter: LongRange? = null
    ) : ReportState<GlucoseLevel>(Report.GLUCOSE, data, filter) {
        override fun copy(data: List<GlucoseLevel>, filter: LongRange?): ReportState<GlucoseLevel> =
            GlucoseLevels(data, filter)
    }

    class FoodIntakes(
        data: List<FoodIntake> = listOf(),
        filter: LongRange? = null
    ) : ReportState<FoodIntake>(Report.FOOD_INTAKE, data, filter) {
        override fun copy(data: List<FoodIntake>, filter: LongRange?): ReportState<FoodIntake> =
            FoodIntakes(data, filter)
    }
}
