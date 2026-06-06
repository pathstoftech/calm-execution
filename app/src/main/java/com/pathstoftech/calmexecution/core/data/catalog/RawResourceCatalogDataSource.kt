package com.pathstoftech.calmexecution.core.data.catalog

import android.content.Context
import com.pathstoftech.calmexecution.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RawResourceCatalogDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val json: Json
) : CatalogDataSource {

    override suspend fun readCatalog(): CatalogDto = withContext(Dispatchers.IO) {
        val rawJson = context.resources
            .openRawResource(R.raw.tips_catalog)
            .bufferedReader()
            .use { it.readText() }

        json.decodeFromString<CatalogDto>(rawJson)
    }
}