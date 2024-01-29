package com.diabetic.domain.model

import java.time.LocalDateTime

interface GlucoseLevelRepository {
    fun fetchAll(): List<GlucoseLevel>
    fun fetchRange(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel>
    fun persist(glucoseLevel: GlucoseLevel)
}