package com.example.a30daysofcalmexecution.core.data.catalog

interface CatalogDataSource {
    suspend fun readCatalog(): CatalogDto
}