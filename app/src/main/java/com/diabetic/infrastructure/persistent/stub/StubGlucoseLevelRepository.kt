package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository

class StubGlucoseLevelRepository : GlucoseLevelRepository {
    private val storage: MutableList<GlucoseLevel> = mutableListOf()

    override fun fetchAll(): List<GlucoseLevel> {
        return storage.toList()
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        storage.add(glucoseLevel)
    }
}