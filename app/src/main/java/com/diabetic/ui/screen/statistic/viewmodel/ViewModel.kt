package com.diabetic.ui.screen.statistic.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.diabetic.ui.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.OutputStream
import androidx.lifecycle.ViewModel as AndroidViewModel

class ViewModel(
    private val strategies: Strategies,
    initial: ReportState<Any> = ReportState.GlucoseLevels()
) : AndroidViewModel() {
    private val strategy: ReportStrategy<Any>
        get() = strategies.of(_state.value)
    private val states: MutableMap<ReportState.Report, ReportState<Any>> = mutableMapOf()

    private val _state = MutableStateFlow(initial)
    private var value: ReportState<Any>
        get() = _state.value
        set(value) {
            _state.value = value
        }

    val state = _state

    init {
        updateData()
    }

    fun changePage(new: ReportState.Report) {
        states[value.of] = value

        if (states[new] == null) {
            value = new.state()
            updateData()
        } else {
            value = states[new]!!
        }
    }

    fun filter(from: Long?, to: Long?) {
        Log.i("Filter::ViewModel::input", "${from.toString()} - ${to.toString()}")
        if (from == null && to == null) {
            applyFilter()
        }

        if (from == null || to == null) {
            return
        }

        applyFilter(LongRange(from, to))
    }

    private fun applyFilter(filter: LongRange? = null) {
        value = value.copy(
            filter = filter,
            data = fetch(filter)
        )
    }

    private fun updateData() {
        value = value.copy(
            data = fetch(value.filter)
        )
    }

    private fun fetch(filter: LongRange? = null): List<Any> {
        return strategy
            .fetch(filter)
    }

    fun generateReportName(): String {
        return strategy.generateReportName(value.filter)
    }

    fun generateReport(stream: OutputStream) {
        strategy.generateReport(
            value.filter,
            stream
        )
    }

    fun deleteRecord(elementIndex: Int) {
        val element: Any = value.data.getOrNull(elementIndex) ?: return
        strategy.delete(element)
        updateData()
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : AndroidViewModel> create(modelClass: Class<T>): T {
                return ViewModel(
                    Strategies(
                        GlucoseReport(
                            ServiceLocator.glucoseLevelRepository(),
                            ServiceLocator.prepareGlucoseLevelReport()
                        ),
                        FoodIntakeReport(
                            ServiceLocator.foodIntakeRepository(),
                            ServiceLocator.prepareFoodIntakeReport()
                        ),
                        LongInsulinReport(
                            ServiceLocator.longInsulinRepository(),
                            ServiceLocator.prepareLongInsulinReport()
                        )
                    )
                ) as T
            }
        }
    }
}