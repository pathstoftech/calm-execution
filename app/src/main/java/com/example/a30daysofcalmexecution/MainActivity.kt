package com.example.a30daysofcalmexecution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.a30daysofcalmexecution.di.RepositoryGraphProbe
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repositoryGraphProbe: RepositoryGraphProbe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repositoryGraphProbe.verifyGraphIsConstructed()

        enableEdgeToEdge()
        setContent {
            CalmExecutionTheme {
                AppShell()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppShellPreview() {
    CalmExecutionTheme {
        Surface {
            AppShell()
        }
    }
}