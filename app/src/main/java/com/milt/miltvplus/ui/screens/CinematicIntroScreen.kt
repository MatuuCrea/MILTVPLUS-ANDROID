package com.milt.miltvplus.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.DarkNavyBackground
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable
import kotlinx.coroutines.delay

@Composable
fun CinematicIntroScreen(
    onIntroFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.8f) }
    var progress by remember { mutableFloatStateOf(0f) }
    var statusMessageIndex by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val finishIntro = {
        if (!isFinished) {
            isFinished = true
            onIntroFinished()
        }
    }

    val statusMessages = listOf(
        "Inicializando Ecosistema MILTVPLUS...",
        "Cargando Canales IPTV & Fuentes HD...",
        "Configurando Motor de Reproducción Media3...",
        "Sincronizando Guía de Programación EPG...",
        "¡Bienvenido a Tu Mundo!"
    )

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        val totalSteps = 100
        for (i in 1..totalSteps) {
            delay(35)
            progress = i / 100f
            statusMessageIndex = when {
                i < 25 -> 0
                i < 50 -> 1
                i < 75 -> 2
                i < 95 -> 3
                else -> 4
            }
        }
        delay(300)
        finishIntro()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        DarkNavyBackground,
                        Color(0xFF020408)
                    ),
                    radius = 1200f
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Subtle, elegant & non-intrusive Skip Button at top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CardSurfaceDark.copy(alpha = 0.6f))
                    .border(1.dp, CardSurfaceBorder.copy(alpha = 0.5f), CircleShape)
                    .dpadFocusable(
                        shape = CircleShape,
                        onClick = finishIntro
                    )
                    .clickable { finishIntro() }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Omitir",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Omitir",
                    tint = AccentCyan.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Center Animated Logo & Loading Status
        Column(
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(PrimaryBlue, AccentCyan)),
                        shape = CircleShape
                    )
                    .border(3.dp, AccentCyan.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M+",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 46.sp,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MILTVPLUS",
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TU MUNDO EN UNA SOLA APP",
                color = AccentCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier.width(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AccentCyan,
                    trackColor = CardSurfaceDark
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = statusMessages.getOrElse(statusMessageIndex) { "Cargando..." },
                    color = TextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
