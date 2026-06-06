package com.pathstoftech.calmexecution.core.data.catalog

import com.pathstoftech.calmexecution.core.model.TipId
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CatalogRepositoryFailurePathTest {

    @Test
    fun `getTip returns null for unknown tip id`() {
        runBlocking {
            val repository = createRepository(
                dataSource = FakeCatalogDataSource(validCatalogDto())
            )

            val tip = repository.getTip(TipId("not_a_real_tip"))

            assertNull(tip)
        }
    }

    @Test
    fun `getAdjacentTipIds returns null ids for unknown tip id`() {
        runBlocking {
            val repository = createRepository(
                dataSource = FakeCatalogDataSource(validCatalogDto())
            )

            val adjacent = repository.getAdjacentTipIds(TipId("not_a_real_tip"))

            assertNull(adjacent.previous)
            assertNull(adjacent.next)
        }
    }

    @Test
    fun `getCatalog throws validation error for malformed catalog content`() {
        runBlocking {
            val malformedDto = validCatalogDto().copy(
                tips = validCatalogDto().tips.mapIndexed { index, tip ->
                    when (index) {
                        0 -> tip.copy(category = "not_a_real_category")
                        1 -> tip.copy(section = "not_a_real_section")
                        else -> tip
                    }
                }
            )
            val repository = createRepository(
                dataSource = FakeCatalogDataSource(malformedDto)
            )

            try {
                repository.getCatalog()
                fail("Expected IllegalStateException for malformed catalog content.")
            } catch (exception: IllegalStateException) {
                val message = exception.message.orEmpty()

                assertTrue(message.contains("Catalog validation failed."))
                assertTrue(message.contains("unknown category key", ignoreCase = true))
                assertTrue(message.contains("unknown section key", ignoreCase = true))
            }
        }
    }

    @Test
    fun `getCatalog propagates catalog data source read failure`() {
        runBlocking {
            val repository = createRepository(
                dataSource = FailingCatalogDataSource(
                    exception = IOException("Catalog source unavailable.")
                )
            )

            try {
                repository.getCatalog()
                fail("Expected IOException from catalog data source.")
            } catch (exception: IOException) {
                assertEquals("Catalog source unavailable.", exception.message)
            }
        }
    }

    private fun createRepository(
        dataSource: CatalogDataSource
    ): CatalogRepository {
        return CatalogRepositoryImpl(
            dataSource = dataSource,
            mapper = CatalogMapper(),
            validator = CatalogValidator()
        )
    }

    private class FakeCatalogDataSource(
        private val dto: CatalogDto
    ) : CatalogDataSource {

        override suspend fun readCatalog(): CatalogDto {
            return dto
        }
    }

    private class FailingCatalogDataSource(
        private val exception: IOException
    ) : CatalogDataSource {

        override suspend fun readCatalog(): CatalogDto {
            throw exception
        }
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
                    endDay = 6
                ),
                SectionDto(
                    key = "build_focus",
                    title = "Build Focus",
                    subtitle = "Section subtitle",
                    startDay = 7,
                    endDay = 12
                ),
                SectionDto(
                    key = "protect_boundaries",
                    title = "Protect Boundaries",
                    subtitle = "Section subtitle",
                    startDay = 13,
                    endDay = 18
                ),
                SectionDto(
                    key = "sustain_energy",
                    title = "Sustain Energy",
                    subtitle = "Section subtitle",
                    startDay = 19,
                    endDay = 24
                ),
                SectionDto(
                    key = "finish_and_improve",
                    title = "Finish and Improve",
                    subtitle = "Section subtitle",
                    startDay = 25,
                    endDay = 30
                )
            ),
            tips = (1..30).map(::validTipDto)
        )
    }

    private fun validTipDto(dayNumber: Int): TipDto {
        val dayLabel = dayNumber.toString().padStart(2, '0')

        return TipDto(
            id = "day_${dayLabel}_sample",
            dayNumber = dayNumber,
            section = sectionWireValueForDay(dayNumber),
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

    private fun sectionWireValueForDay(dayNumber: Int): String {
        return when (dayNumber) {
            in 1..6 -> "start_with_clarity"
            in 7..12 -> "build_focus"
            in 13..18 -> "protect_boundaries"
            in 19..24 -> "sustain_energy"
            in 25..30 -> "finish_and_improve"
            else -> error("Unsupported day number: $dayNumber")
        }
    }
}