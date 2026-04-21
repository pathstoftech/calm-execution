package com.example.a30daysofcalmexecution.core.model

enum class ThemeMode(
    val wireValue: String
) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromWireValue(value: String): ThemeMode? =
            entries.firstOrNull { it.wireValue == value }
    }
}