package com.pathstoftech.calmexecution.core.data.catalog

import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Test

class ActualCatalogValidationTest {

    private val json = Json {
        ignoreUnknownKeys = false
        explicitNulls = false
    }

    private val validator = CatalogValidator()

    @Test
    fun `actual tips catalog is valid`() {
        val catalogJson = readCatalogJson()
        val dto = json.decodeFromString<CatalogDto>(catalogJson)

        val result = validator.validate(dto)

        assertTrue(
            result.errors.joinToString(separator = "\n"),
            result.isValid
        )
    }

    private fun readCatalogJson(): String {
        val candidates = listOf(
            File("src/main/res/raw/tips_catalog.json"),
            File("app/src/main/res/raw/tips_catalog.json")
        )

        val file = candidates.firstOrNull { it.exists() }
            ?: error("tips_catalog.json was not found in expected raw resource paths.")

        return file.readText()
    }
}