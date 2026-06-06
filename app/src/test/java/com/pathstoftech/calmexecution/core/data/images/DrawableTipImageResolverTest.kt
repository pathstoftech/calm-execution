package com.pathstoftech.calmexecution.core.data.images

import com.pathstoftech.calmexecution.R
import com.pathstoftech.calmexecution.core.data.catalog.CatalogDto
import com.pathstoftech.calmexecution.core.data.catalog.CatalogValidator
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DrawableTipImageResolverTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    private val resolver = DrawableTipImageResolver()

    @Test
    fun everyCatalogImageKeyResolvesToDrawable() {
        val unresolvedImageKeys =
            readCatalog()
                .tips
                .map { tip -> tip.imageKey }
                .filter { imageKey -> resolver.resolve(imageKey) == null }

        assertTrue(
            "Unresolved image keys: $unresolvedImageKeys",
            unresolvedImageKeys.isEmpty(),
        )
    }

    @Test
    fun everyResolverImageKeyExistsInCatalog() {
        val catalogImageKeys =
            readCatalog()
                .tips
                .map { tip -> tip.imageKey }
                .toSet()

        val orphanedResolverKeys =
            resolver.knownImageKeys() - catalogImageKeys

        assertTrue(
            "Resolver has orphaned image keys: $orphanedResolverKeys",
            orphanedResolverKeys.isEmpty(),
        )
    }

    @Test
    fun everyCatalogImageKeyHasMatchingRuntimeAssetFile() {
        val missingFiles =
            readCatalog()
                .tips
                .map { tip -> "${tip.imageKey}.webp" }
                .filterNot { fileName ->
                    Files.exists(
                        Path.of(
                            "src",
                            "main",
                            "res",
                            "drawable-nodpi",
                            fileName,
                        ),
                    )
                }

        assertTrue(
            "Missing runtime asset files: $missingFiles",
            missingFiles.isEmpty(),
        )
    }

    @Test
    fun runtimeAssetDirectoryHasNoUnexpectedTipImages() {
        val expectedFileNames =
            readCatalog()
                .tips
                .map { tip -> "${tip.imageKey}.webp" }
                .toSet()

        val runtimeDirectory =
            Path.of(
                "src",
                "main",
                "res",
                "drawable-nodpi",
            )

        val actualTipFileNames =
            Files.list(runtimeDirectory).use { paths ->
                paths
                    .filter { path -> Files.isRegularFile(path) }
                    .map { path -> path.fileName.toString() }
                    .filter { fileName ->
                        fileName.startsWith("tip_") && fileName.endsWith(".webp")
                    }
                    .toList()
                    .toSet()
            }

        val unexpectedFiles = actualTipFileNames - expectedFileNames

        assertTrue(
            "Unexpected runtime tip assets: $unexpectedFiles",
            unexpectedFiles.isEmpty(),
        )
    }

    @Test
    fun resolverResourceIdsExistInRDrawable() {
        val drawableResourceIds =
            R.drawable::class.java.fields
                .map { field -> field.getInt(null) }
                .toSet()

        val missingResourceIds =
            resolver.knownImageKeys()
                .map { imageKey ->
                    imageKey to resolver.resolve(imageKey)
                }
                .filter { (_, resourceId) ->
                    resourceId == null || resourceId !in drawableResourceIds
                }

        assertTrue(
            "Resolver maps to missing R.drawable ids: $missingResourceIds",
            missingResourceIds.isEmpty(),
        )
    }

    @Test
    fun catalogValidatorAcceptsImageKeyResolver() {
        val result =
            CatalogValidator().validate(
                dto = readCatalog(),
                imageKeyExists = { imageKey ->
                    resolver.resolve(imageKey) != null
                },
            )

        assertTrue(
            result.errors.joinToString(separator = "\n"),
            result.isValid,
        )
    }

    @Test
    fun catalogImageKeysAreUnique() {
        val imageKeys =
            readCatalog()
                .tips
                .map { tip -> tip.imageKey }

        assertEquals(
            "Catalog image keys must be unique.",
            imageKeys.size,
            imageKeys.toSet().size,
        )
    }

    @Test
    fun unknownImageKeyReturnsNull() {
        assertEquals(
            null,
            resolver.resolve("missing_image_key"),
        )
    }

    @Test
    fun nonDecorativeImagesHaveContentDescriptions() {
        val invalidTips =
            readCatalog()
                .tips
                .filter { tip ->
                    !tip.imageDecorative && tip.imageContentDescription.isNullOrBlank()
                }
                .map { tip -> tip.id }

        assertTrue(
            "Non-decorative tips missing content descriptions: $invalidTips",
            invalidTips.isEmpty(),
        )
    }

    @Test
    fun decorativeImagesDoNotProvideContentDescriptions() {
        val invalidTips =
            readCatalog()
                .tips
                .filter { tip ->
                    tip.imageDecorative && !tip.imageContentDescription.isNullOrBlank()
                }
                .map { tip -> tip.id }

        assertTrue(
            "Decorative tips should not provide content descriptions: $invalidTips",
            invalidTips.isEmpty(),
        )
    }

    @Test
    fun resolverContainsExactlyThirtyImageKeys() {
        assertEquals(
            30,
            resolver.knownImageKeys().size,
        )
    }

    @Test
    fun eachResolvedResourceIdIsPositive() {
        val invalidResolvedValues =
            resolver.knownImageKeys()
                .map { imageKey ->
                    imageKey to resolver.resolve(imageKey)
                }
                .filter { (_, resourceId) ->
                    resourceId == null || resourceId <= 0
                }

        assertTrue(
            "Invalid resolved resource ids: $invalidResolvedValues",
            invalidResolvedValues.isEmpty(),
        )
    }

    private fun readCatalog(): CatalogDto {
        val catalogPath =
            Path.of(
                "src",
                "main",
                "res",
                "raw",
                "tips_catalog.json",
            )

        assertTrue(
            "Catalog file does not exist: $catalogPath",
            Files.exists(catalogPath),
        )

        val catalogText = Files.readString(catalogPath)

        assertFalse(
            "Catalog file is blank.",
            catalogText.isBlank(),
        )

        return json.decodeFromString<CatalogDto>(catalogText)
    }
}