package com.example.a30daysofcalmexecution.core.data.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogValidatorNonBlankEditorialFieldsTest {
    private val validator = CatalogValidator()

    @Test
    fun `validate returns valid when editorial fields are non-blank`() {
        val dto = validCatalogDto()

        val result = validator.validate(dto)

        assertTrue(result.isValid)
        assertTrue(result.errors.none { it.contains("blank", ignoreCase = true) })
    }

    @Test
    fun `validate returns invalid when title is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(title = " "),
            expectedErrorFragment = "blank title"
        )
    }

    @Test
    fun `validate returns invalid when previewText is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(previewText = " "),
            expectedErrorFragment = "blank previewText"
        )
    }

    @Test
    fun `validate returns invalid when problem is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(problem = " "),
            expectedErrorFragment = "blank problem"
        )
    }

    @Test
    fun `validate returns invalid when tip is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(tip = " "),
            expectedErrorFragment = "blank tip"
        )
    }

    @Test
    fun `validate returns invalid when whyItHelps is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(whyItHelps = " "),
            expectedErrorFragment = "blank whyItHelps"
        )
    }

    @Test
    fun `validate returns invalid when tryToday is blank`() {
        assertBlankFieldDetected(
            updatedTip = validTipDto(1).copy(tryToday = " "),
            expectedErrorFragment = "blank tryToday"
        )
    }

    private fun assertBlankFieldDetected(
        updatedTip: TipDto,
        expectedErrorFragment: String
    ) {
        val dto = validCatalogDto().copy(
            tips = validCatalogDto().tips.mapIndexed { index, tip ->
                if (index == 0) updatedTip else tip
            }
        )

        val result = validator.validate(dto)

        assertFalse(result.isValid)
        assertTrue(
            result.errors.any { it.contains(expectedErrorFragment, ignoreCase = true) }
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