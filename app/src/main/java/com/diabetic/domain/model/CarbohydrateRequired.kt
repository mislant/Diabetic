package com.diabetic.domain.model

class CarbohydrateRequired : DomainException() {
    override val message: String
        get() = "Carbohydrate is required. Please specify it"
}