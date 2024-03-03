package com.diabetic.ui.screen

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.readable
import com.diabetic.domain.model.time.readableShort
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import com.diabetic.infrastructure.persistent.stub.StubLongInsulinRepository
import com.diabetic.ui.ServiceLocator
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.theme.DiabeticMaterialTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.marker.rememberMarkerComponent
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.marker.DefaultMarkerLabelFormatter
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class ChartsActivity : ComponentActivity() {
    private val model: ChartsViewModel by viewModels { ChartsViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content(model)
        }
    }
}

@Composable
private fun Content(model: ChartsViewModel) {
    DiabeticMaterialTheme {
        DiabeticLayout { innerPadding ->
            val style = m3ChartStyle()
            ProvideChartStyle(chartStyle = style) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 15.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Chart(
                            "Длинный инсулин",
                            model.longInsulinModelProducer,
                            model.longInsulin.map { it.first },
                        )
                    }
                    item {
                        Chart(
                            "Уровень глюкозы",
                            model.glucoseLevelsModelProducer,
                            model.glucoseLevels.map { it.first },
                        )
                    }
                    item {
                        Chart(
                            "Короткий инсулин",
                            model.shortInsulinModelProducer,
                            model.shortInsulin.map { it.first },
                        )
                    }
                    item {
                        Chart(
                            "Хлебные единцы",
                            model.breadUnitModelProducer,
                            model.breadUnit.map { it.first },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chart(
    title: String,
    modelProducer: CartesianChartModelProducer,
    dates: List<LocalDateTime>,
) {
    val valueFormatter = DateValuesFormatter(dates)

    val guideline = rememberLineComponent(
        color = MaterialTheme.colorScheme.onSurface.copy(.2F),
        thickness = 2.dp,
        shape = DashedShape(Shapes.pillShape, 8F, 4F),
    )
    val marker = rememberMarkerComponent(
        label = rememberTextComponent(
            lineCount = 1,
            padding = dimensionsOf(4.dp, 8.dp),
            background = ShapeComponent(
                shape = MarkerCorneredShape(Corner.FullyRounded),
                color = MaterialTheme.colorScheme.surface.toArgb()
            ).setShadow(
                radius = 4F,
                dy = 2F,
                applyElevationOverlay = true
            )
        ),
        labelPosition = MarkerComponent.LabelPosition.Top,
        guideline = guideline
    ).apply {
        labelFormatter = MarkerLabelFormatterDateDecorator(
            labelFormatter as DefaultMarkerLabelFormatter,
            dates
        )
    }


    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(),
        startAxis = rememberStartAxis(
            title = title,
            titleComponent = rememberTextComponent(
                color = MaterialTheme.colorScheme.onBackground,
                padding = dimensionsOf(
                    vertical = 3.dp
                )
            ),
            guideline = null,
            horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
            itemPlacer = remember { AxisItemPlacer.Vertical.default(maxItemCount = { 5 }) },
        ),
        bottomAxis = rememberBottomAxis(
            title = "Дата",
            titleComponent = rememberTextComponent(color = MaterialTheme.colorScheme.onBackground),
            valueFormatter = valueFormatter,
            itemPlacer = remember {
                AxisItemPlacer.Horizontal.default(
                    spacing = 4,
                    offset = 1,
                    addExtremeLabelPadding = true
                )
            },
            guideline = null
        ),
        fadingEdges = rememberFadingEdges()
    )
    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        marker = marker,
        placeholder = {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Данных для графика\n$title\nнет",
                textAlign = TextAlign.Center
            )
        }
    )
}

private class DateValuesFormatter(
    private val dates: List<LocalDateTime>
) : AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    override fun formatValue(
        value: Float,
        chartValues: ChartValues,
        verticalAxisPosition: AxisPosition.Vertical?
    ): CharSequence {
        return dates[value.toInt()]
            .readableShort
    }
}

