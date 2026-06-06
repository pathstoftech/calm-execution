package com.pathstoftech.calmexecution.core.data.journey

import androidx.datastore.core.DataStore
import com.pathstoftech.calmexecution.core.data.journey.proto.JourneyStateProto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreJourneyDataSource @Inject constructor(
    private val dataStore: DataStore<JourneyStateProto>
) : JourneyDataSource {
    override val journeyState: Flow<JourneyStateProto>
        get() = dataStore.data

    override suspend fun updateJourneyState(
        transform: suspend (JourneyStateProto) -> JourneyStateProto
    ): JourneyStateProto {
        return dataStore.updateData(transform)
    }
}