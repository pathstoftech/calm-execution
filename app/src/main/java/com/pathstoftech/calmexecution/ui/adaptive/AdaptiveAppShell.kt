package com.pathstoftech.calmexecution.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

enum class AppAdaptiveLayoutMode {
    COMPACT,
    EXPANDED,
}

@Composable
fun AdaptiveAppShell(
    compactContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        when (adaptiveLayoutMode()) {
            AppAdaptiveLayoutMode.COMPACT -> {
                compactContent()
            }

            AppAdaptiveLayoutMode.EXPANDED -> {
                expandedContent()
            }
        }
    }
}

@Composable
fun ExpandedAppScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(ExpandedAppScaffoldTestTag)
            .padding(horizontal = CalmTheme.spacingTokens.screenPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = ExpandedContentMaxWidth),
            color = CalmTheme.colorTokens.screenBackground,
            contentColor = CalmTheme.colorTokens.onScreenBackground,
        ) {
            content()
        }
    }
}

private fun BoxWithConstraintsScope.adaptiveLayoutMode(): AppAdaptiveLayoutMode =
    if (maxWidth >= ExpandedWidthBreakpoint) {
        AppAdaptiveLayoutMode.EXPANDED
    } else {
        AppAdaptiveLayoutMode.COMPACT
    }

private val ExpandedWidthBreakpoint = 840.dp
private val ExpandedContentMaxWidth = 1200.dp

const val ExpandedAppScaffoldTestTag = "expanded_app_scaffold"