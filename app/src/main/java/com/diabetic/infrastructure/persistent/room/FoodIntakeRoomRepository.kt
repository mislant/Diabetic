package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.domain.model.time.datetime
import com.diabetic.domain.model.time.milliseconds
import java.time.LocalDateTime

class FoodIntakeRoomRepository(private val dao: FoodIntakeDao) : FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake = dao.insert(foodIntake.entity()).let {
        foodIntake.id = it.toInt()
        foodIntake
    }

    override fun fetch(id: Int): FoodIntake? = dao.fetch(id)?.cast()

    override fun fetch(): List<FoodIntake> = dao.fetch().map { it.cast() }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<FoodIntake> =
        dao.fetch(from.milliseconds, to.milliseconds).map {
            it.cast()
        }

    override fun delete(id: Int) {
        dao.fetch(id)?.also {
            dao.delete(it)
        }
    }

    private fun FoodIntake.entity(): FoodIntakeEntity = FoodIntakeEntity(
        breadUnit = breadUnit.value,
        insulin = insulin.value,
        datetime = datetime.milliseconds
    )

    private fun FoodIntakeEntity.cast(): FoodIntake = FoodIntake(
        id = id,
        breadUnit = BreadUnit(breadUnit),
        insulin = ShortInsulin(insulin),
        datetime = datetime.datetime
    )
}