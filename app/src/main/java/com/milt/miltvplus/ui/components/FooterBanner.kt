package com.milt.miltvplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentLime
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary

@Composable
fun FooterBanner(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceDark,
                        Color(0xFF070A12)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(CardSurfaceBorder.copy(alpha = 0.5f), Color.Transparent)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .padding(vertical = 28.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Feature Pills Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterFeatureItem(
                icon = Icons.Default.Devices,
                title = "Multiplataforma",
                subtitle = "Android • Android TV • TV Box • Web",
                accentColor = AccentCyan
            )
            FooterFeatureItem(
                icon = Icons.Default.Hd,
                title = "Alta Calidad",
                subtitle = "HD • Full HD • 4K UHD",
                accentColor = AccentLime
            )
            FooterFeatureItem(
                icon = Icons.Default.Security,
                title = "Seguridad",
                subtitle = "Control Parental & Cifrado",
                accentColor = PrimaryBlue
            )
            FooterFeatureItem(
                icon = Icons.Default.SupportAgent,
                title = "Soporte 24/7",
                subtitle = "Atención permanente",
                accentColor = AccentOrange
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = CardSurfaceBorder.copy(alpha = 0.4f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // Branding & Made in Argentina badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(PrimaryBlue, AccentCyan)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M+",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "MILTVPLUS",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "TU MUNDO EN UNA SOLA APP",
                        color = AccentCyan,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Flag badge & Argentina tag
            Row(
                modifier = Modifier
                    .background(CardSurfaceDark, RoundedCornerShape(20.dp))
                    .border(1.dp, CardSurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🇦🇷",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Hecho en Argentina",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "MILTVPlus Siempre contigo • v1.0",
                color = TextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun FooterFeatureItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
