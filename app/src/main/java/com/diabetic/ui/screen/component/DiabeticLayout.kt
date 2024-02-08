package com.diabetic.ui.screen.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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