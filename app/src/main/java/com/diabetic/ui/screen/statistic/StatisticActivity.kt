package com.diabetic.ui.screen.statistic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diabetic.application.command.PrepareFoodIntakeReport
import com.diabetic.application.command.PrepareGlucoseLevelsReport
import com.diabetic.application.command.PrepareLongInsulinReport
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.LongInsulin
import com.diabetic.infrastructure.persistent.stub.StubFoodIntakeRepository
import com.diabetic.infrastructure.persistent.stub.StubGlucoseLevelRepository
import com.diabetic.infrastructure.persistent.stub.StubLongInsulinRepository
import com.diabetic.ui.screen.component.DiabeticLayout
import com.diabetic.ui.screen.statistic.component.DateRangeFilter
import com.diabetic.ui.screen.statistic.component.ExportButton
import com.diabetic.ui.screen.statistic.component.ReportTable
import com.diabetic.ui.screen.statistic.component.TopBar
import com.diabetic.ui.screen.statistic.viewmodel.FoodIntakeReport
import com.diabetic.ui.screen.statistic.viewmodel.GlucoseReport
import com.diabetic.ui.screen.statistic.viewmodel.LongInsulinReport
import com.diabetic.ui.screen.statistic.viewmodel.ReportState
import com.diabetic.ui.screen.statistic.viewmodel.Strategies
import com.diabetic.ui.theme.DiabeticMaterialTheme
import com.diabetic.ui.screen.statistic.viewmodel.ViewModel as InternalViewModel

class StatisticActivity : ComponentActivity() {
    private val model: InternalViewModel by viewModels {
        InternalViewModel.Factory
    }
    private val exportReport = registerForActivityResult(CreateReport()) {
        CreateReport.handle(it, this) { stream ->
            model.generateReport(stream)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content(model = model) {
                exportReport.launch(
                    model.generateReportName()
                )
            }
        }
    }
}

@Composable
private fun Content(
    model: InternalViewModel,
    exportReport: () -> Unit
) {
    val state = model.state
        .collectAsState()
        .value

    DiabeticMaterialTheme {
        DiabeticLayout(
            topBarContent = {
                TopBar(
                    current = state.of,
                    onTabClick = model::changePage
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                DateRangeFilter(
                    state.filter,
                    filter = model::filter
                )
                ReportTable(state)
                ExportButton(exportReport)
            }
        }
    }
}

@Preview
@Composable
private fun GlucoseReportContentPreview() {
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
    Content(
        model = InternalViewModel(
            Strategies(
                GlucoseReport(
                    repository,
                    PrepareGlucoseLevelsReport.Handler(repository)
                ),
                FoodIntakeReport(
                    StubFoodIntakeRepository(),
                    PrepareFoodIntakeReport.Handler(StubFoodIntakeRepository())
                ),
                LongInsulinReport(
                    StubLongInsulinRepository(),
                    PrepareLongInsulinReport.Handler(StubLongInsulinRepository())
                )
            )
        )
    ) {

    }
}

@Preview
@Composable
private fun LongInsulinReportContentPreview() {
    val repository = StubLongInsulinRepository().apply {
        List(30) { id ->
            LongInsulin(
                id = id,
                datetime = DateTime(),
                value = 1.2F
            ).also { persist(it) }
        }
    }
    Content(
        model = InternalViewModel(
            Strategies(
                GlucoseReport(
                    StubGlucoseLevelRepository(),
                    PrepareGlucoseLevelsReport.Handler(StubGlucoseLevelRepository())
                ),
                FoodIntakeReport(
                    StubFoodIntakeRepository(),
                    PrepareFoodIntakeReport.Handler(StubFoodIntakeRepository())
                ),
                LongInsulinReport(
                    repository,
                    PrepareLongInsulinReport.Handler(repository)
                )
            ),
            initial = ReportState.Report.LONG_INSULIN.state()
        )
    ) {

    }
}
