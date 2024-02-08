package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.iso
import java.time.LocalDateTime

class GlucoseLevelRoomRepository(private val dao: GlucoseLevelDAO) : GlucoseLevelRepository {
    override fun fetch(): List<GlucoseLevel> {
        return dao.all().map { it.cast() }
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel> {
        return dao.all(from.iso(), to.iso()).map { it.cast() }
    }

    override fun persist(glucoseLevel: GlucoseLevel) {
        dao.insert(
            GlucoseLevelEntity(
                measureType = glucoseLevel.type.toString(),
                date = glucoseLevel.date.format().iso(),
                value = glucoseLevel.value.level
            )
        )
    }
}

private fun GlucoseLevelEntity.cast() = GlucoseLevel(
    id = id,
    type = GlucoseLevel.MeasureType.from(measureType),
    value = GlucoseLevel.Value(value),
    date = DateTime.fromString(date)
)

