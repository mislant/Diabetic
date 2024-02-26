package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.iso
import java.time.LocalDateTime

class GlucoseLevelRoomRepository(private val dao: GlucoseLevelDao) : GlucoseLevelRepository {
    override fun fetch(id: Int): GlucoseLevel? =
        dao.fetch(id)?.cast()

    override fun fetch(): List<GlucoseLevel> =
        dao.fetch().map { it.cast() }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel> =
        dao.fetch(from.iso(), to.iso()).map { it.cast() }

    override fun persist(glucoseLevel: GlucoseLevel) {
        dao.insert(
            GlucoseLevelEntity(
                measureType = glucoseLevel.type.toString(),
                date = glucoseLevel.date.format().iso(),
                value = glucoseLevel.value.level
            )
        )
    }

    override fun delete(id: Int) {
        dao.fetch(id)?.also {
            dao.delete(it)
        }
    }

    private fun GlucoseLevelEntity.cast() = GlucoseLevel(
        id = id,
        type = GlucoseLevel.MeasureType.from(measureType),
        value = GlucoseLevel.Value(value),
        date = DateTime.fromString(date)
    )
}

