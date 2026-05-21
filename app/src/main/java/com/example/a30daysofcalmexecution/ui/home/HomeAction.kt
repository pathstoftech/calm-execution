package com.example.a30daysofcalmexecution.ui.home

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipId

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

    data class ToggleCompleted(
        val tipId: TipId
    ) : HomeAction

    data class SelectExpandedDetail(
        val tipId: TipId?
    ) : HomeAction

    data object OpenSettings : HomeAction

    data object RetryLoad : HomeAction

    data object DismissMessage : HomeAction
}