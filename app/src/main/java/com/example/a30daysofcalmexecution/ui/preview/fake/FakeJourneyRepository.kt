package com.example.a30daysofcalmexecution.ui.preview.fake

import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipUserState
import com.example.a30daysofcalmexecution.ui.preview.PreviewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeJourneyRepository(
    initialState: JourneyUserState = PreviewJourneyUserState,
) : JourneyRepository {

    private val journeyState = MutableStateFlow(initialState)

    override fun observeJourneyState(): Flow<JourneyUserState> =
        journeyState

    override fun observeTipState(tipId: TipId): Flow<TipUserState> =
        journeyState.map { state ->
            state.tipStates[tipId] ?: TipUserState(tipId = tipId)
        }

    override suspend fun markViewed(tipId: TipId) {
        journeyState.update { current ->
            val existing = current.tipStates[tipId] ?: TipUserState(tipId = tipId)
            val updated = existing.copy(
                lastViewedAtEpochMillis = PreviewTimestampMillis,
            )

            current.copy(
                activeTipId = tipId,
                tipStates = current.tipStates + (tipId to updated),
            )
        }
    }

    override suspend fun setBookmarked(
        tipId: TipId,
        bookmarked: Boolean,
    ) {
        journeyState.update { current ->
            val existing = current.tipStates[tipId] ?: TipUserState(tipId = tipId)
            val updated = existing.copy(
                isBookmarked = bookmarked,
            )

            current.copy(
                tipStates = current.tipStates + (tipId to updated),
            )
        }
    }

    override suspend fun setCompletionStatus(
        tipId: TipId,
        status: TipCompletionStatus,
    ) {
        journeyState.update { current ->
            val existing = current.tipStates[tipId] ?: TipUserState(tipId = tipId)
            val updated = existing.copy(
                completionStatus = status,
                completedAtEpochMillis = if (status == TipCompletionStatus.COMPLETED) {
                    PreviewTimestampMillis
                } else {
                    null
                },
            )

            current.copy(
                tipStates = current.tipStates + (tipId to updated),
            )
        }
    }

    override suspend fun resetProgress() {
        journeyState.value = JourneyUserState()
    }
}

val PreviewJourneyUserState =
    JourneyUserState(
        activeTipId = PreviewData.DayTwoTipId,
        tipStates = mapOf(
            PreviewData.DayOneTipId to TipUserState(
                tipId = PreviewData.DayOneTipId,
                isBookmarked = true,
                completionStatus = TipCompletionStatus.COMPLETED,
                lastViewedAtEpochMillis = PreviewTimestampMillis,
                completedAtEpochMillis = PreviewTimestampMillis,
            ),
            PreviewData.DayTwoTipId to TipUserState(
                tipId = PreviewData.DayTwoTipId,
                isBookmarked = false,
                completionStatus = TipCompletionStatus.IN_PROGRESS,
                lastViewedAtEpochMillis = PreviewTimestampMillis,
            ),
            PreviewData.DayThreeTipId to TipUserState(
                tipId = PreviewData.DayThreeTipId,
                isBookmarked = true,
                completionStatus = TipCompletionStatus.NOT_STARTED,
            ),
        ),
    )

private const val PreviewTimestampMillis = 1_700_000_000_000L