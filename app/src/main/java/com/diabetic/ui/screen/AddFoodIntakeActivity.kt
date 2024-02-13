package com.diabetic.ui.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diabetic.application.command.AddCarbohydrate
import com.diabetic.application.command.BeginFoodIntake
import com.diabetic.domain.model.CarbohydrateStorage
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.infrastructure.persistent.stub.StubCarbohydrateStorage
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddFoodIntakeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val model = viewModel<FoodIntakeViewModel>(
                factory = FoodIntakeViewModel.Factory
            )

            Content(model)
        }
    }
}

@Composable
private fun Content(
    model: FoodIntakeViewModel
) {
    DiabeticMaterialTheme {
        DiabeticLayout { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val state = model.state.collectAsState()

                FoodIntakeInput(
                    state.value,
                    model::changeBreadUnit,
                    model::changeCarbohydrate,
                    model::addFoodIntake,
                    model::flushError
                )
            }
        }
    }
}

@Composable
private fun FoodIntakeInput(
    state: FoodIntakeState,
    onBreadUnitChange: (String) -> Unit,
    onCarbohydrate: (String) -> Unit,
    onSave: () -> Unit,
    onErrorShown: () -> Unit
) {
    if (state.error.isNotEmpty()) {
        Toast
            .makeText(LocalContext.current, state.error, Toast.LENGTH_LONG)
            .show()
        onErrorShown()
    }

    if (state.showInsulin) InsulinView(state)

    AddFoodIntakeForm(
        state,
        onBreadUnitChange,
        onCarbohydrate,
        onSave
    )
}

@Composable
private fun AddFoodIntakeForm(
    state: FoodIntakeState,
    onBreadUnitChange: (String) -> Unit,
    onCarbohydrate: (String) -> Unit,
    onSave: () -> Unit,
) {
    val space = @Composable {
        Spacer(modifier = Modifier.size(20.dp))
    }

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
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        placeholder = { Text(text = "Хлебные единицы") },
        label = { Text(text = "Хлебные единицы") }
    )
    space()
    OutlinedTextField(
        value = state.carbohydrate,
        singleLine = true,
        onValueChange = onCarbohydrate,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Co2,
                contentDescription = null
            )
        },
        placeholder = {
            Text(text = "Углеводный коэффицент")
        },
        label = {
            Text(text = "Углеводный коэффицент")
        }
    )
    space()
    FilledTonalButton(onClick = onSave) {
        Text(text = "Добавить")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsulinView(state: FoodIntakeState) {
    val ctx = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            Intent(ctx, MainActivity::class.java).also {
                ctx.startActivity(it)
            }
        }
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(35.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxHeight(0.25F)
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Количество инсулина, которое вам необходимо принять",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = state.calculatedInsulin!!.value.toString(),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class FoodIntakeState(
    val breadUnit: String = "",
    val carbohydrate: String = "",
    val error: String = "",
    val showInsulin: Boolean = false,
    val calculatedInsulin: ShortInsulin? = null
)

private class FoodIntakeViewModel(
    private val carbohydrateStorage: CarbohydrateStorage,
    private val beginFoodIntake: BeginFoodIntake.Handler,
    private val addCarbohydrate: AddCarbohydrate.Handler,
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
            state.carbohydrate.isEmpty()
        ) {
            error("Заполните все поля")
        }

        try {
            saveFoodIntakeForm()
        } catch (e: Throwable) {
            Log.e(null, e.toString())
            this.error("Что-то пошло не так :(")
        }
    }

    private fun saveFoodIntakeForm() {
        val state = _state.value

        addCarbohydrate.handle(
            AddCarbohydrate.Command(
                state.carbohydrate.toFloat()
            )
        )

        val insulin = beginFoodIntake.handle(
            BeginFoodIntake.Command(
                state.breadUnit.toInt(),
            )
        )

        _state.value = _state.value.copy(
            showInsulin = true,
            calculatedInsulin = insulin
        )
    }

    fun flushError() {
        this.error("")
    }

    private fun error(message: String) {
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
                ) as T
            }
        }
    }
}

@Preview
@Composable
private fun ContentFormPreview() {
    val foodIntakeRepository = StubFoodIntakeRepository()
    val carbohydrateStorage = StubCarbohydrateStorage()

    Content(
        FoodIntakeViewModel(
            StubCarbohydrateStorage(),
            BeginFoodIntake.Handler(foodIntakeRepository, carbohydrateStorage),
            AddCarbohydrate.Handler(carbohydrateStorage),
            FoodIntakeState()
        )
    )
}

@Preview
@Composable
private fun ContentPreview() {
    val foodIntakeRepository = StubFoodIntakeRepository()
    val carbohydrateStorage = StubCarbohydrateStorage()

    Content(
        FoodIntakeViewModel(
            StubCarbohydrateStorage(),
            BeginFoodIntake.Handler(foodIntakeRepository, carbohydrateStorage),
            AddCarbohydrate.Handler(carbohydrateStorage),
            FoodIntakeState(
                showInsulin = true,
                calculatedInsulin = ShortInsulin(2.2F)
            )
        )
    )
}