package com.diabetic.ui.screen.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.DiabeticApplication
import java.nio.file.WatchEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DiabeticLayout(
    topBarContent: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { TopBar(topBarContent) },
        bottomBar = { BottomBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    content: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = if (content == null) {
            { DefaultTopBarContent() }
        } else {
            content
        }
    )
}

@Composable
private fun DefaultTopBarContent() {
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
}

@Preview
@Composable
private fun DiabeticLayoutPreview() {
    DiabeticLayout {
        Text(
            text = "Test",
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
private fun DiabeticLayoutWithCustomAppBarPreview() {
    DiabeticLayout(
        topBarContent = {
            Text(text = "Test")
        }
    ) {
        Text(
            text = "Test",
            modifier = Modifier.padding(it)
        )
    }
}