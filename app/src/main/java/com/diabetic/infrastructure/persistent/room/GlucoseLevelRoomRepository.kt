package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.milliseconds
import java.time.LocalDateTime

class GlucoseLevelRoomRepository(private val dao: GlucoseLevelDao) : GlucoseLevelRepository {
    override fun fetch(id: Int): GlucoseLevel? =
        dao.fetch(id)?.cast()

    override fun fetch(): List<GlucoseLevel> =
        dao.fetch().map { it.cast() }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<GlucoseLevel> =
        dao.fetch(from.milliseconds, to.milliseconds).map { it.cast() }

    override fun persist(glucoseLevel: GlucoseLevel) {
        dao.insert(
            GlucoseLevelEntity(
                measureType = glucoseLevel.type.toString(),
                datetime = glucoseLevel.datetime.milliseconds,
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
        datetime = datetime.datetime
    )
}

