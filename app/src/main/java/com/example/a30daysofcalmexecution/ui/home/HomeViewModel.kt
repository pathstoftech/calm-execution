package com.example.a30daysofcalmexecution.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesRepository
import com.example.a30daysofcalmexecution.core.model.JourneyCatalog
import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.Tip
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.ScreenViewModel
import com.example.a30daysofcalmexecution.core.ui.UiMessage
import com.example.a30daysofcalmexecution.ui.detail.TipDetailTextSectionUi
import com.example.a30daysofcalmexecution.ui.detail.TipDetailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val journeyRepository: JourneyRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel(), ScreenViewModel<HomeUiState, HomeAction> {

    private val selectedTipIdOverride = MutableStateFlow<TipId?>(null)
    private val localMessage = MutableStateFlow<UiMessage?>(null)
    private val reloadRequests = MutableStateFlow(0)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val catalogResult =
        reloadRequests.flatMapLatest {
            flow {
                emit(Result.success(catalogRepository.getCatalog()))
            }.catch { throwable ->
                emit(Result.failure(throwable))
            }
        }
    private val internalState: StateFlow<HomeViewModelState> =
        combine (
            catalogResult,
            journeyRepository.observeJourneyState(),
            preferencesRepository.observePreferences(),
            selectedTipIdOverride,
            localMessage
        ) { catalogResult, journey, preferences, selectedTipIdOverride, localMessage ->
            catalogResult.fold(
                onSuccess = { catalog ->
                    HomeViewModelState(
                        status = AsyncStatus.READY,
                        catalog = catalog,
                        journey = journey,
                        preferences = preferences,
                        message = localMessage,
                        selectedTipIdOverride = selectedTipIdOverride
                    )
                },
                onFailure = {
                    HomeViewModelState(
                        status = AsyncStatus.ERROR,
                        journey = journey,
                        preferences = preferences,
                        message = UiMessage(
                            id = System.currentTimeMillis(),
                            text = "Unable to load journey content."
                        ),
                        selectedTipIdOverride = selectedTipIdOverride
                    )
                }
            )
        }
            .catch {
                emit(
                    HomeViewModelState(
                        status = AsyncStatus.ERROR,
                        message = UiMessage(
                            id = System.currentTimeMillis(),
                            text = "Unable to load journey content."
                        )
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeViewModelState()
            )

    override val uiState: StateFlow<HomeUiState> =
        internalState
            .map { state -> state.toUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SelectSection -> {
                launchRepositoryMutation {
                    preferencesRepository.setLastSelectedSection(action.section)
                }
            }

            is HomeAction.OpenTip -> Unit

            is HomeAction.ToggleBookmark -> {
                toggleBookmark(action.tipId)
            }

            is HomeAction.ToggleCompleted -> {
                toggleCompleted(action.tipId)
            }

            is HomeAction.SelectExpandedDetail -> {
                selectedTipIdOverride.value = action.tipId

                action.tipId?.let { tipId ->
                    launchRepositoryMutation {
                        journeyRepository.markViewed(tipId)
                    }
                }
            }

            HomeAction.OpenSettings -> Unit

            HomeAction.RetryLoad -> {
                reloadRequests.value += 1
            }

            HomeAction.DismissMessage -> {
                localMessage.value = null
            }
        }
    }

    private fun toggleBookmark(tipId: TipId) {
        val currentState = internalState.value.journey.tipStates[tipId]
        val nextBookmarkedValue = currentState?.isBookmarked != true

        launchRepositoryMutation {
            journeyRepository.setBookmarked(
                tipId = tipId,
                bookmarked = nextBookmarkedValue
            )
        }
    }

    private fun toggleCompleted(tipId: TipId) {
        val currentStatus =
            internalState.value.journey.tipStates[tipId]?.completionStatus
                ?: TipCompletionStatus.NOT_STARTED

        val nextStatus =
            if (currentStatus == TipCompletionStatus.COMPLETED) {
                TipCompletionStatus.NOT_STARTED
            } else {
                TipCompletionStatus.COMPLETED
            }

        launchRepositoryMutation {
            journeyRepository.setCompletionStatus(
                tipId = tipId,
                status = nextStatus
            )
        }
    }

    private fun launchRepositoryMutation(
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                block()
            }.onFailure {
                localMessage.value =
                    UiMessage(
                        id = System.currentTimeMillis(),
                        text = "Unable to update journey state."
                    )
            }
        }
    }
}

private data class HomeViewModelState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val catalog: JourneyCatalog? = null,
    val journey: JourneyUserState = JourneyUserState(),
    val preferences: UserPreferences = UserPreferences(),
    val message: UiMessage? = null,
    val selectedTipIdOverride: TipId? = null
)

