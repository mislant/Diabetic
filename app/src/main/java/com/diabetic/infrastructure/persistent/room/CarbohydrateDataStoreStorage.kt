package com.diabetic.infrastructure.persistent.room

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateStorage

class CarbohydrateDataStoreStorage(private val dao: KeyValueDao) :
    CarbohydrateStorage {

    private val key = "carbohydrate"

    override fun set(rate: Carbohydrate) {
        if (dao.exists(key)) update(rate)
        else new(rate)
    }

    private fun update(rate: Carbohydrate) {
        dao.run {
            get(key)!!.also {
                it.value = rate.value.toString()
            }.also {
                update(it)
            }
        }
    }

    private fun new(rate: Carbohydrate) {
        KeyValueEntity(
            key = key,
            value = rate.value.toString(),
        ).also {
            dao.insert(it)
        }
    }

    override fun get(): Carbohydrate? {
        val pair = dao.get(key)

        return if (pair == null) null
        else Carbohydrate(pair.value.toFloat())
    }
}