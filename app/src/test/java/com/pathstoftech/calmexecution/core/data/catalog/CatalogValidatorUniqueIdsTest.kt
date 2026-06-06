package com.pathstoftech.calmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorUniqueIdsTest {
    private val validator = CatalogValidator()

    @Test
    fun `validate returns valid when all tip ids are unique`() {
        val dto = CatalogDto(
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
                validTipDto(
                    id = "day_${day.toString().padStart(2, '0')}_sample",
                    dayNumber = day
                )
            }
        )

        val result = validator.validate(dto)

        assertTrue(result.isValid)
        assertTrue(result.errors.none { it.contains("Duplicate tip id") })
    }

    @Test
    fun `validate returns invalid when duplicate tip ids exist`() {
        val dto = CatalogDto(
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
                val duplicatedId = if (day == 30) {
                    "day_01_sample"
                } else {
                    "day_${day.toString().padStart(2, '0')}_sample"
                }

                validTipDto(
                    id = duplicatedId,
                    dayNumber = day
                )
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Duplicate tip id") })
    }

    private fun validTipDto(
        id: String,
        dayNumber: Int
    ): TipDto {
        return TipDto(
            id = id,
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