package com.example.a30daysofcalmexecution.core.data.journey

import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipUserStateProto
import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipUserState
import javax.inject.Inject

class JourneyStateMapper @Inject constructor() {
    fun toDomain(proto: JourneyStateProto): JourneyUserState {
        val activeTipId = proto.activeTipId
            .takeIf { it.isNotBlank() }
            ?.let(::TipId)

        val tipStates = proto.tipStatesList
            .mapNotNull(::toDomainTipStateOrNull)
            .associateBy { it.tipId }

        return JourneyUserState(
            activeTipId = activeTipId,
            tipStates = tipStates
        )
    }

    fun toProto(domain: JourneyUserState): JourneyStateProto {
        val builder = JourneyStateProto.newBuilder()

        domain.activeTipId?.let { activeTipId ->
            builder.activeTipId = activeTipId.value
        }

        domain.tipStates.values
            .sortedBy { it.tipId.value }
            .map(::toProtoTipState)
            .forEach(builder::addTipStates)
        return builder.build()
    }

    private fun toDomainTipStateOrNull(proto: TipUserStateProto): TipUserState? {
        val tipIdValue = proto.tipId.takeIf { it.isNotBlank() } ?: return null
        val completionStatus = proto.completionStatus.toDomain()
        val completedAtEpochMillis = proto.completedAtEpochMillis
            .takeIf { it > 0L && completionStatus == TipCompletionStatus.COMPLETED }

        return TipUserState(
            tipId = TipId(tipIdValue),
            isBookmarked = proto.isBookmarked,
            completionStatus = completionStatus,
            lastViewedAtEpochMillis = proto.lastViewedAtEpochMillis.takeIf { it > 0L },
            completedAtEpochMillis = completedAtEpochMillis
        )
    }

    private fun toProtoTipState(domain: TipUserState): TipUserStateProto {
        return TipUserStateProto.newBuilder()
            .setTipId(domain.tipId.value)
            .setCompletionStatus(domain.completionStatus.toProto())
            .setIsBookmarked(domain.isBookmarked)
            .setLastViewedAtEpochMillis(domain.lastViewedAtEpochMillis ?: 0L)
            .setCompletedAtEpochMillis(domain.completedAtEpochMillis ?: 0L)
            .build()
    }

    private fun TipCompletionStatusProto.toDomain(): TipCompletionStatus {
        return when (this) {
            TipCompletionStatusProto.COMPLETED -> TipCompletionStatus.COMPLETED
            TipCompletionStatusProto.IN_PROGRESS -> TipCompletionStatus.IN_PROGRESS
            TipCompletionStatusProto.NOT_STARTED,
                TipCompletionStatusProto.TIP_COMPLETION_STATUS_UNSPECIFIED,
                TipCompletionStatusProto.UNRECOGNIZED -> TipCompletionStatus.NOT_STARTED
        }
    }

    private fun TipCompletionStatus.toProto(): TipCompletionStatusProto {
        return when (this) {
            TipCompletionStatus.NOT_STARTED -> TipCompletionStatusProto.NOT_STARTED
            TipCompletionStatus.IN_PROGRESS -> TipCompletionStatusProto.IN_PROGRESS
            TipCompletionStatus.COMPLETED -> TipCompletionStatusProto.COMPLETED
        }
    }
}