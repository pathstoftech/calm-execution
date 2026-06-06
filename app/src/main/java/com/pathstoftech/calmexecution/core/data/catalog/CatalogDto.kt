package com.pathstoftech.calmexecution.core.data.catalog

import kotlinx.serialization.Serializable

@Serializable
data class CatalogDto(
    val title: String,
    val subtitle: String,
    val sections: List<SectionDto>,
    val tips: List<TipDto>
)
