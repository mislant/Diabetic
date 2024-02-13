package com.diabetic.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    DiabeticMaterialTheme {
        DiabeticLayout { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainActionButton(
                    test = "Добавить прием пищи",
                    activity = AddFoodIntakeActivity::class
                )
                Spacer(modifier = Modifier.size(20.dp))
                MainActionButton(
                    test = "Добавить уровень глюкозы",
                    activity = AddGlucoseActivity::class
                )
            }
        }
    }
}

@Composable
private fun MainActionButton(
    test: String,
    activity: KClass<out ComponentActivity>
) {
    val ctx = LocalContext.current

    Button(
        modifier = Modifier
            .fillMaxWidth(0.9F)
            .height(60.dp),
        onClick = {
            Intent(ctx, activity.java).also {
                ctx.startActivity(it)
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = test,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview
private fun ContentPreview() {
    Content()
}
