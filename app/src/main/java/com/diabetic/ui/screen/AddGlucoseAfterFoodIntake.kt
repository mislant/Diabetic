package com.diabetic.ui.screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddGlucoseAfterFoodIntake : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val model = viewModel<AddGlucoseAfterFoodIntakeViewModel>(
                factory = AddGlucoseAfterFoodIntakeViewModel.Factory
            )

            Content(model)
        }
    }
}

@Composable
private fun Content(model: AddGlucoseAfterFoodIntakeViewModel) {
    val state = model.state.collectAsState()
    val ctx = LocalContext.current

    DiabeticMaterialTheme {
        if (state.value.error.isNotEmpty()) {
            Toast
                .makeText(
                    LocalContext.current,
                    state.value.error,
                    Toast.LENGTH_LONG
                )
                .show()
            model.flushError()
        }

        DiabeticLayout { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.value.glucoseLevel,
                    singleLine = true,
                    onValueChange = model::changeGlucoseLevel,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Bloodtype,
                            contentDescription = null
                        )
                    },
                    placeholder = { Text(text = "Уровень глюкозы") },
                    label = { Text(text = "Уровень глюкозы") },
                )
                Spacer(modifier = Modifier.size(20.dp))
                FilledTonalButton(onClick = {
                    model.addLevel().onSuccess {
                        Intent(ctx, MainActivity::class.java).also {
                            ctx.startActivity(it)
                        }
                    }
                }) {
                    Text(text = "Добавить")
                }
            }
        }
    }
}

private fun Boolean.onSuccess(block: () -> Unit) {
    if (this) {
        block()
    }
}

private data class AddGlucoseAfterFoodIntakeState(
    val glucoseLevel: String = "",
    val error: String = ""
)

private class AddGlucoseAfterFoodIntakeViewModel(
    private val handler: AddGlucoseLevel.Handler,
    initial: AddGlucoseAfterFoodIntakeState = AddGlucoseAfterFoodIntakeState("")
) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state = _state.asStateFlow()

    fun changeGlucoseLevel(level: String) {
        if (level.isFloatGraterThenZero() || level.isEmpty()) {
            _state.value = _state.value.copy(
                glucoseLevel = level
            )
        }
    }

    private fun String.isFloatGraterThenZero(): Boolean = this.run {
        toFloatOrNull() !== null && toFloat() > 0F
    }

    fun addLevel(): Boolean {
        val level = _state.value.glucoseLevel

        if (level.isEmpty()) {
            error("Заполните все поля!")
            return false
        }

        return try {
            saveLevel()
            true
        } catch (e: Throwable) {
            error("Упс, что-то пошло не так!:(")
            false
        }
    }

    private fun saveLevel() {
        val level = _state.value.glucoseLevel.toFloat()

        handler.handle(
            AddGlucoseLevel.Command(
                level,
                GlucoseLevel.MeasureType.AFTER_MEAL
            )
        )
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
                return AddGlucoseAfterFoodIntakeViewModel(
                    ServiceLocator.addGlucoseLevelHandler()
                ) as T
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        AddGlucoseAfterFoodIntakeViewModel(
            AddGlucoseLevel.Handler(
                StubGlucoseLevelRepository()
            )
        )
    )
}
