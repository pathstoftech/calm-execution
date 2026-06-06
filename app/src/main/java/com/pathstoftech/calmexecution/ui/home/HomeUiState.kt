package com.pathstoftech.calmexecution.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.ui.AsyncStatus
import com.pathstoftech.calmexecution.core.ui.UiMessage
import com.pathstoftech.calmexecution.ui.detail.TipDetailUi

@Immutable
data class HomeUiState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val screenTitle: String = "30 Days of Calm Execution",
    val introText: String = "",
    val journey: JourneyProgressUi = JourneyProgressUi(),
    val selectedSection: SectionKey? = null,
    val bookmarkedOnly: Boolean = false,
    val sectionTabs: List<SectionTabUi> = emptyList(),
    val feedSections: List<HomeFeedSectionUi> = emptyList(),
    val featuredTipId: TipId? = null,
    val selectedTipId: TipId? = null,
    val selectedTipDetail: TipDetailUi? = null,
    val message: UiMessage? = null
)

@Immutable
data class JourneyProgressUi(
    val completedCount: Int = 0,
    val totalCount: Int = 30,
    val currentDay: Int? = null,
    val completionFraction: Float = 0f
)

@Immutable
data class SectionTabUi(
    val key: SectionKey,
    val title: String,
    val isSelected: Boolean,
    val completedCount: Int,
    val totalCount: Int
)

@Immutable
data class HomeFeedSectionUi(
    val key: SectionKey,
    val title: String,
    val items: List<TipCardUi>
)

@Immutable
data class TipCardUi(
    val id: TipId,
    val dayLabel: String,
    val title: String,
    val previewText: String,
    val categoryLabel: String,
    val imageKey: String,
    @param:DrawableRes val imageResId: Int? = null,
    val imageContentDescription: String? = null,
    val imageDecorative: Boolean = false,
    val isCompleted: Boolean,
    val isBookmarked: Boolean,
)