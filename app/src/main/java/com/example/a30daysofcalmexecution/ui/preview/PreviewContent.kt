package com.example.a30daysofcalmexecution.ui.preview

import androidx.compose.runtime.Composable
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme

@Composable
fun CalmExecutionPreviewContent(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    reducedMotion: Boolean = true,
    content: @Composable () -> Unit,
) {
    CalmExecutionTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        reducedMotion = reducedMotion,
        content = content,
    )
}