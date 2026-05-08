package com.example.a30daysofcalmexecution.core.designsystem.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable

@Immutable
data class CalmShapeTokens(
    val screenContainer: CornerBasedShape,
    val cardContainer: CornerBasedShape,
    val cardContainerLarge: CornerBasedShape,
    val chipContainer: CornerBasedShape,
    val buttonContainer: CornerBasedShape,
    val dialogContainer: CornerBasedShape
)

internal fun Shapes.toCalmShapeTokens(): CalmShapeTokens {
    return CalmShapeTokens(
        screenContainer = extraSmall,
        cardContainer = medium,
        cardContainerLarge = large,
        chipContainer = small,
        buttonContainer = small,
        dialogContainer = extraLarge
    )
}