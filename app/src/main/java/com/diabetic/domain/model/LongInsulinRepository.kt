package com.diabetic.domain.model

interface LongInsulinRepository {
    fun persist(insulin: LongInsulin): LongInsulin
}