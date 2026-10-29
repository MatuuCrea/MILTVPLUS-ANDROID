package com.milt.miltvplus.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.AccentRed
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary

@Composable
fun PinDialog(
    enteredPin: String,
    errorMessage: String?,
    isLockedOut: Boolean = false,
    lockoutRemainingSeconds: Int = 0,
    onDigitEntered: (String) -> Unit,
    onDeleteDigit: () -> Unit,
    onConfirmPin: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Control Parental +18",
    subtitle: String = "Ingresa el PIN de 4 dígitos (predeterminado: 0000)"
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .width(380.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp,
                    color = if (isLockedOut) AccentRed.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                )
                .onPreviewKeyEvent { keyEvent ->
                    if (isLockedOut) return@onPreviewKeyEvent true
                    when (keyEvent.key) {
                        Key.Zero, Key.NumPad0 -> { onDigitEntered("0"); true }
                        Key.One, Key.NumPad1 -> { onDigitEntered("1"); true }
                        Key.Two, Key.NumPad2 -> { onDigitEntered("2"); true }
                        Key.Three, Key.NumPad3 -> { onDigitEntered("3"); true }
                        Key.Four, Key.NumPad4 -> { onDigitEntered("4"); true }
                        Key.Five, Key.NumPad5 -> { onDigitEntered("5"); true }
                        Key.Six, Key.NumPad6 -> { onDigitEntered("6"); true }
                        Key.Seven, Key.NumPad7 -> { onDigitEntered("7"); true }
                        Key.Eight, Key.NumPad8 -> { onDigitEntered("8"); true }
                        Key.Nine, Key.NumPad9 -> { onDigitEntered("9"); true }
                        Key.Backspace, Key.Delete -> { onDeleteDigit(); true }
                        Key.Enter, Key.NumPadEnter -> { onConfirmPin(); true }
                        else -> false
                    }
                },
            color = DarkNavyBackground,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            if (isLockedOut) AccentRed.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLockedOut) Icons.Default.LockClock else Icons.Default.Lock,
                        contentDescription = "PIN Lock",
                        tint = if (isLockedOut) AccentRed else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isLockedOut) "PIN Bloqueado Temporalmente" else title,
                    color = if (isLockedOut) AccentRed else TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isLockedOut) "Superaste el límite de 3 intentos fallidos." else subtitle,
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Visual Lockout Countdown Banner
                if (isLockedOut) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentRed.copy(alpha = 0.12f))
                            .border(1.dp, AccentRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Reintenta en:",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${lockoutRemainingSeconds}s",
                            color = AccentRed,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { lockoutRemainingSeconds / 30f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = AccentRed,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    // PIN Dots Indicator (4 Digits)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            val isFilled = index < enteredPin.length
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isFilled) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else SurfaceDark)
                                    .border(
                                        width = if (isFilled) 2.dp else 1.dp,
                                        color = if (isFilled) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isFilled) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    }

                    // Error Message Display
                    AnimatedVisibility(
                        visible = errorMessage != null && !isLockedOut,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = AccentOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = AccentOrange,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Interactive Keypad (3x4 Layout)
                val keypad = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("DEL", "0", "OK")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    keypad.forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { key ->
                                KeypadButton(
                                    label = key,
                                    isDisabled = isLockedOut,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (!isLockedOut) {
                                            when (key) {
                                                "DEL" -> onDeleteDigit()
                                                "OK" -> onConfirmPin()
                                                else -> onDigitEntered(key)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cancel Button
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancelar",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    label: String,
    isDisabled: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val bgColor = when {
        isDisabled -> SurfaceDark.copy(alpha = 0.5f)
        isFocused -> MaterialTheme.colorScheme.primary
        label == "OK" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        label == "DEL" -> AccentRed.copy(alpha = 0.15f)
        else -> CardSurfaceDark
    }

    val contentColor = when {
        isDisabled -> TextMuted.copy(alpha = 0.4f)
        isFocused -> Color.White
        label == "OK" -> MaterialTheme.colorScheme.primary
        label == "DEL" -> AccentRed
        else -> TextPrimary
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isFocused && !isDisabled) 2.dp else 1.dp,
                color = if (isFocused && !isDisabled) Color.White else Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(enabled = !isDisabled)
            .clickable(enabled = !isDisabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (label) {
            "DEL" -> {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Borrar",
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            "OK" -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Desbloquear",
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "OK",
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            else -> {
                Text(
                    text = label,
                    color = contentColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
