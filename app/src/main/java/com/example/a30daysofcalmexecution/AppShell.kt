package com.example.a30daysofcalmexecution

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTopAppBar
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.navigation.CalmExecutionNavHost

@Composable
fun AppShell(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        containerColor = CalmTheme.colorTokens.screenBackground,
        contentColor = CalmTheme.colorTokens.onScreenBackground,
        topBar = {
            CalmTopAppBar(title = "30 Days of Calm Execution")
        },
    ) { innerPadding ->
        CalmExecutionNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}