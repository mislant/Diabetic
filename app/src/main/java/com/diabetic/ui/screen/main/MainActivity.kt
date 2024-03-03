package com.diabetic.ui.screen.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.diabetic.ui.screen.AddFoodIntakeActivity
import com.diabetic.ui.screen.AddGlucoseActivity
import com.diabetic.ui.screen.AddLongInsulinActivity
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.screen.main.component.EducationCards
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
        DiabeticLayout(
            floatingActionButton = {
                ActionButtons()
            }
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                EducationCards(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

@Composable
private fun ActionButtons() {
    var isClicked by remember {
        mutableStateOf(false)
    }

    Surface(
        color = Color.Transparent,
        tonalElevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (isClicked) {
                ActionButton(
                    title = "Добавить прием пищи",
                    activity = AddFoodIntakeActivity::class
                )
                ActionButton(
                    title = "Добавить уровень глюкозы",
                    activity = AddGlucoseActivity::class
                )
                ActionButton(
                    title = "Добавить длинный инсулин",
                    activity = AddLongInsulinActivity::class
                )
            }

            TextButton(
                onClick = {
                    isClicked = !isClicked
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
            ) {
                Icon(
                    imageVector =
                    if (isClicked) Icons.Default.Close
                    else Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(
                        MaterialTheme.typography.displaySmall.size
                    )
                )
            }
        }
    }

}

private val TextStyle.size: Dp get() = this.fontSize.value.dp

@Composable
private fun ActionButton(
    title: String,
    activity: KClass<out ComponentActivity>
) {
    val ctx = LocalContext.current

    Button(
        onClick = {
            Intent(ctx, activity.java).also {
                ctx.startActivity(it)
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }

    Spacer(modifier = Modifier.size(5.dp))
}

@Composable
@Preview
private fun ContentPreview() {
    Content()
}
