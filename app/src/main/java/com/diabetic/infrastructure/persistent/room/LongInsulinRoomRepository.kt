package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import com.diabetic.domain.model.dateTime

class LongInsulinRoomRepository(private val dao: LongInsulinDao) : LongInsulinRepository {
    override fun persist(insulin: LongInsulin): LongInsulin {
        return insulin.also {
            it.id = dao
                .insert(it.entity())!!.toInt()
        }
    }

    private fun LongInsulin.entity(): LongInsulinEntity = LongInsulinEntity(
        id = 0,
        value = value,
        date = datetime.format().iso()
    )

    private fun LongInsulinEntity.entity(): LongInsulin = LongInsulin(
        id = id,
        value = value,
        datetime = date.dateTime
    )
}