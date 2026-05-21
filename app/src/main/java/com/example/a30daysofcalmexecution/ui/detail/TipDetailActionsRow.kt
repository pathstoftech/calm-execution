package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

@Composable
fun TipDetailActionsRow(
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                }
                .testTag("tip_detail_complete_action"),
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