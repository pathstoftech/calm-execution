package com.pathstoftech.calmexecution.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

enum class CalmLabelTone {
    Neutral,
    Primary,
    Accent,
    Error
}

@Composable
fun CalmLabel(
    text: String,
    modifier: Modifier = Modifier,
    tone: CalmLabelTone = CalmLabelTone.Neutral
) {
    val containerColor = when (tone) {
        CalmLabelTone.Neutral -> CalmTheme.colorTokens.cardContainerVariant
        CalmLabelTone.Primary -> CalmTheme.colorTokens.primaryAction
        CalmLabelTone.Accent -> CalmTheme.colorTokens.accent
        CalmLabelTone.Error -> CalmTheme.colorTokens.error
    }

    val contentColor = when (tone) {
        CalmLabelTone.Neutral -> CalmTheme.colorTokens.onCardContainerVariant
        CalmLabelTone.Primary -> CalmTheme.colorTokens.onPrimaryAction
        CalmLabelTone.Accent -> CalmTheme.colorTokens.onAccent
        CalmLabelTone.Error -> CalmTheme.colorTokens.onError
    }

    Surface(
        modifier = modifier,
        shape = CalmTheme.shapeTokens.chipContainer,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CalmTheme.spacingTokens.small,
                vertical = CalmTheme.spacingTokens.extraSmall
            )
        ) {
            Text(
                text = text,
                style = CalmTheme.typographyTokens.metadataLabel,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}