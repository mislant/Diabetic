package com.diabetic.ui.screen.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diabetic.domain.model.GlucoseLevel

@Composable
fun GlucoseLevelInput(
    type: GlucoseLevel.MeasureType,
    modifier: Modifier = Modifier,
    handleAddingLevel: (Float) -> Unit
) {
    val supportingText = when (type) {
        GlucoseLevel.MeasureType.BEFORE_MEAL -> "Перед едой"
        GlucoseLevel.MeasureType.AFTER_MEAL -> "После еды"
    }

    val model = viewModel<Model>()
    val level = model.level.observeAsState("")


    Row(modifier = modifier) {
        OutlinedTextField(
            value = level.value,
            label = { Text(text = "Уровень глюкозы") },
            supportingText = { Text(text = supportingText) },
            placeholder = { Text(text = "0.0") },
            onValueChange = { model.changeLevel(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            trailingIcon = {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        model.handleGlucoseAdding(handleAddingLevel)
                    }
                )
            }
        )
    }
}

internal class Model() : ViewModel() {
    val level = MutableLiveData("0.0")

    fun changeLevel(new: String) {
        if (new.toFloatOrNull() != null || new.isEmpty()) {
            level.postValue(new)
        }
    }

    fun handleGlucoseAdding(handleLevelAdding: (Float) -> Unit) {
        val toSave = level.value?.toFloatOrNull() ?: return

        try {
            handleLevelAdding(toSave)
        } catch (_: Exception) {
        }

        level.postValue("0.0")
    }
}
