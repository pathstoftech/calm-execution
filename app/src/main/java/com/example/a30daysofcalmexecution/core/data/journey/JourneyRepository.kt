package com.example.a30daysofcalmexecution.core.data.journey

import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipUserState
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