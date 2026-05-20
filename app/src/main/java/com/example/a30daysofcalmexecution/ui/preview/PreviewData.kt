package com.example.a30daysofcalmexecution.ui.preview

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import com.example.a30daysofcalmexecution.ui.detail.TipDetailTextSectionUi
import com.example.a30daysofcalmexecution.ui.detail.TipDetailUi
import com.example.a30daysofcalmexecution.ui.home.HomeFeedSectionUi
import com.example.a30daysofcalmexecution.ui.home.JourneyProgressUi
import com.example.a30daysofcalmexecution.ui.home.SectionTabUi
import com.example.a30daysofcalmexecution.ui.home.TipCardUi

object PreviewData {
    val SectionStartWithClarity = SectionKey.START_WITH_CLARITY
    val SectionBuildFocus = SectionKey.BUILD_FOCUS
    val SectionFinishAndImprove = SectionKey.FINISH_AND_IMPROVE

    val DayOneTipId = TipId("day_01_define_real_priority")
    val DayTwoTipId = TipId("day_02_reduce_open_loops")
    val DayThreeTipId = TipId("day_03_start_before_ready")

    val JourneyProgress =
        JourneyProgressUi(
            completedCount = 7,
            totalCount = 30,
            currentDay = 8,
            completionFraction = 7f / 30f,
        )

    val SectionTabs =
        listOf(
            SectionTabUi(
                key = SectionStartWithClarity,
                title = SectionStartWithClarity.title,
                isSelected = true,
                completedCount = 3,
                totalCount = 7,
            ),
            SectionTabUi(
                key = SectionBuildFocus,
                title = SectionBuildFocus.title,
                isSelected = false,
                completedCount = 2,
                totalCount = 8,
            ),
            SectionTabUi(
                key = SectionFinishAndImprove,
                title = SectionFinishAndImprove.title,
                isSelected = false,
                completedCount = 2,
                totalCount = 15,
            ),
        )

    val DayOneCard =
        TipCardUi(
            id = DayOneTipId,
            dayLabel = "Day 01",
            title = "Define the real priority",
            previewText = "Separate the useful task from the loud task before the day starts.",
            categoryLabel = "Priority",
            imageKey = "tip_01_define_real_priority",
            imageContentDescription = "A calm editorial illustration for this tip.",
            imageDecorative = false,
            isCompleted = true,
            isBookmarked = true,
        )

    val DayTwoCard =
        TipCardUi(
            id = DayTwoTipId,
            dayLabel = "Day 02",
            title = "Reduce open loops",
            previewText = "Close or park small unfinished decisions before they become background noise.",
            categoryLabel = "Clarity",
            imageKey = "tip_02_reduce_open_loops",
            imageContentDescription = "A calm editorial illustration for this tip.",
            imageDecorative = false,
            isCompleted = false,
            isBookmarked = false,
        )

    val DayThreeCard =
        TipCardUi(
            id = DayThreeTipId,
            dayLabel = "Day 03",
            title = "Start before ready",
            previewText = "Use a small first action to break the waiting loop.",
            categoryLabel = "Execution",
            imageKey = "tip_03_start_before_ready",
            imageContentDescription = "A calm editorial illustration for this tip.",
            imageDecorative = false,
            isCompleted = false,
            isBookmarked = true,
        )

    val StartWithClarityFeedSection =
        HomeFeedSectionUi(
            key = SectionStartWithClarity,
            title = SectionStartWithClarity.title,
            items = listOf(
                DayOneCard,
                DayTwoCard,
                DayThreeCard,
            ),
        )

    val FeedSections =
        listOf(
            StartWithClarityFeedSection,
        )

    val DayOneDetail =
        TipDetailUi(
            id = DayOneTipId,
            dayLabel = "Day 01",
            title = "Define the real priority",
            categoryLabel = "Priority",
            imageKey = "tip_01_define_real_priority",
            imageContentDescription = "A calm desk with one clear task card selected.",
            imageDecorative = false,
            problem = TipDetailTextSectionUi(
                title = "Problem",
                body = "A day can feel busy while the important work remains untouched.",
            ),
            recommendation = TipDetailTextSectionUi(
                title = "Tip",
                body = "Choose one real priority before checking secondary inputs.",
            ),
            whyItHelps = TipDetailTextSectionUi(
                title = "Why it helps",
                body = "A visible priority reduces decision churn and makes progress measurable.",
            ),
            tryToday = TipDetailTextSectionUi(
                title = "Try today",
                body = "Write one sentence: today counts if this one thing moves forward.",
            ),
            isBookmarked = true,
            completionStatus = TipCompletionStatus.COMPLETED,
        )

    val DayTwoDetail =
        TipDetailUi(
            id = DayTwoTipId,
            dayLabel = "Day 02",
            title = "Reduce open loops",
            categoryLabel = "Clarity",
            imageKey = "tip_02_reduce_open_loops",
            imageContentDescription = "A notebook with several small tasks grouped into one list.",
            imageDecorative = false,
            problem = TipDetailTextSectionUi(
                title = "Problem",
                body = "Unfinished small decisions quietly consume attention.",
            ),
            recommendation = TipDetailTextSectionUi(
                title = "Tip",
                body = "Capture every open loop, then close, schedule, delegate, or delete it.",
            ),
            whyItHelps = TipDetailTextSectionUi(
                title = "Why it helps",
                body = "A closed loop stops competing with the work that matters.",
            ),
            tryToday = TipDetailTextSectionUi(
                title = "Try today",
                body = "Spend five minutes listing loose ends and resolving just three.",
            ),
            isBookmarked = false,
            completionStatus = TipCompletionStatus.NOT_STARTED,
        )

    val DefaultPreferences =
        UserPreferences(
            themeMode = ThemeMode.SYSTEM,
            dynamicColorEnabled = false,
            reducedMotionEnabled = false,
            lastSelectedSectionKey = SectionStartWithClarity,
            hasSeenIntro = true,
        )

    val DarkReducedMotionPreferences =
        UserPreferences(
            themeMode = ThemeMode.DARK,
            dynamicColorEnabled = false,
            reducedMotionEnabled = true,
            lastSelectedSectionKey = SectionStartWithClarity,
            hasSeenIntro = true,
        )
}