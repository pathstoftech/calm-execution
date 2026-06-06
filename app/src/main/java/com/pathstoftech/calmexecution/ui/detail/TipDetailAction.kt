package com.pathstoftech.calmexecution.ui.detail

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TipDetailAction {
    data object ToggleBookmark: TipDetailAction
    data object ToggleCompleted: TipDetailAction
    data object RetryLoad: TipDetailAction
    data object DismissMessage: TipDetailAction
}