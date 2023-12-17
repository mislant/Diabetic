package com.diabetic.infrastructure.persistant

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository

class GlucoseLevelRoomRepository(private var dao: GlucoseLevelDAO) : GlucoseLevelRepository {
    override fun fetchAll(): List<GlucoseLevel> {
        TODO("Not yet implemented")
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        dao.inset(
            GlucoseLevelEntity(
                measureType = glucoseLevel.type().toString(),
                date = glucoseLevel.date(),
                value = glucoseLevel.value()
            )
        )
    }
}
