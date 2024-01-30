package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import java.time.LocalDateTime

class StubGlucoseLevelRepository : GlucoseLevelRepository {
    private val storage: MutableList<GlucoseLevel> = mutableListOf()

    override fun fetchAll(): List<GlucoseLevel> {
        return storage.toList()
    }

    override fun fetchRange(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel> {
        return storage.toList().filter {
            it.date.localDataTime() > from &&
                    it.date.localDataTime() < to
        }
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        storage.add(glucoseLevel)
    }
}