package com.diabetic.infrastructure.persistant

import com.diabetic.domain.model.GlucoseLevel
import com.diabetic.domain.model.GlucoseLevelRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GlucoseLevelRoomRepository(private var dao: GlucoseLevelDAO) : GlucoseLevelRepository {
    override fun fetchAll(): List<GlucoseLevel> {
        val records = dao.all();
        return records.map {
            val level = GlucoseLevel(
                GlucoseLevel.MeasureType.from(it.measureType),
                GlucoseLevel.Value(it.value),
                LocalDateTime.parse(
                    it.date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                )
            )
            level.setId(it.id)
            level
        }
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
