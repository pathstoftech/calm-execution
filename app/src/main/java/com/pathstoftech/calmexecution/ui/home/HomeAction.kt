package com.pathstoftech.calmexecution.ui.home

import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.TipId

sealed interface HomeAction {
    data class SelectSection(
        val section: SectionKey?
    ) : HomeAction

    data class SetBookmarkedFilter(
        val enabled: Boolean
    ) : HomeAction

    data class OpenTip(
        val tipId: TipId
    ) : HomeAction

    data class ToggleBookmark(
        val tipId: TipId
    ) : HomeAction

    data class SelectExpandedDetail(
        val tipId: TipId?
    ) : HomeAction

    data object OpenSettings : HomeAction

    data object RetryLoad : HomeAction

    data object DismissMessage : HomeAction
}