package com.pathstoftech.calmexecution.core.data.catalog

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class CatalogAssetValidationTest {

    private val json = Json {
        ignoreUnknownKeys = false
        explicitNulls = false
    }

    @Test
    fun catalogImageKeysResolveToRuntimeWebpAssets() {
        val catalog = loadCatalogDto()
        val assetKeys = runtimeTipAssetKeys()

        val unresolvedImageKeys = catalog.tips
            .map { it.imageKey }
            .filterNot { it in assetKeys }
            .sorted()

        assertTrue(
            "Catalog references image keys without matching drawable-nodpi WebP files: $unresolvedImageKeys",
            unresolvedImageKeys.isEmpty(),
        )
    }

    @Test
    fun runtimeTipAssetsAreMappedByCatalog() {
        val catalog = loadCatalogDto()
        val catalogImageKeys = catalog.tips
            .map { it.imageKey }
            .toSet()

        val unmappedAssets = runtimeTipAssetKeys()
            .filterNot { it in catalogImageKeys }
            .sorted()

        assertTrue(
            "drawable-nodpi contains tip WebP assets not referenced by tips_catalog.json: $unmappedAssets",
            unmappedAssets.isEmpty(),
        )
    }

    @Test
    fun catalogHasExactlyOneImageKeyPerTip() {
        val catalog = loadCatalogDto()

        val blankImageKeyTipIds = catalog.tips
            .filter { it.imageKey.isBlank() }
            .map { it.id }

        val duplicateImageKeys = catalog.tips
            .groupBy { it.imageKey }
            .filterValues { tips -> tips.size > 1 }
            .keys
            .sorted()

        assertTrue(
            "Tips with blank imageKey: $blankImageKeyTipIds",
            blankImageKeyTipIds.isEmpty(),
        )
        assertTrue(
            "Duplicate imageKeys in catalog: $duplicateImageKeys",
            duplicateImageKeys.isEmpty(),
        )
        assertEquals(
            "Each of 30 tips must have exactly one unique imageKey.",
            30,
            catalog.tips.map { it.imageKey }.toSet().size,
        )
    }

    @Test
    fun runtimeTipAssetsUseExpectedNamingAndFormat() {
        val invalidAssets = drawableNodpiDir()
            .listFiles()
            .orEmpty()
            .filter { file ->
                file.isFile &&
                        file.name.startsWith("tip_") &&
                        !RuntimeTipAssetNameRegex.matches(file.name)
            }
            .map { it.name }
            .sorted()

        assertTrue(
            "Runtime tip assets must use tip_XX_lower_snake_case.webp naming: $invalidAssets",
            invalidAssets.isEmpty(),
        )
    }

    @Test
    fun catalogAssetSetContainsThirtyTipWebpFiles() {
        val assetKeys = runtimeTipAssetKeys()

        assertEquals(
            "drawable-nodpi must contain exactly 30 runtime tip WebP assets.",
            30,
            assetKeys.size,
        )
    }

    @Test
    fun nonDecorativeImagesHaveContentDescriptions() {
        val catalog = loadCatalogDto()

        val missingDescriptions = catalog.tips
            .filterNot { it.imageDecorative }
            .filter { it.imageContentDescription.isNullOrBlank() }
            .map { it.id }

        assertTrue(
            "Non-decorative images must have imageContentDescription: $missingDescriptions",
            missingDescriptions.isEmpty(),
        )
    }

    @Test
    fun decorativeImagesDoNotHaveContentDescriptions() {
        val catalog = loadCatalogDto()

        val decorativeWithDescriptions = catalog.tips
            .filter { it.imageDecorative }
            .filter { !it.imageContentDescription.isNullOrBlank() }
            .map { it.id }

        assertTrue(
            "Decorative images should not provide imageContentDescription: $decorativeWithDescriptions",
            decorativeWithDescriptions.isEmpty(),
        )
    }

    @Test
    fun catalogValidatorAcceptsRuntimeImageKeys() {
        val catalog = loadCatalogDto()
        val assetKeys = runtimeTipAssetKeys().toSet()

        val result = CatalogValidator().validate(
            dto = catalog,
            imageKeyExists = { imageKey -> imageKey in assetKeys },
        )

        assertTrue(
            "Catalog validation failed with runtime asset resolver:\n${result.errors.joinToString(separator = "\n")}",
            result.isValid,
        )
    }

    private fun loadCatalogDto(): CatalogDto {
        val catalogFile = appDir()
            .resolve("src/main/res/raw/tips_catalog.json")

        assertTrue(
            "Missing catalog file: ${catalogFile.absolutePath}",
            catalogFile.isFile,
        )

        return json.decodeFromString(
            deserializer = CatalogDto.serializer(),
            string = catalogFile.readText(),
        )
    }

    private fun runtimeTipAssetKeys(): List<String> =
        drawableNodpiDir()
            .listFiles()
            .orEmpty()
            .filter { file ->
                file.isFile &&
                        file.name.startsWith("tip_") &&
                        file.extension == "webp"
            }
            .map { file -> file.nameWithoutExtension }
            .sorted()

    private fun drawableNodpiDir(): File {
        val dir = appDir()
            .resolve("src/main/res/drawable-nodpi")

        assertTrue(
            "Missing drawable-nodpi directory: ${dir.absolutePath}",
            dir.isDirectory,
        )

        return dir
    }

    private fun appDir(): File {
        val candidates = listOf(
            File("."),
            File("app"),
        )

        return candidates
            .map { it.canonicalFile }
            .firstOrNull { candidate ->
                candidate.resolve("src/main/res/raw/tips_catalog.json").isFile
            }
            ?: error("Unable to locate app module directory from ${File(".").canonicalPath}")
    }

    private companion object {
        val RuntimeTipAssetNameRegex = Regex("""tip_\d{2}_[a-z0-9_]+\.webp""")
    }
}