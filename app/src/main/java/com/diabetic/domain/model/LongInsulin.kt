package com.diabetic.domain.model

import java.time.LocalDateTime

data class LongInsulin(
    var value: Float,
    var datetime: LocalDateTime = LocalDateTime.now(),
    var id: Int? = null
)