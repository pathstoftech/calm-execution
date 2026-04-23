package com.example.a30daysofcalmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorValidSectionsCategoriesTest {
    private val validator = CatalogValidator()

    @Test
    fun `validate returns valid when all sections and categories are known`() {
        val dto = validCatalogDto()

        val result = validator.validate(dto)

        assertTrue(result.isValid)
        assertTrue(result.errors.none { it.contains("unknown section key", ignoreCase = true) })
        assertTrue(result.errors.none { it.contains("unknown category key", ignoreCase = true) })
    }

    @Test
    fun `validate returns invalid when a tip uses an unknown section key`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) tip.copy(section = "not_a_real_section") else tip
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unknown section key", ignoreCase = true) })
    }

    @Test
    fun `validate returns invalid when a tip uses an unknown category key`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) tip.copy(category = "not_a_real_category") else tip
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unknown category key", ignoreCase = true) })
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