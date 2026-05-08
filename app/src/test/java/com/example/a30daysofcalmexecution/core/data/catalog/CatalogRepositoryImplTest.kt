package com.example.a30daysofcalmexecution.core.data.catalog

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipCategoryKey
import com.example.a30daysofcalmexecution.core.model.TipId
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogRepositoryImplTest {

    @Test
    fun `getCatalog returns mapped catalog from data source`() {
        runBlocking {
            val dataSource = FakeCatalogDataSource(validCatalogDto())
            val repository = createRepository(dataSource)

            val catalog = repository.getCatalog()

            assertEquals("30 Days of Calm Execution", catalog.title)
            assertEquals(
                "Build calmer, more intentional work habits in 30 days.",
                catalog.subtitle
            )
            assertEquals(5, catalog.sections.size)
            assertEquals(30, catalog.allTips.size)

            assertEquals(SectionKey.START_WITH_CLARITY, catalog.sections.first().key)
            assertEquals(TipId("day_01_sample"), catalog.allTips.first().id)
            assertEquals(TipId("day_30_sample"), catalog.allTips.last().id)
        }
    }

    @Test
    fun `getCatalog reads data source only once because catalog is cached`() {
        runBlocking {
            val dataSource = FakeCatalogDataSource(validCatalogDto())
            val repository = createRepository(dataSource)

            repository.getCatalog()
            repository.getCatalog()
            repository.getTip(TipId("day_01_sample"))
            repository.getSection(SectionKey.BUILD_FOCUS)

            assertEquals(1, dataSource.readCount)
        }
    }

    @Test
    fun `getCatalog sorts tips by day number within sections`() {
        runBlocking {
            val dto = validCatalogDto().copy(
                tips = validCatalogDto().tips.reversed()
            )
            val dataSource = FakeCatalogDataSource(dto)
            val repository = createRepository(dataSource)

            val catalog = repository.getCatalog()

            val dayNumbers = catalog.allTips.map { it.dayNumber }

            assertEquals((1..30).toList(), dayNumbers)
        }
    }

    @Test
    fun `getTip returns matching tip by id`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val tip = repository.getTip(TipId("day_07_sample"))

            assertEquals(TipId("day_07_sample"), tip?.id)
            assertEquals(7, tip?.dayNumber)
            assertEquals(SectionKey.BUILD_FOCUS, tip?.sectionKey)
            assertEquals(TipCategoryKey.PLANNING, tip?.categoryKey)
            assertEquals("Sample title 7", tip?.title)
        }
    }

    @Test
    fun `getTip returns null for missing tip id`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val tip = repository.getTip(TipId("missing_tip"))

            assertNull(tip)
        }
    }

    @Test
    fun `getSection returns matching section`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val section = repository.getSection(SectionKey.PROTECT_BOUNDARIES)

            assertEquals(SectionKey.PROTECT_BOUNDARIES, section?.key)
            assertEquals("Protect Boundaries", section?.title)
            assertEquals(6, section?.tips?.size)
            assertEquals(13, section?.tips?.first()?.dayNumber)
            assertEquals(18, section?.tips?.last()?.dayNumber)
        }
    }

    @Test
    fun `getTipsForSection returns tips for requested section`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val tips = repository.getTipsForSection(SectionKey.SUSTAIN_ENERGY)

            assertEquals(6, tips.size)
            assertEquals((19..24).toList(), tips.map { it.dayNumber })
            assertTrue(tips.all { it.sectionKey == SectionKey.SUSTAIN_ENERGY })
        }
    }

    @Test
    fun `getAdjacentTipIds returns only next for first tip`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val adjacent = repository.getAdjacentTipIds(TipId("day_01_sample"))

            assertNull(adjacent.previous)
            assertEquals(TipId("day_02_sample"), adjacent.next)
        }
    }

    @Test
    fun `getAdjacentTipIds returns previous and next for middle tip`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val adjacent = repository.getAdjacentTipIds(TipId("day_15_sample"))

            assertEquals(TipId("day_14_sample"), adjacent.previous)
            assertEquals(TipId("day_16_sample"), adjacent.next)
        }
    }

    @Test
    fun `getAdjacentTipIds returns only previous for last tip`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val adjacent = repository.getAdjacentTipIds(TipId("day_30_sample"))

            assertEquals(TipId("day_29_sample"), adjacent.previous)
            assertNull(adjacent.next)
        }
    }

    @Test
    fun `getAdjacentTipIds returns null ids for missing tip`() {
        runBlocking {
            val repository = createRepository(
                FakeCatalogDataSource(validCatalogDto())
            )

            val adjacent = repository.getAdjacentTipIds(TipId("missing_tip"))

            assertNull(adjacent.previous)
            assertNull(adjacent.next)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `getCatalog throws when catalog validation fails`() {
        runBlocking {
            val invalidDto = validCatalogDto().copy(
                tips = validCatalogDto().tips.dropLast(1)
            )
            val repository = createRepository(
                FakeCatalogDataSource(invalidDto)
            )

            repository.getCatalog()
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

        var readCount: Int = 0
            private set

        override suspend fun readCatalog(): CatalogDto {
            readCount += 1
            return dto
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
            tips = (1..30).map { dayNumber ->
                validTipDto(dayNumber)
            }
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