package com.diabetic.domain.model

class NotFound(private val entity: String) : DomainException() {
    override val message: String
        get() = "Required entity $entity not found"

    companion object {
        fun foodIntake(): NotFound {
            return NotFound("Food intake")
        }
    }
}