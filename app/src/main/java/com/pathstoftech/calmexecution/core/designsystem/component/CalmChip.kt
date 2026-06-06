package com.pathstoftech.calmexecution.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmChip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val containerColor = if (selected) {
        CalmTheme.colorTokens.primaryAction
    } else {
        CalmTheme.colorTokens.cardContainerVariant
    }

    val contentColor = if (selected) {
        CalmTheme.colorTokens.onPrimaryAction
    } else {
        CalmTheme.colorTokens.onCardContainerVariant
    }

    val clickableModifier = if (onClick != null ) {
        modifier.clickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        modifier
    }

    Surface(
        modifier = clickableModifier.alpha(if (enabled) 1f else DisabledChipAlpha),
        shape = CalmTheme.shapeTokens.chipContainer,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CalmTheme.spacingTokens.medium,
                vertical = CalmTheme.spacingTokens.small
            )
        ) {
            Text(
                text = label,
                style = CalmTheme.typographyTokens.chipLabel,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private const val DisabledChipAlpha = 0.38f