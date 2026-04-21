package com.example.a30daysofcalmexecution.core.model

enum class TipCompletionStatus(
    val wireValue: String
) {
    NOT_STARTED(wireValue = "not_started"),
    IN_PROGRESS(wireValue = "in_progress"),
    COMPLETED(wireValue = "completed");

    companion object {
        fun fromWireValue(value: String): TipCompletionStatus? =
            entries.firstOrNull { it.wireValue == value }
    }
}