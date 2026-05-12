package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun TipDetailActionsRow(
    isBookmarked: Boolean,
    isCompleted: Boolean,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        OutlinedButton(onClick = onToggleBookmark) {
            Text(
                text = if (isBookmarked) {
                    "Bookmarked"
                } else {
                    "Bookmark"
                }
            )
        }

        Button(
            onClick = onToggleCompleted
        ) {
            Text(
                text = if (isCompleted) {
                    "Completed"
                } else {
                    "Mark complete"
                }
            )
        }
    }
}