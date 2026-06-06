package com.pathstoftech.calmexecution.ui.detail

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.pathstoftech.calmexecution.core.model.TipCompletionStatus
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.ui.AsyncStatus
import com.pathstoftech.calmexecution.core.ui.UiMessage

@Immutable
data class TipDetailUiState(
    val status: AsyncStatus = AsyncStatus.IDLE,
    val screenTitle: String = "Tip detail",
    val tip: TipDetailUi? = null,
    val message: UiMessage? = null,
)

@Immutable
data class TipDetailUi(
    val id: TipId,
    val dayLabel: String,
    val title: String,
    val categoryLabel: String,
    val imageKey: String,
    @param:DrawableRes val imageResId: Int? = null,
    val imageContentDescription: String?,
    val imageDecorative: Boolean,
    val problem: TipDetailTextSectionUi,
    val recommendation: TipDetailTextSectionUi,
    val whyItHelps: TipDetailTextSectionUi,
    val tryToday: TipDetailTextSectionUi,
    val isBookmarked: Boolean,
    val completionStatus: TipCompletionStatus,
) {
    val isCompleted: Boolean
        get() = completionStatus == TipCompletionStatus.COMPLETED
}

@Immutable
data class TipDetailTextSectionUi(
    val title: String,
    val body: String,
)