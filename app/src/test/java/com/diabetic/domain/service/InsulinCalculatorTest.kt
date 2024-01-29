package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.Insulin
import org.junit.Assert
import org.junit.Test

class InsulinCalculatorTest {

    @Test
    fun inline_calculating_before_food_intake() {
        val breadUnit = BreadUnit(1)
        val carbohydrate = Carbohydrate(1.2F)
        val calculator = InsulinCalculator()

        val insulin = calculator.calculateBeforeFoodIntake(
            breadUnit,
            carbohydrate
        )

        Assert.assertTrue(insulin == Insulin(1.2F))
    }
}