package com.example.a30daysofcalmexecution.core.model

data class JourneyUserState(
    val activeTipId: TipId? = null,
    val tipStates: Map<TipId, TipUserState> = emptyMap()
)
