package com.diabetic.domain.model

import java.time.LocalDateTime

data class FoodIntake(
    var breadUnit: BreadUnit,
    var insulin: ShortInsulin,
    var datetime: LocalDateTime = LocalDateTime.now(),
    var id: Int? = null
) {
}