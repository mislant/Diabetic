package com.diabetic.infrastructure.persistant

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository

class GlucoseLevelRoomRepository(private var dao: GlucoseLevelDAO) : GlucoseLevelRepository {
    override fun fetchAll(): List<GlucoseLevel> {
        return dao.all().map {
            GlucoseLevel(
                GlucoseLevel.MeasureType.from(it.measureType),
                GlucoseLevel.Value(it.value),
                DateTime.fromString(it.date),
                it.id
            )
        }
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        dao.inset(
            GlucoseLevelEntity(
                measureType = glucoseLevel.type.toString(),
                date = glucoseLevel.date.format().iso(),
                value = glucoseLevel.value.level
            )
        )
    }
}
