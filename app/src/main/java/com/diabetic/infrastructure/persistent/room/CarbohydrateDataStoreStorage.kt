package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateStorage

class CarbohydrateDataStoreStorage(private val dao: KeyValueDao) :
    CarbohydrateStorage {

    private val key = "carbohydrate"

    override fun set(rate: Carbohydrate) {
        dao.insert(
            KeyValueEntity(
                key = key,
                value = rate.value.toString(),
            )
        )
    }

    override fun get(): Carbohydrate? {
        val pair = dao.get(key)

        return if (pair == null) null
        else Carbohydrate(pair.value.toFloat())
    }
}