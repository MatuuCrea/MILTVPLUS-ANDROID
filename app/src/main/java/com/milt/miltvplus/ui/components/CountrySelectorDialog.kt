package com.milt.miltvplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.milt.miltvplus.ui.theme.AccentCyan
import com.milt.miltvplus.ui.theme.CardSurfaceBorder
import com.milt.miltvplus.ui.theme.CardSurfaceDark
import com.milt.miltvplus.ui.theme.PrimaryBlue
import com.milt.miltvplus.ui.theme.SurfaceDark
import com.milt.miltvplus.ui.theme.TextMuted
import com.milt.miltvplus.ui.theme.TextPrimary
import com.milt.miltvplus.ui.theme.TextSecondary
import com.milt.miltvplus.ui.theme.dpadFocusable

data class CountryInfo(
    val name: String,
    val flagEmoji: String,
    val code: String
)

val AvailableCountries = listOf(
    CountryInfo("Argentina", "🇦🇷", "AR"),
    CountryInfo("Brasil", "🇧🇷", "BR"),
    CountryInfo("EE.UU.", "🇺🇸", "US"),
    CountryInfo("México", "🇲🇽", "MX"),
    CountryInfo("España", "🇪🇸", "ES"),
    CountryInfo("Uruguay", "🇺🇾", "UY"),
    CountryInfo("Colombia", "🇨🇴", "CO"),
    CountryInfo("Perú", "🇵🇪", "PE"),
    CountryInfo("Venezuela", "🇻🇪", "VE"),
    CountryInfo("Chile", "🇨🇱", "CL"),
    CountryInfo("Paraguay", "🇵🇾", "PY"),
    CountryInfo("Bolivia", "🇧🇴", "BO"),
    CountryInfo("Ecuador", "🇪🇨", "EC")
)

@Composable
fun CountrySelectorDialog(
    selectedCountry: String,
    onCountrySelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            AvailableCountries
        } else {
            AvailableCountries.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 520.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(CardSurfaceBorder, Color.Transparent)
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Selecciona tu país",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Canales y contenidos destacados para tu región",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .size(32.dp)
                            .background(CardSurfaceDark, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar país...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurfaceDark,
                        unfocusedContainerColor = CardSurfaceDark,
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = CardSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Country List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredCountries) { country ->
                        val isSelected = country.name.equals(selectedCountry, ignoreCase = true)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) PrimaryBlue.copy(alpha = 0.25f) else CardSurfaceDark,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AccentCyan else CardSurfaceBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .dpadFocusable(
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = { onCountrySelected(country.name) }
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = country.flagEmoji,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = country.name,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(AccentCyan, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
