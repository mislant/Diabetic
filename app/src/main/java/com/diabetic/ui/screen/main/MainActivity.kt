package com.diabetic.ui.screen.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.main.component.GlucoseLevelInput

class MainActivity : ComponentActivity() {

    companion object {
        const val LOG_TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    Surface(Modifier.fillMaxSize()) {
        GlucoseLevelInput(
            type = GlucoseLevel.MeasureType.BEFORE_MEAL
        ) {
            if (it != null) {
                val addGlucoseLevel = AddGlucoseLevel.Command(
                    it,
                    GlucoseLevel.MeasureType.BEFORE_MEAL
                );

                try {
                    ServiceLocator
                        .get()
                        .addGlucoseLevelHandler()
                        .handle(addGlucoseLevel);
                } catch (ex: Exception) {
                    Log.e(MainActivity.LOG_TAG, ex.message!!)
                }
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    MaterialTheme {
        Main()
    }
}