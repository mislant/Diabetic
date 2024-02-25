package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.LongInsulin as DomainLongInsulin

sealed class ReportState<T>(
    val of: Report,
    val data: List<T> = listOf(),
    val filter: LongRange? = null,
) {
    enum class Report(val title: String) {
        GLUCOSE("Глюкоза") {
            override fun state(): ReportState<GlucoseLevel> = GlucoseLevels()
        },
        FOOD_INTAKE("Приемы пищи") {
            override fun state(): ReportState<FoodIntake> = FoodIntakes()
        },
        LONG_INSULIN("Длинный инсулин") {
            override fun state(): ReportState<out Any> = LongInsulin()
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

    class LongInsulin(
        data: List<DomainLongInsulin> = listOf(),
        filter: LongRange? = null
    ) : ReportState<DomainLongInsulin>(Report.LONG_INSULIN, data, filter) {
        override fun copy(
            data: List<DomainLongInsulin>,
            filter: LongRange?
        ): ReportState<DomainLongInsulin> =
            LongInsulin(data, filter)
    }
}
