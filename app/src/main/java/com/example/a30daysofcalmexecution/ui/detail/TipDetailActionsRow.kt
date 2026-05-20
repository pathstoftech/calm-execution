package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
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
        OutlinedButton(
            onClick = onToggleBookmark,
            modifier = Modifier
                .defaultMinSize(minWidth = 128.dp)
                .semantics {
                    stateDescription = if (isBookmarked) {
                        "Bookmarked"
                    } else {
                        "Not bookmarked"
                    }
                },
        ) {
            Text(
                text = if (isBookmarked) {
                    "Bookmarked"
                } else {
                    "Bookmark"
                },
            )
        }

        Button(
            onClick = onToggleCompleted,
            modifier = Modifier
                .defaultMinSize(minWidth = 128.dp)
                .semantics {
                    stateDescription = if (isCompleted) {
                        "Completed"
                    } else {
                        "Not completed"
                    }
                },
        ) {
            Text(
                text = if (isCompleted) {
                    "Completed"
                } else {
                    "Complete"
                },
            )
        }
    }
}