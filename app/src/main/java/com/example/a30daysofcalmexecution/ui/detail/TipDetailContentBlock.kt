package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

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