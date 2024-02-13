package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.ShortInsulin

class InsulinCalculator {
    fun calculateInsulin(breadUnits: BreadUnit, carbohydrate: Carbohydrate): ShortInsulin {
        return ShortInsulin(carbohydrate.value * breadUnits.value)
    }
}