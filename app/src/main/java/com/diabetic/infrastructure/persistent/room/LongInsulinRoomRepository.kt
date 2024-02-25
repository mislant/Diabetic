package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import com.diabetic.domain.model.dateTime
import com.diabetic.domain.model.iso
import java.time.LocalDateTime

class LongInsulinRoomRepository(private val dao: LongInsulinDao) : LongInsulinRepository {
    override fun persist(insulin: LongInsulin): LongInsulin {
        return insulin.also {
            it.id = dao
                .insert(it.entity())!!.toInt()
        }
    }

    override fun fetch(): List<LongInsulin> {
        return dao.fetch().map {
            it.cast()
        }
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<LongInsulin> {
        return dao.fetch(from.iso(), to.iso()).map {
            it.cast()
        }
    }

    private fun LongInsulin.entity(): LongInsulinEntity = LongInsulinEntity(
        id = 0,
        value = value,
        date = datetime.format().iso()
    )

    private fun LongInsulinEntity.cast(): LongInsulin = LongInsulin(
        id = id,
        value = value,
        datetime = date.dateTime
    )
}