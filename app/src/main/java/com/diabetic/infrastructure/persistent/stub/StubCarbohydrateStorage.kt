package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateStorage

class StubCarbohydrateStorage : InMemoryStorage<Carbohydrate>(), CarbohydrateStorage {
    override fun set(rate: Carbohydrate) {
        storage.add(rate)
    }

    override fun get(): Carbohydrate? {
        return storage.getOrNull(0)
    }
}