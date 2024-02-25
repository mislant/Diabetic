package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository

class StubLongInsulinRepository : LongInsulinRepository, InMemoryStorage<LongInsulin>() {
    override fun persist(insulin: LongInsulin): LongInsulin {
        insulin.id = storage.run {
            add(insulin)
            lastIndex
        }
        return insulin
    }
}