package com.diabetic.infrastructure.persistent.stub

open class InMemoryStorage<T> {
    protected val storage = mutableListOf<T>()
}