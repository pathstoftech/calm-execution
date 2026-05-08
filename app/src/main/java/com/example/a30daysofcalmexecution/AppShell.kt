package com.example.a30daysofcalmexecution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun AppShell(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
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
                text = "30 Days of Calm Execution",
                style = CalmTheme.typographyTokens.appTitle,
                color = CalmTheme.colorTokens.onScreenBackground
            )
            Text(
                text = "Foundation Shell Placeholder",
                style = CalmTheme.typographyTokens.cardSupportingText,
                color = CalmTheme.colorTokens.onCardContainerVariant
            )
        }
    }
}