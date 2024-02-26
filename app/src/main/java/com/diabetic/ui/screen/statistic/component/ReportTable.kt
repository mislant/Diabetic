package com.diabetic.ui.screen.statistic.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.ui.screen.statistic.viewmodel.ReportState
import com.diabetic.ui.screen.statistic.viewmodel.ReportState.FoodIntakes
import com.diabetic.ui.screen.statistic.viewmodel.ReportState.GlucoseLevels
import com.diabetic.ui.screen.statistic.viewmodel.ReportState.LongInsulin
import com.diabetic.domain.model.LongInsulin as DomainLongInsulin

@Composable
fun ReportTable(state: ReportState<Any>, removeRecord: (rowIndex: Int) -> Unit) {
    when (state) {
        is GlucoseLevels -> GlucoseLevelsTable(state.data, removeRecord)
        is FoodIntakes -> FoodIntakeTable(state.data, removeRecord)
        is LongInsulin -> LongInsulinTable(state.data, removeRecord)
    }
}

@Composable
private fun GlucoseLevelsTable(
    glucoseLevels: List<GlucoseLevel> = listOf(),
    removeRecord: (rowIndex: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88F)
            .padding(horizontal = 7.dp)
    ) {
        Table(
            headers = listOf(
                "#", "Уровень", "До\\После\nеды", "Дата"
            ),
            elements = glucoseLevels.mapIndexed() { i, level ->
                listOf(
                    (i + 1).toString(),
                    level.value.level.toString(),
                    when (level.type) {
                        GlucoseLevel.MeasureType.BEFORE_MEAL -> "До"
                        GlucoseLevel.MeasureType.AFTER_MEAL -> "После"
                        GlucoseLevel.MeasureType.UNSPECIFIED -> "-"
                    },
                    level.date.format().readableDate(),
                )
            },
            actions = { rowNumber ->
                Actions { removeRecord(rowNumber) }
            },
            weights = listOf(
                .05F,
                .2F,
                .2F,
                .25F,
                .05F
            )
        )
    }
}

@Composable
private fun FoodIntakeTable(foodIntakes: List<FoodIntake>, removeRecord: (rowIndex: Int) -> Unit) {
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
            actions = { rowNumber ->
                Actions { removeRecord(rowNumber) }
            },
            weights = listOf(
                .1F,
                .25F,
                .25F,
                .35F,
                .1F
            )
        )
    }
}

@Composable
private fun LongInsulinTable(
    longInsulin: List<DomainLongInsulin>,
    removeRecord: (rowIndex: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88F)
            .padding(horizontal = 7.dp)
    ) {
        Table(
            headers = listOf(
                "#", "Инсулин", "Дата"
            ),
            elements = longInsulin.mapIndexed() { i, longInsulin ->
                listOf(
                    (i + 1).toString(),
                    longInsulin.value.toString(),
                    longInsulin.datetime.format().readableDate()
                )
            },
            actions = { rowNumber ->
                Actions { removeRecord(rowNumber) }
            },
            weights = listOf(
                .1F,
                .35F,
                .35F,
                .1F
            )
        )
    }
}

@Composable
private fun Actions(removeRecord: () -> Unit) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(MaterialTheme.typography.bodyLarge.fontSize.dp)
                .clickable { removeRecord() },
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
}

private val TextUnit.dp get() = this.value.dp

@Preview
@Composable
private fun PreviewGlucoseLevelsTable() {
    ReportTable(state = GlucoseLevels(
        List(5) {
            GlucoseLevel(
                type = GlucoseLevel.MeasureType.BEFORE_MEAL,
                value = GlucoseLevel.Value(1.2F),
                date = DateTime(),
                id = it
            )
        }
    )) { _ -> }
}

@Preview
@Composable
private fun PreviewFoodIntakesTable() {
    ReportTable(
        state = FoodIntakes(
            List(5) {
                FoodIntake(
                    id = it,
                    breadUnit = BreadUnit(1),
                    insulin = ShortInsulin(1.2F),
                    date = DateTime()
                )
            }
        ),
    ) { _ ->
    }
}

@Preview
@Composable
private fun PreviewLongInsulinTable() {
    ReportTable(
        state = LongInsulin(
            List(5) {
                DomainLongInsulin(
                    id = it,
                    value = 1F,
                    datetime = DateTime()
                )
            }
        ),
    ) { _ ->
    }
}
