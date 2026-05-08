package com.example.a30daysofcalmexecution.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalmTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = CalmTheme.typographyTokens.screenTitle,
                color = CalmTheme.colorTokens.onScreenBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CalmTheme.colorTokens.screenBackground,
            titleContentColor = CalmTheme.colorTokens.onScreenBackground,
            navigationIconContentColor = CalmTheme.colorTokens.onScreenBackground,
            actionIconContentColor = CalmTheme.colorTokens.onScreenBackground
        )
    )
}