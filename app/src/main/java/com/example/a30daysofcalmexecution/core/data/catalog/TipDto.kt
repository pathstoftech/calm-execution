package com.example.a30daysofcalmexecution.core.data.catalog

import kotlinx.serialization.Serializable

@Serializable
data class TipDto(
    val id: String,
    val dayNumber: Int,
    val section: String,
    val category: String,
    val title: String,
    val previewText: String,
    val problem: String,
    val tip: String,
    val whyItHelps: String,
    val tryToday: String,
    val imageKey: String,
    val imageContentDescription: String? = null,
    val imageDecorative: Boolean = false
)
