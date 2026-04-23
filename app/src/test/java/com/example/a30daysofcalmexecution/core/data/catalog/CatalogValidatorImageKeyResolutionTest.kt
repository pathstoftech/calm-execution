package com.example.a30daysofcalmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorImageKeyResolutionTest {
    private val validator = CatalogValidator()

    @Test
    fun `validate returns valid when all image keys resolve`() {
        val dto = validCatalogDto()

        val result = validator.validate(
            dto = dto,
            imageKeyExists = { true }
        )

        assertTrue(result.isValid)
        assertTrue(result.errors.none { it.contains("unresolved imageKey", ignoreCase = true) })
    }

    @Test
    fun `validate returns invalid when an image key does not resolve`() {
        val dto = validCatalogDto()

        val result = validator.validate(
            dto = dto,
            imageKeyExists = { imageKey ->
                imageKey != "tip_01_sample"
            }
        )

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unresolved imageKey", ignoreCase = true) })
    }

    private fun validCatalogDto(): CatalogDto {
        return CatalogDto(
            title = "30 Days of Calm Execution",
            subtitle = "Build calmer, more intentional work habits in 30 days.",
            sections = listOf(
                SectionDto(
                    key = "start_with_clarity",
                    title = "Start with Clarity",
                    subtitle = "Section subtitle",
                    startDay = 1,
                    endDay = 30
                )
            ),
            tips = (1..30).map { day ->
                validTipDto(day)
            }
        )
    }

    private fun validTipDto(dayNumber: Int): TipDto {
        val dayLabel = dayNumber.toString().padStart(2, '0')

        return TipDto(
            id = "day_${dayLabel}_sample",
            dayNumber = dayNumber,
            section = "start_with_clarity",
            category = "planning",
            title = "Sample title $dayNumber",
            previewText = "Sample preview $dayNumber",
            problem = "Sample problem $dayNumber",
            tip = "Sample tip $dayNumber",
            whyItHelps = "Sample whyItHelps $dayNumber",
            tryToday = "Sample tryToday $dayNumber",
            imageKey = "tip_${dayLabel}_sample",
            imageContentDescription = "Sample image description $dayNumber",
            imageDecorative = false
        )
    }
}