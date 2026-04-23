package com.example.a30daysofcalmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorTipCountTest {

    private val validator = CatalogValidator()

    @Test
    fun `validate return valid when catalog contains exactly 30 tips`() {
        val dto = CatalogDto(
            title = "30 Days of Calm Execution",
            subtitle = "Build calmer, more intentional work habits in 30 days.",
            sections = listOf(
                SectionDto(
                    key = "start_with_clarity",
                    title = "Start with clarity",
                    subtitle = "Section subtitle.",
                    startDay = 1,
                    endDay = 30
                )
            ),
            tips = (1..30).map { day ->
                validTipDto(dayNumber = day)
            }
        )

        val result = validator.validate(dto)

        assertTrue(result.isValid)
        assertTrue(result.errors.none { it.contains("exactly 30 tips") })
    }

    @Test
    fun `validate returns invalid when catalog does not contain exactly 30 tips`() {
        val dto = CatalogDto(
            title = "30 Days of Calm Execution",
            subtitle = "Build calmer, more intentional work habits in 30 days.",
            sections = listOf(
                SectionDto(
                    key = "start_with_clarity",
                    title = "Start with clarity",
                    subtitle = "Section subtitle",
                    startDay = 1,
                    endDay = 29
                )
            ),
            tips = (1..29).map { day ->
                validTipDto(dayNumber = day)
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("exactly 30 tips") })
    }

    private fun validTipDto(dayNumber: Int): TipDto {
        return TipDto(
            id = "day_${dayNumber.toString().padStart(2, '0')}_sample",
            dayNumber = dayNumber,
            section = "start_with_clarity",
            category = "planning",
            title = "Sample title $dayNumber",
            previewText = "Sample preview $dayNumber",
            problem = "Sample problem $dayNumber",
            tip = "Sample tip $dayNumber",
            whyItHelps = "Sample whyItHelps $dayNumber",
            tryToday = "Sample tryToday $dayNumber",
            imageKey = "tip_${dayNumber.toString().padStart(2, '0')}_sample",
            imageContentDescription = "Sample image description $dayNumber",
            imageDecorative = false
        )
    }
}