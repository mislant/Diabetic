package com.diabetic.domain.model

import java.time.LocalDateTime

interface LongInsulinRepository {
    fun persist(insulin: LongInsulin): LongInsulin
    fun fetch(): List<LongInsulin>
    fun fetch(from: LocalDateTime, to: LocalDateTime): List<LongInsulin>
}