private fun HomeViewModelState.toUiState(): HomeUiState {
    val currentCatalog = catalog ?: return HomeUiState(
        status = status,
        message = message
    )

    val validSectionKeys = currentCatalog.sections
        .map { section -> section.key }
        .toSet()

    val selectedSection = preferences.lastSelectedSectionKey
        ?.takeIf { section -> section in validSectionKeys }

    val allTips = currentCatalog.allTips.sortedBy { tip -> tip.dayNumber }
    val validTips = allTips.map { tip -> tip.id }.toSet()

    val completedTipIds =
        journey.tipStates
            .filterValues { tipState ->
                tipState.completionStatus == TipCompletionStatus.COMPLETED
            }
            .keys

    val currentJourneyTip =
        allTips.firstOrNull { tip -> tip.id !in completedTipIds }
            ?: allTips.lastOrNull()

    val selectedTipId = selectedTipIdOverride
        ?.takeIf { tipId -> tipId in validTips }

    val selectedTip = selectedTipId?.let { selectedId ->
        allTips.firstOrNull { tip -> tip.id == selectedId }
    }

    val visibleSections =
        if (selectedSection == null) {
            currentCatalog.sections
        } else {
            currentCatalog.sections.filter { section ->
                section.key == selectedSection
            }
        }

    val completedCount = completedTipIds.size
    val totalCount = allTips.size

    return HomeUiState(
        status = status,
        screenTitle = currentCatalog.title,
        introText = currentCatalog.subtitle.orEmpty(),
        journey = JourneyProgressUi(
            completedCount = completedCount,
            totalCount = totalCount,
            currentDay = currentJourneyTip?.dayNumber,
            completionFraction =
                if (totalCount == 0) {
                    0f
                } else {
                    completedCount.toFloat() / totalCount.toFloat()
                }
        ),
        selectedSection = selectedSection,
        sectionTabs =
            currentCatalog.sections.map { section ->
                SectionTabUi(
                    key = section.key,
                    title = section.title,
                    isSelected = section.key == selectedSection,
                    completedCount =
                        section.tips.count { tip ->
                            tip.id in completedTipIds
                        },
                    totalCount = section.tips.size
                )
            },
        feedSections =
            visibleSections.map { section ->
                HomeFeedSectionUi(
                    key = section.key,
                    title = section.title,
                    items =
                        section.tips
                            .sortedBy { tip -> tip.dayNumber }
                            .map { tip ->
                                tip.toTipCardUi(
                                    isCompleted = tip.id in completedTipIds,
                                    isBookmarked =
                                        journey.tipStates[tip.id]?.isBookmarked == true
                                )
                            },
                )
            },
        featuredTipId = currentJourneyTip?.id,
        selectedTipId = selectedTipId,
        selectedTipDetail = selectedTip?.toTipDetailUi(
            isBookmarked = journey.tipStates[selectedTip.id]?.isBookmarked == true,
            completionStatus = journey.tipStates[selectedTip.id]?.completionStatus
                ?: TipCompletionStatus.NOT_STARTED,
        ),
        message = message
    )
}

private fun Tip.toTipCardUi(
    isCompleted: Boolean,
    isBookmarked: Boolean
): TipCardUi =
    TipCardUi(
        id = id,
        dayLabel = String.format(Locale.US, "Day %02d", dayNumber),
        title = title,
        previewText = previewText,
        categoryLabel = categoryKey.label,
        imageKey = image.imageKey,
        isCompleted = isCompleted,
        isBookmarked = isBookmarked
    )

private fun Tip.toTipDetailUi(
    isBookmarked: Boolean,
    completionStatus: TipCompletionStatus,
): TipDetailUi =
    TipDetailUi(
        id = id,
        dayLabel = String.format(Locale.US, "Day %02d", dayNumber),
        title = title,
        categoryLabel = categoryKey.label,
        imageKey = image.imageKey,
        imageContentDescription = image.contentDescription,
        imageDecorative = image.isDecorative,
        problem = TipDetailTextSectionUi(
            title = "Problem",
            body = body.problem,
        ),
        recommendation = TipDetailTextSectionUi(
            title = "Tip",
            body = body.tip,
        ),
        whyItHelps = TipDetailTextSectionUi(
            title = "Why it helps",
            body = body.whyItHelps,
        ),
        tryToday = TipDetailTextSectionUi(
            title = "Try today",
            body = body.tryToday,
        ),
        isBookmarked = isBookmarked,
        completionStatus = completionStatus,
    )