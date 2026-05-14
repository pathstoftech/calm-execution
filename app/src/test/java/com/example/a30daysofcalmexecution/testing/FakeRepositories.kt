package com.example.a30daysofcalmexecution.testing

import com.example.a30daysofcalmexecution.core.data.catalog.AdjacentTipIds
import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesRepository
import com.example.a30daysofcalmexecution.core.model.JourneyCatalog
import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.Tip
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipSection
import com.example.a30daysofcalmexecution.core.model.TipUserState
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCatalogRepository(
    private var catalog: JourneyCatalog? = ViewModelTestData.catalog(),
) : CatalogRepository {

    var shouldThrowOnCatalogLoad: Boolean = false
    var shouldThrowOnTipLoad: Boolean = false

    override suspend fun getCatalog(): JourneyCatalog {
        if (shouldThrowOnCatalogLoad) {
            error("Catalog load failed.")
        }

        return catalog ?: error("Catalog is missing.")
    }

    override suspend fun getTip(tipId: TipId): Tip? {
        if (shouldThrowOnTipLoad) {
            error("Tip load failed.")
        }

        return catalog
            ?.allTips
            ?.firstOrNull { tip -> tip.id == tipId }
    }

    override suspend fun getSection(sectionKey: SectionKey): TipSection? =
        catalog
            ?.sections
            ?.firstOrNull { section -> section.key == sectionKey }

    override suspend fun getTipsForSection(sectionKey: SectionKey): List<Tip> =
        getSection(sectionKey)?.tips.orEmpty()

    override suspend fun getAdjacentTipIds(tipId: TipId): AdjacentTipIds {
        val tips = catalog?.allTips.orEmpty().sortedBy { tip -> tip.dayNumber }
        val index = tips.indexOfFirst { tip -> tip.id == tipId }

        return AdjacentTipIds(
            previous = tips.getOrNull(index - 1)?.id,
            next = tips.getOrNull(index + 1)?.id,
        )
    }
}

class FakeJourneyRepository(
    initialState: JourneyUserState = JourneyUserState(),
) : JourneyRepository {

    val journeyState = MutableStateFlow(initialState)

    val markedViewedTipIds = mutableListOf<TipId>()
    val bookmarkedMutations = mutableListOf<Pair<TipId, Boolean>>()
    val completionMutations = mutableListOf<Pair<TipId, TipCompletionStatus>>()

    var shouldThrowOnMutation: Boolean = false

    override fun observeJourneyState(): Flow<JourneyUserState> =
        journeyState

    override fun observeTipState(tipId: TipId): Flow<TipUserState> =
        journeyState.map { state ->
            state.tipStates[tipId] ?: TipUserState(tipId = tipId)
        }

    override suspend fun markViewed(tipId: TipId) {
        throwIfNeeded()
        markedViewedTipIds += tipId

        val currentTipState = journeyState.value.tipStates[tipId] ?: TipUserState(tipId = tipId)

        journeyState.value = journeyState.value.copy(
            activeTipId = tipId,
            tipStates = journeyState.value.tipStates + (
                    tipId to currentTipState.copy(
                        lastViewedAtEpochMillis = 1L,
                    )
                    ),
        )
    }

    override suspend fun setBookmarked(
        tipId: TipId,
        bookmarked: Boolean,
    ) {
        throwIfNeeded()
        bookmarkedMutations += tipId to bookmarked

        val currentTipState = journeyState.value.tipStates[tipId] ?: TipUserState(tipId = tipId)

        journeyState.value = journeyState.value.copy(
            tipStates = journeyState.value.tipStates + (
                    tipId to currentTipState.copy(
                        isBookmarked = bookmarked,
                    )
                    ),
        )
    }

    override suspend fun setCompletionStatus(
        tipId: TipId,
        status: TipCompletionStatus,
    ) {
        throwIfNeeded()
        completionMutations += tipId to status

        val currentTipState = journeyState.value.tipStates[tipId] ?: TipUserState(tipId = tipId)

        journeyState.value = journeyState.value.copy(
            tipStates = journeyState.value.tipStates + (
                    tipId to currentTipState.copy(
                        completionStatus = status,
                        completedAtEpochMillis = if (status == TipCompletionStatus.COMPLETED) {
                            1L
                        } else {
                            null
                        },
                    )
                    ),
        )
    }

    override suspend fun resetProgress() {
        throwIfNeeded()
        journeyState.value = JourneyUserState()
    }

    private fun throwIfNeeded() {
        if (shouldThrowOnMutation) {
            error("Journey mutation failed.")
        }
    }
}

class FakePreferencesRepository(
    initialPreferences: UserPreferences = UserPreferences(),
) : PreferencesRepository {

    val preferences = MutableStateFlow(initialPreferences)

    val selectedSectionMutations = mutableListOf<SectionKey?>()

    override fun observePreferences(): Flow<UserPreferences> =
        preferences

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferences.value = preferences.value.copy(themeMode = themeMode)
    }

    override suspend fun setDynamicColorEnabled(enabled: Boolean) {
        preferences.value = preferences.value.copy(dynamicColorEnabled = enabled)
    }

    override suspend fun setReducedMotionEnabled(enabled: Boolean) {
        preferences.value = preferences.value.copy(reducedMotionEnabled = enabled)
    }

    override suspend fun setLastSelectedSection(sectionKey: SectionKey?) {
        selectedSectionMutations += sectionKey
        preferences.value = preferences.value.copy(lastSelectedSectionKey = sectionKey)
    }

    override suspend fun setHasSeenIntro(seen: Boolean) {
        preferences.value = preferences.value.copy(hasSeenIntro = seen)
    }
}