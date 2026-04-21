package com.example.a30daysofcalmexecution.core.model

enum class SectionKey(
    val wireValue: String,
    val title: String
) {
    START_WITH_CLARITY(
        wireValue = "start_with_clarity",
        title = "Start with clarity"
    ),
    BUILD_FOCUS(
        wireValue = "build_focus",
        title = "Build focus"
    ),
    PROTECT_BOUNDARIES(
        wireValue = "protect_boundaries",
        title = "Protect boundaries"
    ),
    SUSTAIN_ENERGY(
        wireValue = "sustain_energy",
        title = "Sustain energy"
    ),
    FINISH_AND_IMPROVE(
        wireValue = "finish_and_improve",
        title = "Finish and improve"
    );

    companion object {
        fun fromWireValue(value: String): SectionKey? = entries.firstOrNull {it.wireValue == value}
    }
}