package com.pathstoftech.calmexecution.core.model

@JvmInline
value class TipId(val value: String) {
    init {
        require(value.isNotBlank()) { "TipId must not be blank." }
    }
    override fun toString(): String = value
}