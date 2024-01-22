package com.diabetic.ui.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import com.diabetic.ui.screen.main.component.GlucoseLevelInput
import com.diabetic.ui.screen.main.component.GlucoseLevelTable

class MainActivity : ComponentActivity() {
    private val glucoseLevelModel by viewModels<GlucoseLevelViewModel> { GlucoseLevelViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content(glucoseLevelModel)
        }
    }
}

@Composable
fun Content(glucoseLevelModel: GlucoseLevelViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GlucoseLevelInput(
                    modifier = Modifier
                        .width(180.dp),
                    type = GlucoseLevel.MeasureType.BEFORE_MEAL,
                    handleAddingLevel = { glucoseLevelModel.addGlucoseLevelBeforeMeal(it) }
                )
                GlucoseLevelInput(
                    modifier = Modifier
                        .width(180.dp),
                    type = GlucoseLevel.MeasureType.AFTER_MEAL,
                    handleAddingLevel = { glucoseLevelModel.addGlucoseLevelAfterMeal(it) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val levels by glucoseLevelModel.glucoseLevels.collectAsState()
            GlucoseLevelTable(levels)
        }
    }
}

@Composable
@Preview
fun ContentPreview() {
    val repository = StubGlucoseLevelRepository()
    val model = GlucoseLevelViewModel(
        repository,
        AddGlucoseLevel.Handler(repository)
    )

    Content(glucoseLevelModel = model)
}