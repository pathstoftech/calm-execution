package com.example.a30daysofcalmexecution.ui.preview

import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.UiMessage
import com.example.a30daysofcalmexecution.ui.detail.TipDetailUiState
import com.example.a30daysofcalmexecution.ui.home.HomeUiState
import com.example.a30daysofcalmexecution.ui.settings.SettingsUiState

object PreviewUiStates {
    val HomeReady =
        HomeUiState(
            status = AsyncStatus.READY,
            screenTitle = "30 Days of Calm Execution",
            introText = "A practical journey for focused, sustainable execution.",
            journey = PreviewData.JourneyProgress,
            selectedSection = PreviewData.SectionStartWithClarity,
            sectionTabs = PreviewData.SectionTabs,
            feedSections = PreviewData.FeedSections,
            featuredTipId = PreviewData.DayTwoTipId,
            selectedTipId = null,
            selectedTipDetail = null,
            message = null,
        )

    val HomeReadyWithSelectedDetail =
        HomeReady.copy(
            selectedTipId = PreviewData.DayTwoTipId,
            selectedTipDetail = PreviewData.DayTwoDetail,
        )

    val HomeLoading =
        HomeUiState(
            status = AsyncStatus.LOADING,
            screenTitle = "30 Days of Calm Execution",
        )

    val HomeError =
        HomeUiState(
            status = AsyncStatus.ERROR,
            screenTitle = "30 Days of Calm Execution",
            message = PreviewMessages.HomeLoadFailed,
        )

    val HomeEmptyFiltered =
        HomeReady.copy(
            selectedSection = PreviewData.SectionBuildFocus,
            sectionTabs = PreviewData.SectionTabs.map { tab ->
                tab.copy(
                    isSelected = tab.key == PreviewData.SectionBuildFocus,
                )
            },
            feedSections = emptyList(),
            featuredTipId = PreviewData.DayTwoTipId,
            selectedTipId = null,
            selectedTipDetail = null,
        )

    val DetailReady =
        TipDetailUiState(
            status = AsyncStatus.READY,
            screenTitle = PreviewData.DayOneDetail.title,
            tip = PreviewData.DayOneDetail,
            message = null,
        )

    val DetailReadyBookmarkedCompleted =
        TipDetailUiState(
            status = AsyncStatus.READY,
            screenTitle = PreviewData.DayOneDetail.title,
            tip = PreviewData.DayOneDetail,
            message = null,
        )

    val DetailLoading =
        TipDetailUiState(
            status = AsyncStatus.LOADING,
            screenTitle = "Loading tip",
            tip = null,
            message = null,
        )

    val DetailError =
        TipDetailUiState(
            status = AsyncStatus.ERROR,
            screenTitle = "Tip unavailable",
            tip = null,
            message = PreviewMessages.DetailLoadFailed,
        )

    val SettingsReady =
        SettingsUiState(
            status = AsyncStatus.READY,
            screenTitle = "Settings",
            themeMode = ThemeMode.SYSTEM,
            dynamicColorEnabled = false,
            reducedMotionEnabled = false,
            showResetProgressDialog = false,
            message = null,
        )

    val SettingsDarkReducedMotion =
        SettingsReady.copy(
            themeMode = ThemeMode.DARK,
            dynamicColorEnabled = false,
            reducedMotionEnabled = true,
        )

    val SettingsResetDialog =
        SettingsReady.copy(
            showResetProgressDialog = true,
        )

    val SettingsLoading =
        SettingsUiState(
            status = AsyncStatus.LOADING,
            screenTitle = "Settings",
        )

    val SettingsError =
        SettingsUiState(
            status = AsyncStatus.ERROR,
            screenTitle = "Settings",
            message = PreviewMessages.SettingsLoadFailed,
        )
}

object PreviewMessages {
    val HomeLoadFailed =
        UiMessage(
            id = 1L,
            text = "Unable to load journey content.",
        )

    val DetailLoadFailed =
        UiMessage(
            id = 2L,
            text = "This tip could not be loaded.",
        )

    val SettingsLoadFailed =
        UiMessage(
            id = 3L,
            text = "Unable to load settings.",
        )
}