package com.diabetic.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.readableDate
import com.diabetic.domain.model.time.readableTime
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    onDateChange: (Long?) -> Unit,
    onTimeChange: (hour: Int, minute: Int) -> Unit
) {
    val now = LocalDateTime.now()

    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var date by remember {
        mutableStateOf(now.readableDate)
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var time by remember {
        mutableStateOf(now.readableTime)
    }

    Row(
        Modifier.width(280.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(135.dp)
                .clickable {
                    showDatePicker = true
                },
            value = date,
            onValueChange = {},
            label = { Text(text = "Дата") },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .width(135.dp)
                .clickable {
                    showTimePicker = true
                },
            value = time,
            onValueChange = {},
            label = { Text(text = "Время") },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }

    val datePickerState = rememberDatePickerState(
        null,
        null,
        IntRange(1000, 3000),
        DisplayMode.Picker
    )

    if (showDatePicker) {
        val confirm = {
            date = datePickerState
                .selectedDateMillis
                ?.datetime
                ?.readableDate ?: date
            onDateChange(datePickerState.selectedDateMillis)
            showDatePicker = false
        }
        DatePickerDialog(
            onDismissRequest = confirm,
            confirmButton = {
                TextButton(onClick = confirm) {
                    Text(text = "Выбрать")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            tonalElevation = 0.dp
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false
            )
        }
    }

    val timePickerState = rememberTimePickerState(
        now.hour,
        now.minute
    )
    if (showTimePicker) {
        AlertDialog(
            modifier = Modifier
                .clip(RoundedCornerShape(35.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp),
            onDismissRequest = {
                time = "%s:%s".format(
                    if (timePickerState.hour < 10) "0${timePickerState.hour}"
                    else timePickerState.hour.toString(),
                    if (timePickerState.minute < 10) "0${timePickerState.minute}"
                    else timePickerState.minute.toString(),
                )
                onTimeChange(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            }) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    }
}