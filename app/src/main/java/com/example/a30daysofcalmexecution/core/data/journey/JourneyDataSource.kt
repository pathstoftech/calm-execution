package com.example.a30daysofcalmexecution.core.data.journey

import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto
import kotlinx.coroutines.flow.Flow

interface JourneyDataSource {
    val journeyState: Flow<JourneyStateProto>

    suspend fun updateJourneyState(
        transform: suspend (JourneyStateProto) -> JourneyStateProto
    ): JourneyStateProto
}