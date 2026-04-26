package com.example.a30daysofcalmexecution.core.data.journey

import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipUserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JourneyRepositoryImpl @Inject constructor(
    private val dataSource: JourneyDataSource,
    private val mapper: JourneyStateMapper
) : JourneyRepository {
    override fun observeJourneyState(): Flow<JourneyUserState> {
        return dataSource.journeyState
            .map(mapper::toDomain)
            .distinctUntilChanged()
    }

    override fun observeTipState(tipId: TipId): Flow<TipUserState> {
        return observeJourneyState()
            .map { journeyState ->
                journeyState.tipStates[tipId] ?: TipUserState(tipId = tipId)
            }
            .distinctUntilChanged()
    }

    override suspend fun markViewed(tipId: TipId) {
        val now = currentTimeMillis()

        updateDomainState { current ->
            current
                .copy(activeTipId = tipId)
                .updateTipState(tipId) { tipState ->
                    tipState.copy(
                        lastViewedAtEpochMillis = now
                    )
                }
        }
    }

    override suspend fun setBookmarked(tipId: TipId, bookmarked: Boolean) {
        updateDomainState { current ->
            current.updateTipState(tipId) { tipState ->
                tipState.copy(
                    isBookmarked = bookmarked
                )
            }
        }
    }

    override suspend fun setCompletionStatus(tipId: TipId, status: TipCompletionStatus) {
        val now = currentTimeMillis()

        updateDomainState { current ->
            current.updateTipState(tipId) { tipState ->
                val completedAtEpochMillis = when (status) {
                    TipCompletionStatus.COMPLETED -> tipState.completedAtEpochMillis ?: now
                    TipCompletionStatus.NOT_STARTED,
                    TipCompletionStatus.IN_PROGRESS -> null
                }

                tipState.copy(
                    completionStatus = status,
                    completedAtEpochMillis = completedAtEpochMillis
                )
            }
        }
    }

    override suspend fun resetProgress() {
        updateDomainState {
            JourneyUserState()
        }
    }

    private suspend fun updateDomainState(
        transform: suspend (JourneyUserState) -> JourneyUserState
    ): JourneyUserState {
        val updatedProto = dataSource.updateJourneyState { currentProto ->
            val currentDomain = mapper.toDomain(currentProto)
            val updatedDomain = transform(currentDomain)

            mapper.toProto(updatedDomain)
        }

        return mapper.toDomain(updatedProto)
    }

    private fun JourneyUserState.updateTipState(
        tipId: TipId,
        transform: (TipUserState) -> TipUserState
    ) : JourneyUserState {
        val currentTipState = tipStates[tipId] ?: TipUserState(tipId = tipId)
        val updatedTipState = transform(currentTipState)

        return copy(tipStates = tipStates + (tipId to updatedTipState))
    }

    private fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}