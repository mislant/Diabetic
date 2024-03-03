package com.diabetic.domain.service

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.ShortInsulin
import org.junit.Assert
import org.junit.Test

class InsulinCalculatorTest {

    @Test
    fun `calculating insulin before food intake`() {
        val breadUnit = BreadUnit(1)
        val carbohydrate = Carbohydrate(1.2F)
        val calculator = InsulinCalculator()

        val insulin = calculator.calculateInsulin(
            breadUnit,
            carbohydrate
        )

        Assert.assertTrue(insulin == ShortInsulin(1.2F))
    }
}