package com.diabetic.ui.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.ui.screen.main.component.GlucoseLevelInput

class MainActivity : ComponentActivity() {

    private val glucoseLevelModel by viewModels<GlucoseLevelViewModel> { GlucoseLevelViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    GlucoseLevelInput(
                        type = GlucoseLevel.MeasureType.BEFORE_MEAL
                    ) { glucoseLevelModel.addGlucoseLevelBeforeMeal(it) }
                }
            }
        }
    }
}
