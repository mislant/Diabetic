package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.Carbohydrate
import com.diabetic.domain.model.CarbohydrateStorage

class StubCarbohydrateStorage : CarbohydrateStorage {
    override fun set(rate: Carbohydrate) {
    }

    override fun get(): Carbohydrate? {
        return null
    }
}