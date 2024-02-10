package com.diabetic.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diabetic.application.command.PrepareGlucoseLevelsReport
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.OutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class StatisticActivity : ComponentActivity() {
    private val model: StatisticsViewModel by viewModels(
        factoryProducer = { StatisticsViewModel.Factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val createReportContract =
            object : ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
                override fun createIntent(context: Context, input: String): Intent {
                    return super.createIntent(context, input)
                        .addCategory(Intent.CATEGORY_OPENABLE)
                }
            }

        val launcher = registerForActivityResult(createReportContract) {
            if (it === null) {
                return@registerForActivityResult
            }

            val stream = contentResolver.openOutputStream(it)
            if (stream === null) {
                return@registerForActivityResult
            }

            model.generateReport(stream)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Content(model) {
                launcher.launch(model.generateFileName())
            }
        }
    }
}

@Composable
private fun Content(model: StatisticsViewModel, saveFile: () -> Unit) {
    val state = model.state.collectAsState()

    DiabeticMaterialTheme {
        DiabeticLayout(topBarContent = { TopBar() }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Row(horizontalArrangement = Arrangement.End) {
                    DateFilterInput(model::filterGlucoseLevels)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.88F)
                        .padding(horizontal = 7.dp)
                ) {
                    GlucoseLevelsTable(state.value.glucoseLevels)
                }
                Divider(modifier = Modifier.padding())
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = saveFile) {
                        Icon(
                            imageVector = Icons.Default.Summarize,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Excel"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Text(
        text = "Отчет",
        modifier = Modifier
            .fillMaxWidth(0.95F)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilterInput(filter: (Long?, Long?) -> Unit) {
    val range = IntRange(1900, 2024)

    val state = rememberDateRangePickerState(
        null,
        null,
        null,
        range,
        DisplayMode.Input
    )

    DateRangePicker(
        state = state,
        title = null,
        headline = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Фильтрация по дате"
            )
        }
    )

    Log.e(null, state.selectedStartDateMillis.toString())
    Log.e(null, state.selectedEndDateMillis.toString())

    filter(
        state.selectedStartDateMillis,
        state.selectedEndDateMillis
    )
}

@Composable
private fun GlucoseLevelsTable(glucoseLevels: List<GlucoseLevel> = listOf()) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        val text = @Composable { text: String, weight: Float ->
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(weight)
            )
        }

        text("№", 0.1F)
        text("Уровень", 0.2F)
        text("Дата", 0.35F)
        text("До\\После еды", 0.25F)
    }
    Divider(modifier = Modifier.padding(vertical = 2.dp))
    LazyColumn {
        items(glucoseLevels) { level ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                val text = @Composable { text: String, weight: Float ->
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(weight)
                    )
                }

                text(level.id.toString(), 0.1F)
                text(level.value.level.toString(), 0.2F)
                text(level.date.format().readable(), 0.35F)
                text(
                    when (level.type) {
                        GlucoseLevel.MeasureType.BEFORE_MEAL -> "До"
                        GlucoseLevel.MeasureType.AFTER_MEAL -> "После"
                    },
                    0.25F
                )
            }
            Divider(modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}

private data class StatisticsViewModelState(
    val glucoseLevels: List<GlucoseLevel> = listOf(),
    val range: LongRange? = null
)

private class StatisticsViewModel(
    private val glucoseLevelRepository: GlucoseLevelRepository,
    private val handler: PrepareGlucoseLevelsReport.Handler
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsViewModelState())
    val state = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(
            glucoseLevels = glucoseLevelRepository.fetch()
        )
    }

    fun filterGlucoseLevels(from: Long?, to: Long?) {
        if (from == null && to == null) {
            _state.value = _state.value.copy(
                glucoseLevels = glucoseLevelRepository.fetch(),
                range = null
            )
        }

        if (from == null || to == null) {
            return
        }

        _state.value = _state.value.copy(
            glucoseLevels = glucoseLevelRepository.fetch(
                from.toLocalDateTime(),
                to.toLocalDateTime()
            ),
            range = LongRange(from, to)
        )
    }

    fun generateFileName(): String {
        return handler.handle(
            PrepareGlucoseLevelsReport.GenerateFileNameCommand(
                _state.value.range?.toLocalDateTimePair()
            )
        )
    }

    fun generateReport(stream: OutputStream) {
        return handler.handle(
            PrepareGlucoseLevelsReport.WriteReportCommand(
                _state.value.range?.toLocalDateTimePair(),
                stream
            )
        )
    }

    private fun LongRange.toLocalDateTimePair(): Pair<LocalDateTime, LocalDateTime> {
        return Pair(
            start.toLocalDateTime(),
            endInclusive.toLocalDateTime()
        )
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StatisticsViewModel(
                    ServiceLocator.glucoseLevelRepository(),
                    ServiceLocator.prepareGlucoseLevelReport()
                ) as T
            }
        }
    }
}

@Preview
@Composable
private fun ContextPreview() {
    val repository = StubGlucoseLevelRepository().apply {
        List(30) { id ->
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                DateTime(),
                id
            ).also { persist(it) }
        }
    }
    val model = StatisticsViewModel(
        repository,
        PrepareGlucoseLevelsReport.Handler(repository)
    )

    Content(model) {}
}

@Preview
@Composable
private fun GlucoseLevelsTablePreview() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(3.dp)
    ) {
        GlucoseLevelsTable(List(3) { id ->
            GlucoseLevel(
                GlucoseLevel.MeasureType.BEFORE_MEAL,
                GlucoseLevel.Value(1.2F),
                DateTime(),
                id
            )
        })
    }
}