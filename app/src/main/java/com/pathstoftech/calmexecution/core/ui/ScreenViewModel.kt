package com.pathstoftech.calmexecution.core.ui

import kotlinx.coroutines.flow.StateFlow

interface ScreenViewModel<S,A> {
    val uiState: StateFlow<S>

    fun onAction(action: A)
}