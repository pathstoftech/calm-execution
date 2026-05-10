package com.example.a30daysofcalmexecution.core.ui

import kotlinx.coroutines.flow.StateFlow

interface ScreenViewModel<S,A> {
    val uiState: StateFlow<S>

    fun onAction(action: A)
}