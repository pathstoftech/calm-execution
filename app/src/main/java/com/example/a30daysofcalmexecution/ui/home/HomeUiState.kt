package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.runtime.Immutable
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.UiMessage
import com.example.a30daysofcalmexecution.ui.detail.TipDetailUi

@Immutable
data class HomeUiState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val screenTitle: String = "30 Days of Calm Execution",
    val introText: String = "",
    val journey: JourneyProgressUi = JourneyProgressUi(),
    val selectedSection: SectionKey? = null,
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
    val isCompleted: Boolean,
    val isBookmarked: Boolean
)