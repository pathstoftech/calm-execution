package com.pathstoftech.calmexecution.core.data.journey

import com.pathstoftech.calmexecution.core.model.JourneyUserState
import com.pathstoftech.calmexecution.core.model.TipCompletionStatus
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.TipUserState
import kotlinx.coroutines.flow.Flow

interface JourneyRepository {
    fun observeJourneyState(): Flow<JourneyUserState>

    fun observeTipState(tipId: TipId): Flow<TipUserState>

    suspend fun markViewed(tipId: TipId)

    suspend fun setBookmarked(
        tipId: TipId,
        bookmarked: Boolean
    )

    suspend fun setCompletionStatus(
        tipId: TipId,
        status: TipCompletionStatus
    )

    suspend fun resetProgress()
}