package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.components.NervaShieldLogo
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun MatchHubScreen(
    userClub: Club,
    rivalClub: Club,
    onPlayMatch: () -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF3B166A),
                        Color(0xFF260D4E),
                        Color(0xFF14052E)
                    )
                )
            )
            .testTag("match_hub_screen_exact")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. TOP STATUS BAR (Image 1: FV 200 on left, 2276 FV+ & Gear on right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Free Coins Banner
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE61A52).copy(alpha = 0.8f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = NervaWhite, modifier = Modifier.size(14.dp))
                        Text(
                            text = "FV 200 ¡MONEDAS GRATIS!",
                            color = NervaWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Coins Pill & Settings
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(1.dp, Color(0xFF6A3B9E), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "⚡", fontSize = 12.sp)
                            Text(
                                text = "${userClub.coins}",
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Text(text = "+", color = Color(0xFFE61A52), fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .clickable { onNavigate(ScreenDestination.AJUSTES) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = NervaWhite, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // 2. MAIN CENTER ROW: Left Match Card + Right Club Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // LEFT PANEL: Match Fixture Card
                Card(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF5A259E))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Liga C Argentina",
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "LUN, 20 JUL 2026, Soleado, Ronda 10",
                                color = NervaWhite.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }

                        // Matchup (Floresta vs Cóndor Inca FC / NERVA FC)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // Opponent (Floresta)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "FLR", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                }
                                Text(text = "Floresta", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            // Center VS & Time
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "⚽", fontSize = 10.sp)
                                    Text(text = "☀️", fontSize = 10.sp)
                                }
                                Text(
                                    text = "VS",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF260D4E))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "8:30 p.", color = NervaWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // User Club (Cóndor Inca FC / NERVA FC)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                NervaShieldLogo(size = 46.dp)
                                Text(text = userClub.name, color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Calendar Day Strip (7 days with icons)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF381466)),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CalendarDayCell("JUL 20", "⚽", isActive = true)
                            CalendarDayCell("JUL 21", "▲")
                            CalendarDayCell("JUL 22", "☕")
                            CalendarDayCell("JUL 23", "☕")
                            CalendarDayCell("JUL 24", "☕")
                            CalendarDayCell("JUL 25", "🛡", isHighlight = true)
                            CalendarDayCell("JUL 26", "▲")
                        }

                        // 3 Bottom Action Tiles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ActionMiniTile(
                                title = "ESTADÍSTICAS",
                                icon = Icons.Default.Leaderboard,
                                onClick = { onNavigate(ScreenDestination.ESTADISTICAS) },
                                modifier = Modifier.weight(1f)
                            )
                            ActionMiniTile(
                                title = "SITUACIÓN",
                                icon = Icons.Default.TrendingUp,
                                subtitle = "⚡ -344",
                                onClick = { onNavigate(ScreenDestination.LIGA) },
                                modifier = Modifier.weight(1f)
                            )
                            ActionMiniTile(
                                title = "CALENDARIO",
                                icon = Icons.Default.CalendarMonth,
                                onClick = { onNavigate(ScreenDestination.LIGA) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // RIGHT PANEL: Club Card + Gestión Equipo + Traslado
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Top Club Card with Slanted Ranking Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4C218B))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    NervaShieldLogo(size = 40.dp)
                                    Column {
                                        Text(
                                            text = userClub.name,
                                            color = NervaWhite,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color.White.copy(alpha = 0.2f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(text = "67 OVR", color = NervaWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = NervaWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Slanted Red/Pink Banner: "Ranking de liga: 1 >"
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFFF1B68), Color(0xFFE61A52))
                                        )
                                    )
                                    .clickable { onNavigate(ScreenDestination.LIGA) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ranking de liga: 1",
                                        color = NervaWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "›",
                                        color = NervaWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Row: GESTIÓN EQUIPO + TRASLADO
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Gestión Equipo (Tactical 4-2-3-1 icon)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(10.dp))
                                .clickable { onNavigate(ScreenDestination.TACTICAS) },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF4C218B))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    text = "GESTIÓN EQUIPO",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )

                                // Tactical Pitch Board Icon
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFF260D4E), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "☷", color = NervaWhite, fontSize = 20.sp)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFFFF1B68))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "4-2-3-1(1)", color = NervaWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Traslado Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onNavigate(ScreenDestination.MERCADO) },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1255))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "TRASLADO",
                                        color = NervaWhite.copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "!", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                    }
                                }

                                Text(text = "👤 68 RB", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Contreras", color = NervaTextMuted, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // 3. BOTTOM BAR (Image 1: Home button on left, Red/Pink soccer ball button on right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Button
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black)
                        .clickable { onNavigate(ScreenDestination.HOME) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = NervaWhite, modifier = Modifier.size(20.dp))
                }

                // Match Start Button (Glowing Red/Pink gradient with soccer ball)
                Button(
                    onClick = onPlayMatch,
                    modifier = Modifier
                        .width(130.dp)
                        .height(38.dp)
                        .testTag("play_match_button_exact"),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF1B68),
                        contentColor = NervaWhite
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = NervaWhite, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "JUGAR", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: String,
    icon: String,
    isActive: Boolean = false,
    isHighlight: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date,
            color = if (isActive) Color(0xFFFF1B68) else if (isHighlight) Color(0xFF38BDF8) else NervaWhite.copy(alpha = 0.7f),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = icon, fontSize = 11.sp)
    }
}

@Composable
private fun ActionMiniTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF4C218B))
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = NervaWhite,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Icon(imageVector = icon, contentDescription = title, tint = NervaWhite, modifier = Modifier.size(16.dp))
            if (subtitle != null) {
                Text(text = subtitle, color = Color(0xFFFBBF24), fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
