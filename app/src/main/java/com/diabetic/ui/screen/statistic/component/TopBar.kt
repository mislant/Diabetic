package com.diabetic.ui.screen.statistic.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diabetic.ui.screen.statistic.viewmodel.ReportState

@Composable
fun TopBar(current: ReportState.Report, onTabClick: (ReportState.Report) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(0.95F)
    ) {
        Text(
            text = "Отчет",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        TabRow(
            containerColor = MaterialTheme.colorScheme.background,
            selectedTabIndex = current.ordinal
        ) {
            ReportState.Report.entries.forEach { listed ->
                Tab(
                    selected = current.ordinal == listed.ordinal,
                    onClick = { onTabClick(listed) }
                ) {
                    Text(
                        text = listed.title,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
        }
    }
}