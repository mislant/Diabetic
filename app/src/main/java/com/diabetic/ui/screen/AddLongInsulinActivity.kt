package com.diabetic.ui.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diabetic.application.command.AddLongInsulin.Command
import com.diabetic.application.command.AddLongInsulin.Handler
import com.diabetic.domain.model.time.datetime
import com.diabetic.infrastructure.persistent.stub.StubLongInsulinRepository
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.component.DateTimePicker
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class AddLongInsulinActivity : ComponentActivity() {
    private val model: AddLongInsulinViewModel by viewModels {
        AddLongInsulinViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = model.state.collectAsState()

            Content(
                state = state.value,
                flushError = model::flushError,
                changeInsulin = model::changeInsulin,
                changeDate = model::changeDate,
                changeTime = model::changeTime,
                addInsulin = model::addLongInsulin
            )
        }
    }
}

@Composable
private fun Content(
    state: AddLongInsulinViewModelState,
    flushError: () -> Unit,
    changeInsulin: (String) -> Unit,
    changeDate: (Long?) -> Unit,
    changeTime: (Int, Int) -> Unit,
    addInsulin: () -> Boolean
) {
    DiabeticMaterialTheme {
        if (state.error.isNotEmpty()) {
            Toast
                .makeText(
                    LocalContext.current,
                    state.error,
                    Toast.LENGTH_LONG
                )
                .show()
            flushError()
        }

        DiabeticLayout { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddLongInsulinForm(
                    state = state,
                    onInsulinChange = changeInsulin,
                    onDateChange = changeDate,
                    onTimeChange = changeTime,
                    onSave = addInsulin
                )
            }
        }
    }
}

@Composable
private fun AddLongInsulinForm(
    state: AddLongInsulinViewModelState,
    onInsulinChange: (String) -> Unit,
    onDateChange: (Long?) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onSave: () -> Boolean
) {
    OutlinedTextField(
        value = state.insulin,
        singleLine = true,
        onValueChange = onInsulinChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Vaccines,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
        ),
        placeholder = { Text(text = "Длинный инсулин") },
        label = { Text(text = "Длинный инсулин") },
    )
    Spacer(modifier = Modifier.size(20.dp))

    DateTimePicker(onDateChange, onTimeChange)
    Spacer(modifier = Modifier.size(20.dp))

    val ctx = LocalContext.current
    FilledTonalButton(onClick = {
        onSave().onSuccess {
            Intent(ctx, MainActivity::class.java).also {
                ctx.startActivity(it)
            }
        }
    }) {
        Text(text = "Добавить")
    }
}

private fun Boolean.onSuccess(block: () -> Unit) {
    if (this) {
        block()
    }
}

private data class AddLongInsulinViewModelState(
    val insulin: String = "",
    val error: String = "",
    val date: Long = LocalDate
        .now()
        .atTime(0, 0)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli(),
    val hour: Int = LocalDateTime.now().atZone(ZoneId.systemDefault()).hour,
    val minute: Int = LocalDateTime.now().atZone(ZoneId.systemDefault()).minute
) {
    val datetime: Long
        get() {
            return date +
                    hour * 3_600_000 +
                    minute * 60_000
        }
}

private class AddLongInsulinViewModel(
    private val handler: Handler,
    initial: AddLongInsulinViewModelState = AddLongInsulinViewModelState()
) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state = _state.asStateFlow()

    fun changeInsulin(value: String) {
        if (value.isFloatGraterThenZero() || value.isEmpty()) {
            _state.value = _state.value.copy(
                insulin = value
            )
        }
    }

    private fun String.isFloatGraterThenZero(): Boolean = this.run {
        toFloatOrNull() !== null && toFloat() > 0F
    }

    fun changeDate(date: Long?) {
        if (date == null) {
            return
        }

        _state.value = _state.value.copy(
            date = date
        )
    }

    fun changeTime(hour: Int, minute: Int) {
        _state.value = _state.value.copy(
            hour = hour,
            minute = minute
        )
    }

    fun addLongInsulin(): Boolean {
        val value = _state.value.insulin

        if (value.isEmpty()) {
            error("Укажите дозу длинного инсулина")
            return false
        }

        return try {
            handler.handle(
                Command(
                    value.toFloat(),
                    _state.value.datetime.datetime
                )
            )
            true
        } catch (e: Throwable) {
            error("Упс, что-то пошло не так!:(")
            Log.e(null, e.toString())
            false
        }
    }

    fun flushError() {
        error("")
    }

    private fun error(msg: String) {
        _state.value = _state.value.copy(
            error = msg
        )
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddLongInsulinViewModel(
                    handler = ServiceLocator.addLongInsulin()
                ) as T
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    val model = AddLongInsulinViewModel(
        Handler(StubLongInsulinRepository())
    )
    val state = model.state.collectAsState()

    Content(
        state = state.value,
        flushError = model::flushError,
        changeInsulin = model::changeInsulin,
        changeDate = model::changeDate,
        changeTime = model::changeTime,
        addInsulin = model::addLongInsulin
    )
}