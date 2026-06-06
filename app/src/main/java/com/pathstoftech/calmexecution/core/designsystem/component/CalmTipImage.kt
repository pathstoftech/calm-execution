package com.pathstoftech.calmexecution.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmTipImage(
    @DrawableRes imageResId: Int?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    if (imageResId != null) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    } else {
        MissingTipImageFallback(
            modifier = modifier,
        )
    }
}

@Composable
private fun MissingTipImageFallback(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = CalmTheme.colorTokens.cardContainerVariant,
        contentColor = CalmTheme.colorTokens.onCardContainerVariant,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(CalmTheme.spacingTokens.cardPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Image unavailable",
                style = CalmTheme.typographyTokens.metadataLabel,
                color = CalmTheme.colorTokens.onCardContainerVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}