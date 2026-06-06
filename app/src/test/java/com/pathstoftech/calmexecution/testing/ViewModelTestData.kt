package com.pathstoftech.calmexecution.testing

import com.pathstoftech.calmexecution.core.model.JourneyCatalog
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.Tip
import com.pathstoftech.calmexecution.core.model.TipBody
import com.pathstoftech.calmexecution.core.model.TipCategoryKey
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.TipImageRef
import com.pathstoftech.calmexecution.core.model.TipSection

object ViewModelTestData {

    val DayOneTipId = TipId("day_01_define_real_priority")
    val DayTwoTipId = TipId("day_02_stop_planning_by_panic")
    val MissingTipId = TipId("missing_tip")

    fun catalog(): JourneyCatalog {
        val dayOne = tip(
            id = DayOneTipId,
            dayNumber = 1,
            title = "Define real priority",
            previewText = "Choose the one result that matters.",
            categoryKey = TipCategoryKey.PLANNING,
        )

        val dayTwo = tip(
            id = DayTwoTipId,
            dayNumber = 2,
            title = "Stop planning by panic",
            previewText = "Name the pressure before making the plan.",
            categoryKey = TipCategoryKey.AWARENESS,
        )

        return JourneyCatalog(
            title = "30 Days of Calm Execution",
            subtitle = "A calmer way to execute the day.",
            sections = listOf(
                TipSection(
                    key = SectionKey.START_WITH_CLARITY,
                    title = "Start with Clarity",
                    subtitle = null,
                    startDay = 1,
                    endDay = 2,
                    tips = listOf(dayOne, dayTwo),
                ),
            ),
        )
    }

    fun tip(
        id: TipId = DayOneTipId,
        dayNumber: Int = 1,
        title: String = "Define real priority",
        previewText: String = "Choose the one result that matters.",
        categoryKey: TipCategoryKey = TipCategoryKey.PLANNING,
    ): Tip =
        Tip(
            id = id,
            dayNumber = dayNumber,
            sectionKey = SectionKey.START_WITH_CLARITY,
            categoryKey = categoryKey,
            title = title,
            previewText = previewText,
            body = TipBody(
                problem = "Problem body",
                tip = "Tip body",
                whyItHelps = "Why it helps body",
                tryToday = "Try today body",
            ),
            image = TipImageRef(
                imageKey = id.value,
                contentDescription = "Image for $title",
                isDecorative = false,
            ),
        )
}