package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.BreadUnit
import com.diabetic.domain.model.DateTime
import com.diabetic.domain.model.FoodIntake
import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.model.ShortInsulin
import com.diabetic.domain.model.iso
import java.time.LocalDateTime

class FoodIntakeRoomRepository(private val dao: FoodIntakeDao) : FoodIntakeRepository {
    override fun persist(foodIntake: FoodIntake): FoodIntake {
        return dao.insert(foodIntake.entity()).let {
            foodIntake.id = it.toInt()
            foodIntake
        }
    }

    override fun fetch(id: Int): FoodIntake? {
        return dao.fetch(id)?.cast()
    }

    override fun fetch(): List<FoodIntake> {
        return dao.fetch().map { it.cast() }
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<FoodIntake> {
        return dao.fetch(from.iso(), to.iso()).map {
            it.cast()
        }
    }

    private fun FoodIntake.entity(): FoodIntakeEntity = FoodIntakeEntity(
        breadUnit = breadUnit.value,
        insulin = insulin.value,
        date = date.format().iso()
    )

    private fun FoodIntakeEntity.cast(): FoodIntake = FoodIntake(
        id,
        BreadUnit(breadUnit),
        ShortInsulin(insulin),
        DateTime.fromString(date)
    )
}