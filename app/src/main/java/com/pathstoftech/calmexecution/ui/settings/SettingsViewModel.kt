package com.pathstoftech.calmexecution.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pathstoftech.calmexecution.core.data.journey.JourneyRepository
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepository
import com.pathstoftech.calmexecution.core.model.UserPreferences
import com.pathstoftech.calmexecution.core.ui.AsyncStatus
import com.pathstoftech.calmexecution.core.ui.ScreenViewModel
import com.pathstoftech.calmexecution.core.ui.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val journeyRepository: JourneyRepository,
) : ViewModel(), ScreenViewModel<SettingsUiState, SettingsAction> {

    private val reloadRequests = MutableStateFlow(0)
    private val showResetProgressDialog = MutableStateFlow(false)
    private val localMessage = MutableStateFlow<UiMessage?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val preferencesResult = reloadRequests
        .flatMapLatest {
            preferencesRepository.observePreferences()
                .map<UserPreferences, Result<UserPreferences>> { preferences ->
                    Result.success(preferences)
                }
                .catch { throwable ->
                    emit(Result.failure(throwable))
                }
        }

    private val internalState: StateFlow<SettingsViewModelState> = combine(
        preferencesResult,
        showResetProgressDialog,
        localMessage,
    ) { preferencesResult, showResetProgressDialog, localMessage ->
        preferencesResult.fold(
            onSuccess = { preferences ->
                SettingsViewModelState(
                    status = AsyncStatus.READY,
                    preferences = preferences,
                    showResetProgressDialog = showResetProgressDialog,
                    message = localMessage,
                )
            },
            onFailure = {
                SettingsViewModelState(
                    status = AsyncStatus.ERROR,
                    showResetProgressDialog = showResetProgressDialog,
                    message = UiMessage(
                        id = System.currentTimeMillis(),
                        text = "Unable to load settings.",
                    ),
                )
            },
        )
    }
        .catch {
            emit(
                SettingsViewModelState(
                    status = AsyncStatus.ERROR,
                    message = UiMessage(
                        id = System.currentTimeMillis(),
                        text = "Unable to load settings.",
                    ),
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsViewModelState(),
        )

    override val uiState: StateFlow<SettingsUiState> = internalState
        .map { state -> state.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetThemeMode -> {
                launchSettingsMutation {
                    preferencesRepository.setThemeMode(action.themeMode)
                }
            }

            is SettingsAction.SetDynamicColorEnabled -> {
                launchSettingsMutation {
                    preferencesRepository.setDynamicColorEnabled(action.enabled)
                }
            }

            is SettingsAction.SetReducedMotionEnabled -> {
                launchSettingsMutation {
                    preferencesRepository.setReducedMotionEnabled(action.enabled)
                }
            }

            SettingsAction.ShowResetProgressDialog -> {
                showResetProgressDialog.value = true
            }

            SettingsAction.DismissResetProgressDialog -> {
                showResetProgressDialog.value = false
            }

            SettingsAction.ConfirmResetProgress -> {
                resetProgress()
            }

            SettingsAction.RetryLoad -> {
                reloadRequests.value += 1
            }

            SettingsAction.DismissMessage -> {
                localMessage.value = null
            }
        }
    }

    private fun resetProgress() {
        showResetProgressDialog.value = false

        viewModelScope.launch {
            runCatching {
                journeyRepository.resetProgress()
            }.onSuccess {
                localMessage.value = UiMessage(
                    id = System.currentTimeMillis(),
                    text = "Progress reset.",
                )
            }.onFailure {
                localMessage.value = UiMessage(
                    id = System.currentTimeMillis(),
                    text = "Unable to reset progress.",
                )
            }
        }
    }

    private fun launchSettingsMutation(
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                block()
            }.onFailure {
                localMessage.value = UiMessage(
                    id = System.currentTimeMillis(),
                    text = "Unable to update settings.",
                )
            }
        }
    }
}

private data class SettingsViewModelState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val preferences: UserPreferences = UserPreferences(),
    val showResetProgressDialog: Boolean = false,
    val message: UiMessage? = null,
)

private fun SettingsViewModelState.toUiState(): SettingsUiState =
    SettingsUiState(
        status = status,
        screenTitle = "Settings",
        themeMode = preferences.themeMode,
        dynamicColorEnabled = preferences.dynamicColorEnabled,
        reducedMotionEnabled = preferences.reducedMotionEnabled,
        showResetProgressDialog = showResetProgressDialog,
        message = message,
    )