package com.diabetic.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            Content()
        }
    }
}

@Composable
fun Content() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(0.9F),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Добавить прием пищи")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar() {
    TopAppBar(title = {
        Text(
            text = LocalDateTime
                .now()
                .format(
                    DateTimeFormatter.ofPattern(
                        "dd MMM uuuu"
                    )
                ),
            modifier = Modifier
                .fillMaxWidth(0.95F)
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            textAlign = TextAlign.Center
        )
    })
}

@Composable
fun BottomBar() {
    BottomAppBar() {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxHeight()
                    .width(200.dp),
                Arrangement.SpaceAround
            ) {
                BottomButton(
                    icon = Icons.Default.WaterDrop,
                    text = "Основное",
                )
                BottomButton(
                    icon = Icons.Default.BarChart,
                    text = "Статистика"
                )
            }
        }
    }
}

@Composable
fun BottomButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxHeight()
            .width(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            imageVector = icon,
            contentDescription = null
        )
        Text(text)
    }
}

@Composable
@Preview
fun ContentPreview() {
    Content()
}
