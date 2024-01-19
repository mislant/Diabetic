package com.diabetic.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diabetic.application.command.AddGlucoseLevel
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.ui.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GlucoseLevelViewModel(
    private val repository: GlucoseLevelRepository,
    private val addHandler: AddGlucoseLevel.Handler
) : ViewModel() {

    private val _glucoseLevels = MutableStateFlow(repository.fetchAll())
    val glucoseLevels = _glucoseLevels.asStateFlow()

    fun addGlucoseLevelBeforeMeal(value: Float) {
        addHandler.handle(
            AddGlucoseLevel.Command(
                value,
                GlucoseLevel.MeasureType.BEFORE_MEAL
            )
        )
        _glucoseLevels.value = repository.fetchAll()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GlucoseLevelViewModel(
                    ServiceLocator.get().glucoseLevelRepository(),
                    ServiceLocator.get().addGlucoseLevelHandler()
                ) as T
            }
        }
    }
}