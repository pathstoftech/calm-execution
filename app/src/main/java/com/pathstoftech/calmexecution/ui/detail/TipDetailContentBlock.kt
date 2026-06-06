package com.pathstoftech.calmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

// Keep this order aligned with the locked Detail reading order:
// Problem -> Tip -> Why it helps -> Try today.
@Composable
fun TipDetailContentBlock(
    tip: TipDetailUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap)
    ) {
        TipDetailSectionCard(
            section = tip.problem
        )

        TipDetailSectionCard(
            section = tip.recommendation
        )

        TipDetailSectionCard(
            section = tip.whyItHelps
        )

        TipDetailSectionCard(
            section = tip.tryToday
        )
    }
}