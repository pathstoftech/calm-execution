package com.example.a30daysofcalmexecution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmChip
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTopAppBar
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun AppShell(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        containerColor = CalmTheme.colorTokens.screenBackground,
        contentColor = CalmTheme.colorTokens.onScreenBackground,
        topBar = {
            CalmTopAppBar(
                title = "30 Days of Calm Execution"
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = CalmTheme.colorTokens.screenBackground,
            contentColor = CalmTheme.colorTokens.onScreenBackground,
            shape = CalmTheme.shapeTokens.screenContainer,
            tonalElevation = CalmTheme.elevationTokens.screen,
            shadowElevation = CalmTheme.elevationTokens.none
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CalmTheme.spacingTokens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(
                    space = CalmTheme.spacingTokens.inlineGap,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Foundation Shell Placeholder",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onScreenBackground
                )
                Text(
                    text = "Shared chips installed",
                    style = CalmTheme.typographyTokens.cardSupportingText,
                    color = CalmTheme.colorTokens.onCardContainerVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        CalmTheme.spacingTokens.inlineGap
                    )
                ) {
                    CalmChip(
                        label = "Design system",
                        selected = true
                    )
                    CalmChip(
                        label = "Reusable UI"
                    )
                }
            }
        }
    }

}