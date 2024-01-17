package com.diabetic.ui.screen.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.domain.model.GlucoseLevel

@Composable
fun GlucoseLevelInput(
    type: GlucoseLevel.MeasureType,
    modifier: Modifier = Modifier,
    handleAddingLevel: (Float?) -> Unit
) {
    val supporting = when (type) {
        GlucoseLevel.MeasureType.BEFORE_MEAL -> "До еды"
        GlucoseLevel.MeasureType.AFTER_MEAL -> "После еды"
    };
    val glucose = remember { mutableStateOf("") }

    Row(modifier = modifier) {
        OutlinedTextField(
            value = glucose.value,
            label = { Text("Уровень глюкозы") },
            supportingText = { Text(supporting) },
            placeholder = { Text(text = "0.0") },
            onValueChange = {
                if (it.toFloatOrNull() != null || it.isEmpty()) {
                    glucose.value = it;
                }
            },
            trailingIcon = {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        handleAddingLevel(glucose.value.toFloatOrNull())
                        glucose.value = "";
                    }
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    GlucoseLevelInput(
        type = GlucoseLevel.MeasureType.BEFORE_MEAL,
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
            .width(500.dp)
    ) {}
}