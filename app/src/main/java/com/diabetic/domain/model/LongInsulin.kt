package com.diabetic.domain.model

data class LongInsulin(
    var value: Float,
    var datetime: DateTime
) {
    var id: Int? = null

    constructor(
        id: Int,
        value: Float,
        datetime: DateTime
    ) : this(value, datetime) {
        this.id = id
    }

    constructor(
        value: Float,
    ) : this(value, DateTime())
}