private class MarkerLabelFormatterDateDecorator(
    private val formatter: DefaultMarkerLabelFormatter,
    private val dates: List<LocalDateTime>
) : MarkerLabelFormatter {
    override fun getLabel(
        markedEntries: List<Marker.EntryModel>,
        chartValues: ChartValues
    ): CharSequence {
        val model = markedEntries.first()
        val datetime = dates[model.entry.x.toInt()].readable
        val value = formatter.getLabel(markedEntries, chartValues)

        return SpannableStringBuilder().apply {
            append(
                value,
                " ",
                datetime
            )
            setSpan(
                ForegroundColorSpan(model.color),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

private class ChartsViewModel(
    val glucoseLevelRepository: GlucoseLevelRepository,
    val foodIntakesRepository: FoodIntakeRepository,
    val longInsulinRepository: LongInsulinRepository
) : ViewModel() {
    val longInsulin = longInsulinRepository.fetch().map { it.datetime to it.value }
    val longInsulinModelProducer by mutableStateOf(CartesianChartModelProducer.build()).also {
        runBlocking {
            it.value.runTransaction {
                if (longInsulin.isNotEmpty()) {
                    lineSeries {
                        series(longInsulin.map { it.second })
                    }
                }
            }.await()
        }
    }

    val glucoseLevels = glucoseLevelRepository.fetch().map { it.datetime to it.value.level }
    val glucoseLevelsModelProducer by mutableStateOf(CartesianChartModelProducer.build()).also {
        runBlocking {
            it.value.runTransaction {
                if (glucoseLevels.isNotEmpty()) {
                    lineSeries {
                        series(glucoseLevels.map { it.second })
                    }
                }
            }.await()
        }
    }

    val shortInsulin = foodIntakesRepository.fetch().map { it.datetime to it.insulin.value }
    val shortInsulinModelProducer by mutableStateOf(CartesianChartModelProducer.build()).also {
        runBlocking {
            it.value.runTransaction {
                if (shortInsulin.isNotEmpty()) {
                    lineSeries {
                        series(shortInsulin.map { it.second })
                    }
                }
            }.await()
        }
    }

    val breadUnit = foodIntakesRepository.fetch().map { it.datetime to it.breadUnit.value }
    val breadUnitModelProducer by mutableStateOf(CartesianChartModelProducer.build()).also {
        runBlocking {
            it.value.runTransaction {
                if (breadUnit.isNotEmpty()) {
                    lineSeries {
                        series(breadUnit.map { it.second })
                    }
                }
            }.await()
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChartsViewModel(
                    ServiceLocator.glucoseLevelRepository(),
                    ServiceLocator.foodIntakeRepository(),
                    ServiceLocator.longInsulinRepository()
                ) as T
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    val insulin = listOf(
        "2024-01-01 18:37".datetime to 1.2F,
        "2024-01-01 20:18".datetime to .7F,
        "2024-01-02 08:47".datetime to 1.8F,
        "2024-01-02 12:04".datetime to 6.8F,
        "2024-01-02 18:27".datetime to .8F,
        "2024-01-02 22:15".datetime to 2.8F,
        "2024-01-03 08:47".datetime to 1.8F,
        "2024-01-03 12:04".datetime to 6.8F,
        "2024-01-03 18:27".datetime to .8F,
        "2024-01-04 18:37".datetime to 1.2F,
        "2024-01-04 20:18".datetime to .7F,
        "2024-01-03 08:47".datetime to 1.8F,
        "2024-01-03 12:04".datetime to 6.8F,
        "2024-01-03 18:27".datetime to .8F,
        "2024-01-04 18:37".datetime to 1.2F,
        "2024-01-04 20:18".datetime to .7F,
    ).map {
        LongInsulin(
            value = it.second,
            datetime = it.first
        )
    }

    DiabeticMaterialTheme {
        Content(
            ChartsViewModel(
                StubGlucoseLevelRepository(),
                StubFoodIntakeRepository(),
                StubLongInsulinRepository().apply {
                    insulin.forEach {
                        persist(it)
                    }
                }
            )
        )
    }
}