package com.pathstoftech.calmexecution.core.data.journey

import com.pathstoftech.calmexecution.core.data.journey.proto.JourneyStateProto
import kotlinx.coroutines.flow.Flow

interface JourneyDataSource {
    val journeyState: Flow<JourneyStateProto>

    suspend fun updateJourneyState(
        transform: suspend (JourneyStateProto) -> JourneyStateProto
    ): JourneyStateProto
}