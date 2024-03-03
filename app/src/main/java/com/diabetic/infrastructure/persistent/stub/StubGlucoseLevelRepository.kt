package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import java.time.LocalDateTime

class StubGlucoseLevelRepository : InMemoryStorage<GlucoseLevel>(), GlucoseLevelRepository {
    override fun fetch(): List<GlucoseLevel> {
        return storage.toList()
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel> {
        return storage.toList().filter {
            it.datetime > from &&
                    it.datetime < to
        }
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        storage.add(glucoseLevel)
    }

    override fun fetch(id: Int): GlucoseLevel? =
        storage.find { it.id == id }

    override fun delete(id: Int) {
        storage.removeAt(id)
    }
}