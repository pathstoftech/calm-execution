package com.pathstoftech.calmexecution.ui.preview.fake

import com.pathstoftech.calmexecution.core.data.catalog.AdjacentTipIds
import com.pathstoftech.calmexecution.core.data.catalog.CatalogRepository
import com.pathstoftech.calmexecution.core.model.JourneyCatalog
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.Tip
import com.pathstoftech.calmexecution.core.model.TipBody
import com.pathstoftech.calmexecution.core.model.TipCategoryKey
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.TipImageRef
import com.pathstoftech.calmexecution.core.model.TipSection
import com.pathstoftech.calmexecution.ui.preview.PreviewData

class FakeCatalogRepository(
    private val catalog: JourneyCatalog = PreviewJourneyCatalog,
) : CatalogRepository {

    override suspend fun getCatalog(): JourneyCatalog =
        catalog

    override suspend fun getTip(tipId: TipId): Tip? =
        catalog.allTips.firstOrNull { tip -> tip.id == tipId }

    override suspend fun getSection(sectionKey: SectionKey): TipSection? =
        catalog.sections.firstOrNull { section -> section.key == sectionKey }

    override suspend fun getTipsForSection(sectionKey: SectionKey): List<Tip> =
        getSection(sectionKey)?.tips.orEmpty()

    override suspend fun getAdjacentTipIds(tipId: TipId): AdjacentTipIds {
        val tips = catalog.allTips
        val index = tips.indexOfFirst { tip -> tip.id == tipId }

        if (index == -1) {
            return AdjacentTipIds(
                previous = null,
                next = null,
            )
        }

        return AdjacentTipIds(
            previous = tips.getOrNull(index - 1)?.id,
            next = tips.getOrNull(index + 1)?.id,
        )
    }
}

val PreviewDayOneTip =
    Tip(
        id = PreviewData.DayOneTipId,
        dayNumber = 1,
        sectionKey = SectionKey.START_WITH_CLARITY,
        categoryKey = TipCategoryKey.DECISION_MAKING,
        title = "Define the real priority",
        previewText = "Separate the useful task from the loud task before the day starts.",
        body = TipBody(
            problem = "A day can feel busy while the important work remains untouched.",
            tip = "Choose one real priority before checking secondary inputs.",
            whyItHelps = "A visible priority reduces decision churn and makes progress measurable.",
            tryToday = "Write one sentence: today counts if this one thing moves forward.",
        ),
        image = TipImageRef(
            imageKey = "tip_01_define_real_priority",
            contentDescription = "A calm desk with one clear task card selected.",
            isDecorative = false,
        ),
    )

val PreviewDayTwoTip =
    Tip(
        id = PreviewData.DayTwoTipId,
        dayNumber = 2,
        sectionKey = SectionKey.START_WITH_CLARITY,
        categoryKey = TipCategoryKey.AWARENESS,
        title = "Reduce open loops",
        previewText = "Close or park small unfinished decisions before they become background noise.",
        body = TipBody(
            problem = "Unfinished small decisions quietly consume attention.",
            tip = "Capture every open loop, then close, schedule, delegate, or delete it.",
            whyItHelps = "A closed loop stops competing with the work that matters.",
            tryToday = "Spend five minutes listing loose ends and resolving just three.",
        ),
        image = TipImageRef(
            imageKey = "tip_02_reduce_open_loops",
            contentDescription = "A notebook with several small tasks grouped into one list.",
            isDecorative = false,
        ),
    )

val PreviewDayThreeTip =
    Tip(
        id = PreviewData.DayThreeTipId,
        dayNumber = 3,
        sectionKey = SectionKey.START_WITH_CLARITY,
        categoryKey = TipCategoryKey.EXECUTION,
        title = "Start before ready",
        previewText = "Use a small first action to break the waiting loop.",
        body = TipBody(
            problem = "Waiting for perfect readiness delays useful movement.",
            tip = "Start with a safe first action that takes less than five minutes.",
            whyItHelps = "Small starts reduce resistance and create evidence that the task can move.",
            tryToday = "Open the task and complete only the first visible step.",
        ),
        image = TipImageRef(
            imageKey = "tip_03_start_before_ready",
            contentDescription = "A simple workspace with a timer and one small task note.",
            isDecorative = false,
        ),
    )

val PreviewJourneyCatalog =
    JourneyCatalog(
        title = "30 Days of Calm Execution",
        subtitle = "A practical journey for focused, sustainable execution.",
        sections = listOf(
            TipSection(
                key = SectionKey.START_WITH_CLARITY,
                title = SectionKey.START_WITH_CLARITY.title,
                subtitle = "Clarify what matters before momentum starts pulling sideways.",
                startDay = 1,
                endDay = 3,
                tips = listOf(
                    PreviewDayOneTip,
                    PreviewDayTwoTip,
                    PreviewDayThreeTip,
                ),
            ),
        ),
    )