package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.milliseconds
import java.time.LocalDateTime

class LongInsulinRoomRepository(private val dao: LongInsulinDao) : LongInsulinRepository {
    override fun persist(insulin: LongInsulin): LongInsulin =
        insulin.also { it.id = dao.insert(it.entity())!!.toInt() }

    override fun fetch(): List<LongInsulin> =
        dao.fetch().map { it.cast() }


    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<LongInsulin> =
        dao.fetch(from.milliseconds, to.milliseconds).map { it.cast() }

    override fun fetch(id: Int): LongInsulin? =
        dao.fetch(id)?.cast()

    override fun delete(id: Int) {
        dao.fetch(id)?.also {
            dao.delete(it)
        }
    }

    private fun LongInsulin.entity(): LongInsulinEntity = LongInsulinEntity(
        id = 0,
        value = value,
        datetime = datetime.milliseconds
    )

    private fun LongInsulinEntity.cast(): LongInsulin = LongInsulin(
        id = id,
        value = value,
        datetime = datetime.datetime
    )
}