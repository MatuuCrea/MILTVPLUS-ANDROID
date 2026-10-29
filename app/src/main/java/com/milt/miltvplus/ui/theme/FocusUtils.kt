package com.milt.miltvplus.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.dpadFocusable(
    shape: Shape = RoundedCornerShape(12.dp),
    focusedBorderColor: Color = FocusGlowBorder,
    focusedScale: Float = 1.04f,
    borderWidth: Dp = 2.5.dp,
    onClick: (() -> Unit)? = null,
    onFocusChange: ((Boolean) -> Unit)? = null
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) focusedScale else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "FocusScale"
    )
    val interactionSource = remember { MutableInteractionSource() }

    var modifier = this
        .scale(scale)
        .onFocusChanged { state ->
            isFocused = state.isFocused
            onFocusChange?.invoke(state.isFocused)
        }
        .border(
            width = if (isFocused) borderWidth else 0.dp,
            color = if (isFocused) focusedBorderColor else Color.Transparent,
            shape = shape
        )

    if (onClick != null) {
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    }

    return modifier.focusable()
}
