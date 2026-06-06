package com.pathstoftech.calmexecution.ui.settings

import com.pathstoftech.calmexecution.core.model.JourneyUserState
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.model.TipCompletionStatus
import com.pathstoftech.calmexecution.core.model.TipUserState
import com.pathstoftech.calmexecution.core.model.UserPreferences
import com.pathstoftech.calmexecution.core.ui.AsyncStatus
import com.pathstoftech.calmexecution.testing.FakeJourneyRepository
import com.pathstoftech.calmexecution.testing.FakePreferencesRepository
import com.pathstoftech.calmexecution.testing.MainDispatcherRule
import com.pathstoftech.calmexecution.testing.ViewModelTestData
import com.pathstoftech.calmexecution.testing.collectStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun readyState_mapsPreferencesToUiState() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository(
            initialPreferences = UserPreferences(
                themeMode = ThemeMode.DARK,
                dynamicColorEnabled = true,
                reducedMotionEnabled = true,
            ),
        )
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.READY, state.status)
        assertEquals("Settings", state.screenTitle)
        assertEquals(ThemeMode.DARK, state.themeMode)
        assertEquals(true, state.dynamicColorEnabled)
        assertEquals(true, state.reducedMotionEnabled)
        assertEquals(false, state.showResetProgressDialog)
        assertNull(state.message)

        collectJob.cancel()
    }

    @Test
    fun setThemeMode_updatesRepositoryAndUiState() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository()
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.SetThemeMode(ThemeMode.LIGHT))
        advanceUntilIdle()

        assertEquals(listOf(ThemeMode.LIGHT), preferencesRepository.themeModeMutations)
        assertEquals(ThemeMode.LIGHT, viewModel.uiState.value.themeMode)

        collectJob.cancel()
    }

    @Test
    fun setDynamicColorEnabled_updatesRepositoryAndUiState() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository()
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.SetDynamicColorEnabled(true))
        advanceUntilIdle()

        assertEquals(listOf(true), preferencesRepository.dynamicColorMutations)
        assertEquals(true, viewModel.uiState.value.dynamicColorEnabled)

        collectJob.cancel()
    }

    @Test
    fun setReducedMotionEnabled_updatesRepositoryAndUiState() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository()
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.SetReducedMotionEnabled(true))
        advanceUntilIdle()

        assertEquals(listOf(true), preferencesRepository.reducedMotionMutations)
        assertEquals(true, viewModel.uiState.value.reducedMotionEnabled)

        collectJob.cancel()
    }

    @Test
    fun showAndDismissResetProgressDialog_updatesDialogState() = runViewModelTest {
        val viewModel = createViewModel()

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.ShowResetProgressDialog)
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.showResetProgressDialog)

        viewModel.onAction(SettingsAction.DismissResetProgressDialog)
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.showResetProgressDialog)

        collectJob.cancel()
    }

    @Test
    fun confirmResetProgress_resetsJourneyHidesDialogAndShowsSuccessMessage() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository(
            initialState = JourneyUserState(
                activeTipId = ViewModelTestData.DayOneTipId,
                tipStates = mapOf(
                    ViewModelTestData.DayOneTipId to TipUserState(
                        tipId = ViewModelTestData.DayOneTipId,
                        isBookmarked = true,
                        completionStatus = TipCompletionStatus.COMPLETED,
                        completedAtEpochMillis = 1L,
                    ),
                ),
            ),
        )
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.ShowResetProgressDialog)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.ConfirmResetProgress)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(1, journeyRepository.resetProgressCallCount)
        assertEquals(JourneyUserState(), journeyRepository.journeyState.value)
        assertEquals(false, state.showResetProgressDialog)
        assertEquals("Progress reset.", state.message?.text)

        collectJob.cancel()
    }

    @Test
    fun preferencesLoadFailure_mapsToErrorState() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository().apply {
            shouldThrowOnObserve = true
        }
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.ERROR, state.status)
        assertEquals("Unable to load settings.", state.message?.text)

        collectJob.cancel()
    }

    @Test
    fun retryLoad_reloadsPreferencesAfterFailure() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository(
            initialPreferences = UserPreferences(
                themeMode = ThemeMode.DARK,
                dynamicColorEnabled = true,
                reducedMotionEnabled = true,
            ),
        ).apply {
            shouldThrowOnObserve = true
        }
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        assertEquals(AsyncStatus.ERROR, viewModel.uiState.value.status)

        preferencesRepository.shouldThrowOnObserve = false
        viewModel.onAction(SettingsAction.RetryLoad)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.READY, state.status)
        assertEquals(ThemeMode.DARK, state.themeMode)
        assertEquals(true, state.dynamicColorEnabled)
        assertEquals(true, state.reducedMotionEnabled)

        collectJob.cancel()
    }

    @Test
    fun preferenceMutationFailure_keepsStateAndShowsErrorMessage() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository(
            initialPreferences = UserPreferences(
                themeMode = ThemeMode.SYSTEM,
            ),
        ).apply {
            shouldThrowOnMutation = true
        }
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.SetThemeMode(ThemeMode.DARK))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.READY, state.status)
        assertEquals(ThemeMode.SYSTEM, state.themeMode)
        assertTrue(preferencesRepository.themeModeMutations.isEmpty())
        assertEquals("Unable to update settings.", state.message?.text)

        collectJob.cancel()
    }

    @Test
    fun resetProgressFailure_hidesDialogAndShowsErrorMessage() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository().apply {
            shouldThrowOnMutation = true
        }
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.ShowResetProgressDialog)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.ConfirmResetProgress)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(0, journeyRepository.resetProgressCallCount)
        assertEquals(false, state.showResetProgressDialog)
        assertEquals("Unable to reset progress.", state.message?.text)

        collectJob.cancel()
    }

    @Test
    fun dismissMessage_clearsLocalMessage() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository().apply {
            shouldThrowOnMutation = true
        }
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(SettingsAction.SetDynamicColorEnabled(true))
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.message)

        viewModel.onAction(SettingsAction.DismissMessage)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.message)

        collectJob.cancel()
    }

    private fun createViewModel(
        preferencesRepository: FakePreferencesRepository = FakePreferencesRepository(),
        journeyRepository: FakeJourneyRepository = FakeJourneyRepository(),
    ): SettingsViewModel =
        SettingsViewModel(
            preferencesRepository = preferencesRepository,
            journeyRepository = journeyRepository,
        )

    private fun runViewModelTest(
        block: suspend TestScope.() -> Unit,
    ) = runTest(
        context = mainDispatcherRule.testDispatcher,
        testBody = block,
    )
}