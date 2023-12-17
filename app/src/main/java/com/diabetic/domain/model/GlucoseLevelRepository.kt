package com.diabetic.domain.model

interface GlucoseLevelRepository {
    fun fetchAll(): List<GlucoseLevel>
    fun persist(glucoseLevel: GlucoseLevel)
}