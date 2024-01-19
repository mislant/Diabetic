package com.diabetic.ui.screen.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diabetic.domain.model.GlucoseLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    val level by model.level.collectAsState()

    OutlinedTextField(
        modifier = modifier,
        value = level,
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

@Preview
@Composable
fun GlucoseLevelInputPreview() {
    GlucoseLevelInput(
        type = GlucoseLevel.MeasureType.BEFORE_MEAL,
        handleAddingLevel = {}
    )
}

internal class Model() : ViewModel() {
    private val _level = MutableStateFlow("")
    val level = _level.asStateFlow()

    fun changeLevel(new: String) {
        if (new.toFloatOrNull() != null || new.isEmpty()) {
            _level.value = new
        }
    }

    fun handleGlucoseAdding(handleLevelAdding: (Float) -> Unit) {
        val toSave = _level.value.toFloatOrNull() ?: return

        try {
            handleLevelAdding(toSave)
        } catch (_: Exception) {
        }

        _level.value = ""
    }
}
