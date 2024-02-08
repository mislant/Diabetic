package com.diabetic.domain.model

import java.time.LocalDateTime

interface GlucoseLevelRepository {
    fun fetch(): List<GlucoseLevel>
    fun fetch(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel>
    fun persist(glucoseLevel: GlucoseLevel)
}