package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

data class SidebarNavItem(
    val destination: ScreenDestination,
    val label: String,
    val icon: ImageVector,
    val isHighlighted: Boolean = false
)

@Composable
fun AppSidebar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    userClub: Club? = null,
    isMusicPlaying: Boolean = false,
    onToggleMusic: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        SidebarNavItem(ScreenDestination.HOME, "INICIO", Icons.Default.Home),
        SidebarNavItem(ScreenDestination.MULTIJUGADOR, "MULTIJUGADOR", Icons.Default.Public, isHighlighted = true),
        SidebarNavItem(ScreenDestination.LIGA, "LIGA", Icons.Default.EmojiEvents, isHighlighted = true),
        SidebarNavItem(ScreenDestination.MI_CLUB, "MI CLUB", Icons.Default.Palette, isHighlighted = true),
        SidebarNavItem(ScreenDestination.ADMIN, "PANEL ADMIN", Icons.Default.AdminPanelSettings, isHighlighted = true),
        SidebarNavItem(ScreenDestination.PATROCINADORES, "PATROCINIOS", Icons.Default.Handshake),
        SidebarNavItem(ScreenDestination.PLANTILLA, "PLANTILLA", Icons.Default.Groups),
        SidebarNavItem(ScreenDestination.MERCADO, "MERCADO", Icons.Default.SwapHoriz),
        SidebarNavItem(ScreenDestination.PARTIDOS, "PARTIDOS", Icons.Default.SportsSoccer),
        SidebarNavItem(ScreenDestination.TACTICAS, "TÁCTICAS", Icons.Default.DashboardCustomize),
        SidebarNavItem(ScreenDestination.ESTADISTICAS, "ESTADÍSTICAS", Icons.Default.Leaderboard),
        SidebarNavItem(ScreenDestination.FORO_DT, "FORO DT", Icons.Default.Forum),
        SidebarNavItem(ScreenDestination.AJUSTES, "AJUSTES", Icons.Default.Settings)
    )

    Column(
        modifier = modifier
            .width(172.dp)
            .fillMaxHeight()
            .background(NervaSidebarDark)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Top Club Shield Logo with Name & Edit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF07142E))
                .border(1.dp, Color(0xFF1B3B70), RoundedCornerShape(10.dp))
                .clickable { onNavigate(ScreenDestination.MI_CLUB) }
                .padding(vertical = 6.dp, horizontal = 6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                NervaShieldLogo(size = 38.dp)
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = userClub?.name?.uppercase() ?: "NERVA FC",
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.5.sp,
                        letterSpacing = 0.5.sp,
                        maxLines = 1
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Club",
                        tint = NervaCyanAccent,
                        modifier = Modifier.size(11.dp)
                    )
                }
                Text(
                    text = "TEMPORADA 2026/27",
                    color = NervaCyanAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Navigation Items List (Scrollable)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            navItems.forEach { item ->
                val isSelected = currentScreen == item.destination

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            when {
                                isSelected -> NervaBlueElectric
                                item.destination == ScreenDestination.MULTIJUGADOR -> Color(0xFF063327)
                                item.destination == ScreenDestination.ADMIN -> Color(0xFF3B0707)
                                item.destination == ScreenDestination.MI_CLUB -> Color(0xFF0D234A)
                                else -> Color.Transparent
                            }
                        )
                        .border(
                            width = if (item.isHighlighted && !isSelected) 0.8.dp else 0.dp,
                            color = when (item.destination) {
                                ScreenDestination.MULTIJUGADOR -> NervaNeonGreen.copy(alpha = 0.7f)
                                ScreenDestination.ADMIN -> Color(0xFFDC2626).copy(alpha = 0.6f)
                                ScreenDestination.MI_CLUB -> NervaCyanAccent.copy(alpha = 0.5f)
                                ScreenDestination.LIGA -> Color(0xFFFFD700).copy(alpha = 0.4f)
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(7.dp)
                        )
                        .clickable { onNavigate(item.destination) }
                        .padding(horizontal = 8.dp)
                        .testTag("sidebar_${item.destination.name.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = when {
                            isSelected -> NervaWhite
                            item.destination == ScreenDestination.MULTIJUGADOR -> NervaNeonGreen
                            item.destination == ScreenDestination.ADMIN -> Color(0xFFF87171)
                            item.destination == ScreenDestination.MI_CLUB -> NervaCyanAccent
                            item.destination == ScreenDestination.LIGA -> Color(0xFFFFD700)
                            else -> NervaTextMuted
                        },
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.label,
                        color = when {
                            isSelected -> NervaWhite
                            item.destination == ScreenDestination.MULTIJUGADOR -> NervaNeonGreen
                            item.destination == ScreenDestination.ADMIN -> Color(0xFFFCA5A5)
                            item.destination == ScreenDestination.MI_CLUB -> NervaCyanAccent
                            item.destination == ScreenDestination.LIGA -> Color(0xFFFFE082)
                            else -> NervaTextMuted
                        },
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected || item.isHighlighted) FontWeight.Black else FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 3. Quick Utility Actions: Music Toggle
        if (onToggleMusic != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isMusicPlaying) Color(0xFF064E3B) else Color(0xFF0B1934))
                    .border(1.dp, if (isMusicPlaying) NervaGreen else Color(0xFF1E355B), RoundedCornerShape(6.dp))
                    .clickable { onToggleMusic() }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(if (isMusicPlaying) "🔊" else "🔈", fontSize = 12.sp)
                Text(
                    text = if (isMusicPlaying) "Música ON" else "Música OFF",
                    color = if (isMusicPlaying) NervaGreen else NervaTextMuted,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
