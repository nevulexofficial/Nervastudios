package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppBackgroundTheme
import com.example.model.Club
import com.example.ui.components.AppTopBar
import com.example.ui.components.ClubBadge
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    userClub: Club,
    currentTheme: AppBackgroundTheme = AppBackgroundTheme.ESTADIO_NOCTURNO,
    onSelectTheme: (AppBackgroundTheme) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var highQuality2D by remember { mutableStateOf(true) }
    var autoSave by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(currentTheme.secondaryColorHex))
            .testTag("settings_screen")
    ) {
        AppTopBar(
            userClub = userClub,
            title = "AJUSTES Y TEMAS DEL JUEGO",
            subtitle = "Personaliza el aspecto visual (Negro, Blanco, etc.) y preferencias"
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LEFT COLUMN: Theme Selector Grid (Negro, Blanco, etc.)
            Card(
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF091632)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(NervaCyanAccent, Color(0xFF1E3A8A)))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TEMA VISUAL DEL JUEGO",
                                color = NervaCyanAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Cambia entre Negro Puro, Blanco Nieve y estilos dinámicos",
                                color = NervaTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(currentTheme.primaryColorHex))
                                .border(1.dp, NervaCyanAccent, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${currentTheme.previewIcon} ${currentTheme.title}",
                                color = NervaWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Grid of Themes: Negro, Blanco, Estadio Nocturno, Cyber, Oro, etc.
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(AppBackgroundTheme.values()) { theme ->
                            val isSelected = currentTheme == theme
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(theme.primaryColorHex))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) NervaCyanAccent else Color(0xFF1E293B),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onSelectTheme(theme) }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(theme.previewIcon, fontSize = 22.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = theme.title,
                                            color = if (theme.isLight) Color(0xFF0F172A) else NervaWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = theme.subtitle,
                                            color = if (theme.isLight) Color(0xFF475569) else NervaTextMuted,
                                            fontSize = 9.sp,
                                            maxLines = 1
                                        )
                                    }
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(NervaCyanAccent),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("✓", color = NervaBlueDeep, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // RIGHT COLUMN: Club Information & Preferences
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF091632)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0E1E38)))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Club Summary Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ClubBadge(club = userClub, size = 52.dp)
                        Column {
                            Text(text = userClub.name, color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(text = "DT: ${userClub.managerName}", color = NervaCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = userClub.stadiumName, color = NervaTextMuted, fontSize = 10.sp)
                        }
                    }

                    HorizontalDivider(color = Color(0xFF1E355B))

                    Text(text = "PREFERENCIAS DE SIMULACIÓN", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Motor 2D Alta Definición", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Física de balón y sombreado de campo", color = NervaTextMuted, fontSize = 10.sp)
                        }
                        Switch(
                            checked = highQuality2D,
                            onCheckedChange = { highQuality2D = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Efectos y Silbato en Partido", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Audio de árbitro y ambiente de grada", color = NervaTextMuted, fontSize = 10.sp)
                        }
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Guardado Automático", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Sincroniza fichajes y tácticas al instante", color = NervaTextMuted, fontSize = 10.sp)
                        }
                        Switch(
                            checked = autoSave,
                            onCheckedChange = { autoSave = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF1E355B))

                    Text(
                        text = "NERVA FOOTBALL MANAGER 2026/27 • V2.4",
                        color = NervaWhite.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
