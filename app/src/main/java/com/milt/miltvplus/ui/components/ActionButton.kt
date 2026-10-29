package com.milt.miltvplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.dpadFocusable

enum class ActionButtonType {
    PRIMARY,
    ACCENT,
    SECONDARY,
    OUTLINED
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    type: ActionButtonType = ActionButtonType.PRIMARY
) {
    val shape = RoundedCornerShape(10.dp)

    val (bgModifier, contentColor) = when (type) {
        ActionButtonType.PRIMARY -> {
            Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(PrimaryBlue, Color(0xFF0288D1))
                ),
                shape = shape
            ) to Color.White
        }
        ActionButtonType.ACCENT -> {
            Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(AccentCyan, Color(0xFF00B0FF))
                ),
                shape = shape
            ) to Color.Black
        }
        ActionButtonType.SECONDARY -> {
            Modifier.background(CardSurfaceDark, shape = shape) to Color.White
        }
        ActionButtonType.OUTLINED -> {
            Modifier
                .background(Color.Transparent, shape = shape)
                .border(1.dp, CardSurfaceBorder, shape) to Color.White
        }
    }

    Row(
        modifier = modifier
            .then(bgModifier)
            .dpadFocusable(shape = shape, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
