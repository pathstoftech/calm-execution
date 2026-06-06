package com.pathstoftech.calmexecution.core.data.catalog

interface CatalogDataSource {
    suspend fun readCatalog(): CatalogDto
}