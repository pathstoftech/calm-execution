package com.pathstoftech.calmexecution.core.ui

import androidx.compose.runtime.Immutable

enum class AsyncStatus {
    IDLE,
    LOADING,
    READY,
    ERROR
}

@Immutable
data class UiMessage(
    val id: Long,
    val text: String
)