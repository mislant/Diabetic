package com.diabetic.ui.screen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diabetic.application.command.AddCarbohydrate
import com.diabetic.application.command.BeginFoodIntake
import com.diabetic.application.command.CalculateInsulinBeforeFoodIntake
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.Insulin
import com.diabetic.infrastructure.persistent.stub.StubCarbohydrateStorage
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import com.diabetic.ui.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddFoodIntakeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val foodIntakeViewModel = viewModel<FoodIntakeViewModel>(
                factory = FoodIntakeViewModel.Factory
            )

            Content(foodIntakeViewModel)
        }
    }
}

@Composable
private fun Content(
    foodIntakeViewModel: FoodIntakeViewModel
) {
    DiabeticLayout { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val state = foodIntakeViewModel.state.collectAsState()

            FoodIntakeInput(
                state.value,
                foodIntakeViewModel::changeGlucoseLevel,
                foodIntakeViewModel::changeBreadUnit,
                foodIntakeViewModel::changeCarbohydrate,
                foodIntakeViewModel::addFoodIntake,
                foodIntakeViewModel::flushError
            )
        }
    }
}

@Composable
private fun FoodIntakeInput(
    state: FoodIntakeState,
    onGlucoseLevelChange: (String) -> Unit,
    onBreadUnitChange: (String) -> Unit,
    onCarbohydrate: (String) -> Unit,
    onSave: () -> Unit,
    onErrorShown: () -> Unit
) {
    val space = @Composable {
        Spacer(modifier = Modifier.size(20.dp))
    }

    if (state.error.isNotEmpty()) {
        Toast
            .makeText(LocalContext.current, state.error, Toast.LENGTH_LONG)
            .show()
        onErrorShown()
    }

    if (state.isSaved) {
        Text(
            text = "Количество инсулина, которое вам необходимо принять",
            fontSize = 4.em,
            textAlign = TextAlign.Center
        )
        Text(
            text = state.calculatedInsulin!!.value.toString(),
            fontSize = 10.em,
            fontWeight = FontWeight.Bold
        )
        return
    }

    OutlinedTextField(
        value = state.glucoseLevel,
        singleLine = true,
        onValueChange = onGlucoseLevelChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Bloodtype,
                contentDescription = null
            )
        },
        placeholder = { Text(text = "Уровень глюкозы") },
        label = { Text(text = "Уровень глюкозы") },
    )
    space()
    OutlinedTextField(
        value = state.breadUnit,
        singleLine = true,
        onValueChange = onBreadUnitChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = null
            )
        },
        placeholder = { Text(text = "Хлебные единицы") },
        label = { Text(text = "Хлебные единицы") }
    )
    space()
    OutlinedTextField(
        value = state.carbohydrate,
        singleLine = true,
        onValueChange = onCarbohydrate,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Co2,
                contentDescription = null
            )
        },
        placeholder = {
            Text(text = "Углеводородный индекс")
        },
        label = {
            Text(text = "Углеводородный индекс")
        }
    )
    space()
    FilledTonalButton(onClick = onSave) {
        Text(text = "Добавить")
    }
}

@Preview
@Composable
private fun ContentPreview() {
    val foodIntakeRepository = StubFoodIntakeRepository()
    val carbohydrateStorage = StubCarbohydrateStorage()

    Content(
        FoodIntakeViewModel(
            StubCarbohydrateStorage(),
            BeginFoodIntake.Handler(foodIntakeRepository),
            AddCarbohydrate.Handler(carbohydrateStorage),
            CalculateInsulinBeforeFoodIntake.Handler(
                foodIntakeRepository,
                carbohydrateStorage
            ),
            FoodIntakeState(
                isSaved = true,
                calculatedInsulin = Insulin(2.2F)
            )
        )
    )
}

private class FoodIntakeViewModel(
    private val carbohydrateStorage: CarbohydrateStorage,
    private val beginFoodIntake: BeginFoodIntake.Handler,
    private val addCarbohydrate: AddCarbohydrate.Handler,
    private val calculateInsulinBeforeFoodIntake: CalculateInsulinBeforeFoodIntake.Handler,
    initialState: FoodIntakeState = FoodIntakeState()
) : ViewModel() {
    private var _state = MutableStateFlow(initialState)
    val state: StateFlow<FoodIntakeState>
        get() = _state.asStateFlow();

    init {
        val carbohydrate = carbohydrateStorage.get()

        if (carbohydrate != null) {
            _state.value = _state.value.copy(
                carbohydrate = carbohydrate.value.toString()
            )
        }
    }

    fun changeGlucoseLevel(value: String) {
        if (value.isFloatGraterThenZero() || value.isEmpty()) {
            _state.value = _state.value.copy(
                glucoseLevel = value
            )
        }
    }

    fun changeBreadUnit(value: String) {
        if (value.isIntGraterThenZero() || value.isEmpty()) {
            _state.value = _state.value.copy(
                breadUnit = value
            )
        }
    }

    fun changeCarbohydrate(value: String) {
        if (value.isFloatGraterThenZero() || value.isEmpty()) {
            _state.value = _state.value.copy(
                carbohydrate = value
            )
        }
    }

    private fun String.isFloatGraterThenZero(): Boolean = this.run {
        toFloatOrNull() !== null && toFloat() > 0F
    }

    private fun String.isIntGraterThenZero(): Boolean = this.run {
        toIntOrNull() !== null && toInt() > 0F
    }

    fun addFoodIntake() {
        val state = _state.value

        if (
            state.breadUnit.isEmpty() ||
            state.glucoseLevel.isEmpty() ||
            state.carbohydrate.isEmpty()
        ) {
            addError("Заполните все поля")
        }

        try {
            saveFoodIntakeForm()
        } catch (e: Throwable) {
            Log.e(null, e.toString())
            this.addError("Что-то пошло не так :(")
        }
    }

    private fun saveFoodIntakeForm() {
        val state = _state.value

        val savedFoodIntake = beginFoodIntake.handle(
            BeginFoodIntake.Command(
                state.glucoseLevel.toFloat(),
                state.breadUnit.toInt(),
            )
        )

        addCarbohydrate.handle(
            AddCarbohydrate.Command(
                state.carbohydrate.toFloat()
            )
        )

        val calculatedInsulin = calculateInsulinBeforeFoodIntake.handle(
            CalculateInsulinBeforeFoodIntake.Command(
                savedFoodIntake
            )
        )

        _state.value = _state.value.copy(
            isSaved = true,
            calculatedInsulin = calculatedInsulin
        )
    }

    fun flushError() {
        this.addError("")
    }


    private fun addError(message: String) {
        _state.value = _state.value.copy(
            error = message
        )
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FoodIntakeViewModel(
                    ServiceLocator.carbohydrateStorage(),
                    ServiceLocator.beginFoodIntakeHandler(),
                    ServiceLocator.addCarbohydrateHandler(),
                    ServiceLocator.calculateInsulinBeforeFoodIntakeHandler()
                ) as T
            }
        }
    }
}

private data class FoodIntakeState(
    val glucoseLevel: String = "",
    val breadUnit: String = "",
    val carbohydrate: String = "",
    val error: String = "",
    val isSaved: Boolean = false,
    val calculatedInsulin: Insulin? = null
) {
}