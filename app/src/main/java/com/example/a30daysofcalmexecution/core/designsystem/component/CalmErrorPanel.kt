package com.example.a30daysofcalmexecution.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmErrorPanel(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Column(
            modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
        ) {
            CalmLabel(
                text = "Error",
                tone = CalmLabelTone.Error
            )
            Text(
                text = title,
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.error
            )
            Text(
                text = message,
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant
            )

            if (actionLabel != null && onActionClick != null) {
                OutlinedButton(
                    onClick = onActionClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CalmTheme.colorTokens.error
                    )
                ) {
                    Text(
                        text = actionLabel,
                        style = CalmTheme.typographyTokens.actionLabel,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}