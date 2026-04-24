package com.example.a30daysofcalmexecution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.ui.theme._30DaysOfCalmExecutionTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var catalogRepository: CatalogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Temporary DI smoke check.
        // Do not load catalog here yet; this only proves Hilt can inject the repository.
        check(::catalogRepository.isInitialized)

        enableEdgeToEdge()
        setContent {
            _30DaysOfCalmExecutionTheme {
                AppShell()
            }
        }
    }
}

@Preview
@Composable
fun AppShellPreview() {
    _30DaysOfCalmExecutionTheme {
        Surface {
            AppShell()
        }
    }
}