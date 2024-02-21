package com.diabetic.ui.screen.statistic.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.ui.screen.statistic.viewmodel.ReportState

@Composable
fun ReportTable(state: ReportState<out Any>) {
    when (state) {
        is ReportState.GlucoseLevels -> GlucoseLevelsTable(state.data)
        is ReportState.FoodIntakes -> FoodIntakeTable(state.data)
    }
}

@Composable
private fun GlucoseLevelsTable(glucoseLevels: List<GlucoseLevel> = listOf()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88F)
            .padding(horizontal = 7.dp)
    ) {
        Table(
            headers = listOf(
                "#", "Уровень", "Дата", "До\\После еды"
            ),
            elements = glucoseLevels.mapIndexed() { i, level ->
                listOf(
                    (i + 1).toString(),
                    level.value.level.toString(),
                    level.date.format().readableDate(),
                    when (level.type) {
                        GlucoseLevel.MeasureType.BEFORE_MEAL -> "До"
                        GlucoseLevel.MeasureType.AFTER_MEAL -> "После"
                        GlucoseLevel.MeasureType.UNSPECIFIED -> "-"
                    }
                )
            },
            weights = floatArrayOf(
                .1F,
                .2F,
                .35F,
                .25F,
            )
        )
    }
}

@Composable
private fun FoodIntakeTable(foodIntakes: List<FoodIntake>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88F)
            .padding(horizontal = 7.dp)
    ) {
        Table(
            headers = listOf(
                "#", "Инсулин", "Хлебные единицы", "Дата"
            ),
            elements = foodIntakes.mapIndexed() { i, foodIntake ->
                listOf(
                    (i + 1).toString(),
                    foodIntake.insulin.value.toString(),
                    foodIntake.breadUnit.value.toString(),
                    foodIntake.date.format().readableDate()
                )
            },
            weights = floatArrayOf(
                .1F,
                .25F,
                .25F,
                .35F,
            )
        )
    }
}