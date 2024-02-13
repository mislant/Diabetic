package com.diabetic.domain.model

data class FoodIntake(
    var breadUnit: BreadUnit,
    var insulin: ShortInsulin,
    var date: DateTime
) {
    var id: Int? = null

    constructor(
        breadUnit: BreadUnit,
        insulin: ShortInsulin
    ) : this(breadUnit, insulin, DateTime())

    constructor(
        id: Int,
        breadUnit: BreadUnit,
        insulin: ShortInsulin,
        date: DateTime
    ) : this(breadUnit, insulin, date) {
        this.id = id
    }
}