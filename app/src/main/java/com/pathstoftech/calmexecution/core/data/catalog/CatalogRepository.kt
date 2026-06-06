package com.pathstoftech.calmexecution.core.data.catalog

import com.pathstoftech.calmexecution.core.model.JourneyCatalog
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.Tip
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.TipSection

interface CatalogRepository {
    suspend fun getCatalog(): JourneyCatalog
    suspend fun getTip(tipId: TipId): Tip?
    suspend fun getSection(sectionKey: SectionKey): TipSection?
    suspend fun getTipsForSection(sectionKey: SectionKey): List<Tip>
    suspend fun getAdjacentTipIds(tipId: TipId): AdjacentTipIds
}
data class AdjacentTipIds(
    val previous: TipId?,
    val next: TipId?
)