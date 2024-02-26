package com.diabetic.ui.screen.statistic.component

import androidx.collection.FloatList
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    actions: (@Composable (row: Int) -> Unit)? = null,
    weights: List<Float> = List(headers.count()) { .1F }
) {
    val preparedHeaders =
        if (actions != null) headers.toMutableList().apply {
            add("")
            toList()
        }
        else headers


    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row {
                preparedHeaders.forEachIndexed { i, header ->
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
        itemsIndexed(elements) { rowNum, row ->
            Row {
                row.forEachIndexed { i, item ->
                    TextCell(
                        text = item,
                        weight = weights[i],
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (actions != null) {
                    BoxCell(weight = weights.last()) {
                        actions(rowNum)
                    }
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
            .align(Alignment.CenterVertically)
            .weight(weight),
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight,
        style = style
    )
}

@Composable
private fun RowScope.BoxCell(
    weight: Float,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxWidth(),
    ) {
        content()
    }
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