package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.Insulin

class InsulinCalculator {
    fun calculateBeforeFoodIntake(breadUnits: BreadUnit, carbohydrate: Carbohydrate): Insulin {
        return Insulin(carbohydrate.value * breadUnits.value)
    }
}