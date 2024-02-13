package com.diabetic.ui.screen.statistic.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeFilter(filter: (Long?, Long?) -> Unit) {
    val range = IntRange(1900, 2024)

    val state = rememberDateRangePickerState(
        null,
        null,
        null,
        range,
        DisplayMode.Input
    )

    DateRangePicker(
        state = state,
        title = null,
        headline = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Фильтрация по дате"
            )
        }
    )

    filter(
        state.selectedStartDateMillis,
        state.selectedEndDateMillis
    )
}