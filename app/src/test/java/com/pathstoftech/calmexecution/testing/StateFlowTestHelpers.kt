package com.pathstoftech.calmexecution.testing

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope

fun TestScope.collectStateFlow(
    stateFlow: StateFlow<*>,
): Job =
    launch {
        stateFlow.collect {
            // Keep the StateFlow active during ViewModel tests.
        }
    }