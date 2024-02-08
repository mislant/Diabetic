package com.diabetic.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.screen.component.DiabeticLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    val ctx = LocalContext.current

    DiabeticLayout { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .height(60.dp),
                onClick = {
                    Intent(ctx, AddFoodIntakeActivity::class.java).also {
                        ctx.startActivity(it)
                    }
                }
            ) {
                Text(text = "Добавить прием пищи")
            }
            Spacer(modifier = Modifier.size(20.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .height(60.dp),
                onClick = {
                    Intent(ctx, AddGlucoseAfterFoodIntake::class.java)
                        .also { ctx.startActivity(it) }
                }
            ) {
                Text(
                    text = "Добавить уровень глюкозы после приема пищи",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
private fun ContentPreview() {
    Content()
}
