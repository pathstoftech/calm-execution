package com.pathstoftech.calmexecution.core.data.catalog

import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    val key: String,
    val title: String,
    val subtitle: String? = null,
    val startDay: Int,
    val endDay: Int
)
