package com.diabetic.ui.screen.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel

@Composable
fun GlucoseLevelTable(levels: List<GlucoseLevel>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Тип замера")
            Text(text = "Значение")
            Text(text = "Время")
        }

        Divider()

        LazyColumn {
            items(levels) { level -> GlucoseLevelItem(level) }
        }
    }
}

@Composable
fun GlucoseLevelItem(level: GlucoseLevel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = when (level.type) {
                    GlucoseLevel.MeasureType.AFTER_MEAL -> "Перед едой"
                    GlucoseLevel.MeasureType.BEFORE_MEAL -> "После еды"
                }
            )
            Text(text = level.value.toString())
            Text(text = level.date.format().readable())
        }
        Divider()
    }
}

@Composable
@Preview
fun GlucoseLevelTablePreview() {
    val levels = mutableListOf<GlucoseLevel>()

    repeat(3) {
        levels.add(
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                DateTime(),
                it
            )
        )
    }

    GlucoseLevelTable(levels = levels)
}