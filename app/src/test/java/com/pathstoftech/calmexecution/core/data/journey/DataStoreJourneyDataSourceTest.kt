package com.pathstoftech.calmexecution.core.data.journey

import androidx.datastore.core.DataStoreFactory
import com.pathstoftech.calmexecution.core.data.journey.proto.JourneyStateProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipUserStateProto
import java.io.File
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DataStoreJourneyDataSourceTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `journeyState emits default value before updates`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "default_journey_state.pb")

            val state = dataSource.journeyState.first()

            assertEquals(
                JourneyStateProto.getDefaultInstance(),
                state
            )
        }
    }

    @Test
    fun `updateJourneyState persists updated active tip id`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "active_tip_journey_state.pb")

            val updated = dataSource.updateJourneyState { current ->
                current.toBuilder()
                    .setActiveTipId("day_01_define_real_priority")
                    .build()
            }

            val restored = dataSource.journeyState.first()

            assertEquals("day_01_define_real_priority", updated.activeTipId)
            assertEquals(updated, restored)
        }
    }

    @Test
    fun `updateJourneyState persists tip user state`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "tip_user_state.pb")

            val updated = dataSource.updateJourneyState { current ->
                current.toBuilder()
                    .addTipStates(
                        TipUserStateProto.newBuilder()
                            .setTipId("day_01_define_real_priority")
                            .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                            .setIsBookmarked(true)
                            .setLastViewedAtEpochMillis(123L)
                            .build()
                    )
                    .build()
            }

            val restored = dataSource.journeyState.first()
            val restoredTipState = restored.tipStatesList.first()

            assertEquals(updated, restored)
            assertEquals("day_01_define_real_priority", restoredTipState.tipId)
            assertEquals(TipCompletionStatusProto.IN_PROGRESS, restoredTipState.completionStatus)
            assertEquals(true, restoredTipState.isBookmarked)
            assertEquals(123L, restoredTipState.lastViewedAtEpochMillis)
        }
    }

    private fun createDataSource(fileName: String): JourneyDataSource {
        val dataStore = DataStoreFactory.create(
            serializer = JourneyStateSerializer(),
            produceFile = {
                File(temporaryFolder.root, fileName)
            }
        )

        return DataStoreJourneyDataSource(
            dataStore = dataStore
        )
    }
}