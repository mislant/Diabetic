package com.diabetic.ui.screen.statistic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Table(
    headers: List<String>,
    elements: List<List<String>>,
    modifier: Modifier = Modifier,
    weights: FloatArray = FloatArray(headers.count()) { .1F }
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row {
                headers.forEachIndexed { i, header ->
                    TextCell(
                        text = header,
                        weight = weights[i],
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )
        }
        itemsIndexed(elements) { index, row ->
            Row {
                row.forEachIndexed { i, item ->
                    TextCell(
                        text = item,
                        weight = weights[i],
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun RowScope.TextCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier
            .weight(weight),
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight,
        style = style
    )
}

@Preview
@Composable
private fun TablePreview() {
    val headers = listOf("one", "two", "three")
    val data = List(5) {
        List(3) {
            "Test"
        }
    }
    Table(
        headers = headers,
        elements = data,
        modifier = Modifier
            .background(Color.White)
    )
}