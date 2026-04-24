package com.example.a30daysofcalmexecution.core.data.catalog

import com.example.a30daysofcalmexecution.core.model.JourneyCatalog
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.Tip
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipSection
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val dataSource: CatalogDataSource,
    private val mapper: CatalogMapper,
    private val validator: CatalogValidator
) : CatalogRepository {
    private val mutex = Mutex()
    private var cachedCatalog: JourneyCatalog? = null
    override suspend fun getCatalog(): JourneyCatalog {
        cachedCatalog?.let { return it }

        return mutex.withLock {
            cachedCatalog?.let { return it }

            val dto = dataSource.readCatalog()
            val validation = validator.validate(dto)

            check(validation.isValid) {
                buildString {
                    appendLine("Catalog validation failed.")
                    validation.errors.forEach { error ->
                        appendLine("- $error")
                    }
                }
            }

            mapper.map(dto).also { mapped ->
                cachedCatalog = mapped
            }
        }
    }

    override suspend fun getTip(tipId: TipId): Tip? {
        return getCatalog()
            .allTips
            .firstOrNull { it.id == tipId }
    }
    override suspend fun getSection(sectionKey: SectionKey): TipSection? {
        return getCatalog()
            .sections
            .firstOrNull { it.key == sectionKey }
    }

    override suspend fun getTipsForSection(sectionKey: SectionKey): List<Tip> {
        return getSection(sectionKey)?.tips.orEmpty()
    }

    override suspend fun getAdjacentTipIds(tipId: TipId): AdjacentTipIds {
        val allTips = getCatalog()
            .allTips
            .sortedBy { it.dayNumber }

        val index = allTips.indexOfFirst { it.id == tipId }

        if (index == -1) {
            return AdjacentTipIds(
                previous = null,
                next = null
            )
        }

        val previous = allTips.getOrNull(index - 1)?.id
        val next = allTips.getOrNull(index + 1)?.id

        return AdjacentTipIds(
            previous = previous,
            next = next
        )
    }
}