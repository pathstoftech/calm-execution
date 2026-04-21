package com.example.a30daysofcalmexecution.core.model

@JvmInline
value class TipId(val value: String) {
    init {
        require(value.isNotBlank()) { "TipId must not be blank." }
    }
    override fun toString(): String = value
}