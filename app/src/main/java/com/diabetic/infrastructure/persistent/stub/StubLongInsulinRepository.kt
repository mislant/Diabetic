package com.diabetic.infrastructure.persistent.stub

import com.diabetic.domain.model.LongInsulin
import com.diabetic.domain.model.LongInsulinRepository
import java.time.LocalDateTime

class StubLongInsulinRepository : LongInsulinRepository, InMemoryStorage<LongInsulin>() {
    override fun persist(insulin: LongInsulin): LongInsulin {
        insulin.id = storage.run {
            add(insulin)
            lastIndex
        }
        return insulin
    }

    override fun fetch(): List<LongInsulin> {
        return storage.toList()
    }

    override fun fetch(from: LocalDateTime, to: LocalDateTime): List<LongInsulin> {
        return storage.filter {
            it.datetime.localDataTime() > from &&
                    it.datetime.localDataTime() < to
        }.toList()
    }

    override fun fetch(id: Int): LongInsulin? = storage.find {
        it.id == id
    }

    override fun delete(id: Int) {
        fetch(id)?.also {
            storage.remove(it)
        }
    }
}