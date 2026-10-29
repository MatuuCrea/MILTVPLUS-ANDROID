package com.milt.miltvplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milt.miltvplus.ui.navigation.NavigationSection
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.AccentOrange
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopNavBar(
    currentSection: NavigationSection,
    selectedCountry: String,
    onSectionSelected: (NavigationSection) -> Unit,
    onSearchClick: () -> Unit,
    onCountrySelectorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTimeString by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        while (true) {
            currentTimeString = sdf.format(Date())
            delay(10000)
        }
    }

    val flagEmoji = when (selectedCountry) {
        "Argentina" -> "🇦🇷"
        "Brasil" -> "🇧🇷"
        "EE.UU." -> "🇺🇸"
        "México" -> "🇲🇽"
        "España" -> "🇪🇸"
        "Uruguay" -> "🇺🇾"
        "Colombia" -> "🇨🇴"
        "Perú" -> "🇵🇪"
        "Venezuela" -> "🇻🇪"
        "Chile" -> "🇨🇱"
        else -> "🌐"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark.copy(alpha = 0.95f))
            .border(1.dp, CardSurfaceBorder.copy(alpha = 0.4f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Top Header Row: Logo, Country Selector, Quick Icons (Search, Notifications, Profile, Time)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .dpadFocusable(
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onSectionSelected(NavigationSection.HOME) }
                    )
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
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
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "MILTVPLUS",
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "TU MUNDO EN UNA SOLA APP",
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quick Actions & Tools
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Country Selector Button
                Row(
                    modifier = Modifier
                        .background(CardSurfaceDark, RoundedCornerShape(20.dp))
                        .border(1.dp, CardSurfaceBorder, RoundedCornerShape(20.dp))
                        .dpadFocusable(
                            shape = RoundedCornerShape(20.dp),
                            onClick = onCountrySelectorClick
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = flagEmoji,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedCountry,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Search Icon
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(CardSurfaceDark, CircleShape)
                        .dpadFocusable(shape = CircleShape, onClick = onSearchClick)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Notifications Icon with badge
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(36.dp)
                        .background(CardSurfaceDark, CircleShape)
                        .dpadFocusable(shape = CircleShape, onClick = {})
                ) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = AccentOrange,
                                contentColor = Color.Black
                            ) { Text("2") }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // User Profile
                IconButton(
                    onClick = { onSectionSelected(NavigationSection.SETTINGS) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(CardSurfaceDark, CircleShape)
                        .dpadFocusable(shape = CircleShape, onClick = { onSectionSelected(NavigationSection.SETTINGS) })
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Time Display
                if (currentTimeString.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(CardSurfaceDark, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentTimeString,
                            color = AccentCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Navigation Category Tabs Row
        val tabsScrollState = rememberScrollState()
        val sections = listOf(
            NavigationSection.HOME,
            NavigationSection.LIVE_TV,
            NavigationSection.MOVIES,
            NavigationSection.SERIES,
            NavigationSection.SPORTS,
            NavigationSection.RADIOS,
            NavigationSection.MUSIC,
            NavigationSection.THEATRE,
            NavigationSection.ADULT,
            NavigationSection.SETTINGS
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tabsScrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sections.forEach { section ->
                val isSelected = section == currentSection
                val bg = if (isSelected) PrimaryBlue else Color.Transparent
                val textColor = if (isSelected) Color.White else TextSecondary
                val borderColor = if (isSelected) AccentCyan else Color.Transparent

                Row(
                    modifier = Modifier
                        .background(bg, RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                        .dpadFocusable(
                            shape = RoundedCornerShape(16.dp),
                            onClick = { onSectionSelected(section) }
                        )
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = section.displayName,
                        color = textColor,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (section.isPinProtected) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Protegido",
                            tint = AccentOrange,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
