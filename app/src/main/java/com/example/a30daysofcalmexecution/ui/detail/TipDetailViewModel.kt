package com.example.a30daysofcalmexecution.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.model.Tip
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipUserState
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.ScreenViewModel
import com.example.a30daysofcalmexecution.core.ui.UiMessage
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
class TipDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catalogRepository: CatalogRepository,
    private val journeyRepository: JourneyRepository
) : ViewModel(), ScreenViewModel<TipDetailUiState, TipDetailAction> {

    private val tipId = TipId(
        savedStateHandle.get<String>(TipIdSavedStateKey)
            ?: error("Missing tipId.")
    )

    private val reloadRequests = MutableStateFlow(0)
    private val localMessage = MutableStateFlow<UiMessage?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tipResult =
        reloadRequests.flatMapLatest {
            flow<Result<Tip?>> {
                val tip = catalogRepository.getTip(tipId)

                if (tip != null) {
                    markViewed()
                }

                emit(Result.success(tip))
            }.catch { throwable ->
                emit(Result.failure(throwable))
            }
        }

    private val internalState: StateFlow<TipDetailViewModelState> =
        combine(
            tipResult,
            journeyRepository.observeTipState(tipId),
            localMessage
        ) { tipResult, tipUserState, localMessage ->
            tipResult.fold(
                onSuccess = { tip ->
                    if (tip == null) {
                        TipDetailViewModelState(
                            status = AsyncStatus.ERROR,
                            tipUserState = tipUserState,
                            message = UiMessage(
                                id = System.currentTimeMillis(),
                                text = "This tip is not available in the current catalog"
                            )
                        )
                    } else {
                        TipDetailViewModelState(
                            status = AsyncStatus.READY,
                            tip = tip,
                            tipUserState = tipUserState,
                            message = localMessage
                        )
                    }
                },
                onFailure = {
                    TipDetailViewModelState(
                        status = AsyncStatus.ERROR,
                        tipUserState = tipUserState,
                        message = UiMessage(
                            id = System.currentTimeMillis(),
                            text = "Unable to load tip detail."
                        )
                    )
                }
            )
        }
            .catch {
                emit(
                    TipDetailViewModelState(
                        status = AsyncStatus.ERROR,
                        message = UiMessage(
                            id = System.currentTimeMillis(),
                            text = "Unable to load tip detail."
                        )
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TipDetailViewModelState()
            )

    override val uiState: StateFlow<TipDetailUiState> =
        internalState
            .map { state -> state.toUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TipDetailUiState(status = AsyncStatus.LOADING)
            )

    override fun onAction(action: TipDetailAction) {
        when (action) {
            TipDetailAction.ToggleBookmark -> Unit
            TipDetailAction.ToggleCompleted -> Unit
            TipDetailAction.RetryLoad -> {
                reloadRequests.value += 1
            }
            TipDetailAction.DismissMessage -> {
                localMessage.value = null
            }
        }
    }

    private fun markViewed() {
        launchRepositoryMutation {
            journeyRepository.markViewed(tipId)
        }
    }

    private fun launchRepositoryMutation(
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching { block() }
                .onFailure {
                    localMessage.value = UiMessage(
                        id = System.currentTimeMillis(),
                        text = "Unable to update journey state."
                    )
                }
        }
    }
}

private data class TipDetailViewModelState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val tip: Tip? = null,
    val tipUserState: TipUserState? = null,
    val message: UiMessage? = null
)

private fun TipDetailViewModelState.toUiState(): TipDetailUiState {
    val currentTip = tip

    return TipDetailUiState(
        status = status,
        screenTitle = currentTip?.title ?: "Tip detail",
        tip = currentTip?.toTipDetailUi(
            tipUserState = tipUserState
        ),
        message = message
    )
}

private fun Tip.toTipDetailUi(
    tipUserState: TipUserState?
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
            body = body.problem
        ),
        recommendation = TipDetailTextSectionUi(
            title = "Tip",
            body = body.tip
        ),
        whyItHelps = TipDetailTextSectionUi(
            title = "Why it helps",
            body = body.whyItHelps
        ),
        tryToday = TipDetailTextSectionUi(
            title = "Try today",
            body = body.tryToday
        ),
        isBookmarked = tipUserState?.isBookmarked == true,
        completionStatus = tipUserState?.completionStatus ?: TipCompletionStatus.NOT_STARTED
    )

private const val TipIdSavedStateKey = "tipId"