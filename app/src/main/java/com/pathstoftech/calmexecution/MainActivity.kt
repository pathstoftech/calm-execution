package com.pathstoftech.calmexecution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pathstoftech.calmexecution.core.data.catalog.CatalogRepository
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepository
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmExecutionTheme
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.UserPreferences
import com.pathstoftech.calmexecution.di.RepositoryGraphProbe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    @Inject
    lateinit var repositoryGraphProbe: RepositoryGraphProbe
    @Inject
    lateinit var catalogRepository: CatalogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repositoryGraphProbe.verifyGraphIsConstructed()

        enableEdgeToEdge()
        setContent {
            val preferences = preferencesRepository
                .observePreferences()
                .collectAsStateWithLifecycle(
                    initialValue = UserPreferences(),
                )
                .value

            val systemDarkTheme = isSystemInDarkTheme()
            val darkTheme = when (preferences.themeMode) {
                ThemeMode.SYSTEM -> systemDarkTheme
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            CalmExecutionTheme(
                darkTheme = darkTheme,
                dynamicColor = preferences.dynamicColorEnabled,
                reducedMotion = preferences.reducedMotionEnabled,
            ) {
                AppShell(
                    isKnownTipId = { rawTipId ->
                        catalogRepository.getTip(TipId(rawTipId)) != null
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppShellPreview() {
    CalmExecutionTheme(
        darkTheme = false,
        dynamicColor = false,
        reducedMotion = false,
    ) {
        Surface {
            AppShell(
                isKnownTipId = { true },
            )
        }
    }
}