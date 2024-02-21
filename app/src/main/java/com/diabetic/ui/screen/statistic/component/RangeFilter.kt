package com.diabetic.ui.screen.statistic.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.theme.DiabeticMaterialTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DateRangeFilter(
    initial: LongRange?,
    filter: (Long?, Long?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Title()
        Divider()
        DateRangeFields(initial, filter)
    }
}

@Composable
private fun Title() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Фильтрация по дате",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeFields(initial: LongRange?, filter: (Long?, Long?) -> Unit) {
    var pickerShown by remember { mutableStateOf(false) }

    val initialFrom = initial?.first?.localDateTime?.asDate() ?: ""
    val initialTo = initial?.last?.localDateTime?.asDate() ?: ""

    var from by remember { mutableStateOf(initialFrom) }.apply {
        value = initialFrom
    }
    var to by remember { mutableStateOf(initialTo) }.apply {
        value = initialTo
    }

    Log.i("Filter::DateRangeFields::initial", initial.toString())
    Log.i("Filter::DateRangeFields::FromTo", "$from | $to")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val showPicker = { pickerShown = true }
        DateInput("С", from, onClick = showPicker)
        DateInput("По", to, onClick = showPicker)
    }

    if (pickerShown) {
        val pickerState = rememberDateRangePickerState(
            initial?.start,
            initial?.last,
            initialDisplayMode = DisplayMode.Picker
        )

        pickerState.setSelection(
            initial?.start,
            initial?.last
        )

        DatePickerDialog(
            onDismissRequest = { pickerShown = false },
            confirmButton = {
                Button(onClick = {
                    pickerShown = false
                    filter(
                        null,
                        null
                    )
                }) {
                    Text(text = "Сбросить")
                }
                Button(onClick = {
                    pickerShown = false
                    filter(
                        pickerState.selectedStartDateMillis,
                        pickerState.selectedEndDateMillis
                    )
                }) {
                    Text(text = "Выбрать")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            tonalElevation = 0.dp
        ) {
            DateRangePicker(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxHeight(.85F),
                state = pickerState,
                title = null,
                headline = null,
                showModeToggle = false
            )
        }
    }
}

private val Long.localDateTime: LocalDateTime
    get() {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

private fun LocalDateTime.asDate(): String =
    this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

@Composable
private fun DateInput(
    fieldName: String,
    value: String,
    onClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        value = value,
        readOnly = true,
        enabled = false,
        label = { Text(text = fieldName) },
        placeholder = { Text(text = fieldName) },
        onValueChange = {},
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview
@Composable
private fun PreviewDateRangeFilter() {
    DiabeticMaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DateRangeFilter(initial = null, filter = { _, _ -> })
        }
    }
}