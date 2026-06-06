package com.pathstoftech.calmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorAdditionalRulesTest {

    private val validator = CatalogValidator()

    @Test
    fun `validate returns invalid when section definition uses unknown section key`() {
        val dto = validCatalogDto().copy(
            sections = listOf(
                SectionDto(
                    key = "not_a_real_section",
                    title = "Unknown Section",
                    subtitle = "Section subtitle",
                    startDay = 1,
                    endDay = 30
                )
            )
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("unknown section key in sections list", ignoreCase = true)
            }
        )
    }

    @Test
    fun `validate returns invalid when day number is below one`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) tip.copy(dayNumber = 0) else tip
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("invalid dayNumber", ignoreCase = true)
            }
        )
    }

    @Test
    fun `validate returns invalid when day number is above thirty`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) tip.copy(dayNumber = 31) else tip
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("invalid dayNumber", ignoreCase = true)
            }
        )
    }

    @Test
    fun `validate returns invalid when section day range does not match contained tips`() {
        val dto = validCatalogDto().copy(
            sections = listOf(
                SectionDto(
                    key = "start_with_clarity",
                    title = "Start with Clarity",
                    subtitle = "Section subtitle",
                    startDay = 1,
                    endDay = 29
                )
            )
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("day range mismatch", ignoreCase = true)
            }
        )
    }

    @Test
    fun `validate returns invalid when decorative image has content description`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) {
                    tip.copy(
                        imageDecorative = true,
                        imageContentDescription = "This should not be present."
                    )
                } else {
                    tip
                }
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("decorative", ignoreCase = true) &&
                        it.contains("imageContentDescription", ignoreCase = true)
            }
        )
    }

    @Test
    fun `validate returns invalid when non decorative image has no content description`() {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) {
                    tip.copy(
                        imageDecorative = false,
                        imageContentDescription = null
                    )
                } else {
                    tip
                }
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any {
                it.contains("non-decorative", ignoreCase = true) &&
                        it.contains("imageContentDescription", ignoreCase = true)
            }
        )
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
                validTipDto(dayNumber = day)
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