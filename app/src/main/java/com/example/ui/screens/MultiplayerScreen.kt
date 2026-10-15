package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun MultiplayerScreen(
    presenceList: List<MultiplayerPresence>,
    lobbiesList: List<MultiplayerLobby>,
    leaderboardList: List<MultiplayerLeaderboardEntry>,
    activeLobby: MultiplayerLobby?,
    isSearching: Boolean,
    statusMessage: String?,
    isSupabaseConfigured: Boolean,
    onQuickMatchmaking: () -> Unit,
    onCreatePrivateLobby: () -> Unit,
    onJoinLobby: (String) -> Unit,
    onChallengeManager: (MultiplayerPresence) -> Unit,
    onCancelMatchmaking: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Salas en Vivo, 1: Managers Online, 2: Ranking ELO, 3: Supabase
    var roomCodeInput by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radarPulse"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. TOP HEADER & STATUS
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071426)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF00FF88).copy(alpha = 0.5f), Color(0xFF00D4FF).copy(alpha = 0.5f))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0A3022)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = null,
                                    tint = NervaNeonGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "NERVA MULTIPLAYER CLOUD",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Servidores Globales • Partidos 1v1 en Tiempo Real",
                                    color = NervaTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSupabaseConfigured) Color(0xFF06331E) else Color(0xFF281E05))
                                .border(
                                    1.dp,
                                    if (isSupabaseConfigured) NervaNeonGreen else NervaGold,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSupabaseConfigured) NervaNeonGreen else NervaGold)
                                )
                                Text(
                                    text = if (isSupabaseConfigured) "SUPABASE ONLINE" else "SIMULACIÓN CLOUD",
                                    color = if (isSupabaseConfigured) NervaNeonGreen else NervaGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp
                                )
                            }
                        }
                    }

                    // Stats summary bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1D35))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Tu ELO: 1220 pts • División 12",
                            color = NervaCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "🟢 ${presenceList.size} Managers Activos",
                            color = NervaNeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. MATCHMAKING RADAR & ACTION CONTROLS
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF1E3A8A), Color(0xFF0E7490))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isSearching) {
                        // Searching animation
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size((50 * radarPulse).dp)
                                    .clip(CircleShape)
                                    .background(NervaNeonGreen.copy(alpha = 0.2f))
                                    .border(2.dp, NervaNeonGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Radar,
                                    contentDescription = null,
                                    tint = NervaNeonGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = statusMessage ?: "Buscando rival online en Supabase...",
                                color = NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Button(
                                onClick = onCancelMatchmaking,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D))
                            ) {
                                Text("Cancelar Búsqueda", color = NervaWhite, fontSize = 11.sp)
                            }
                        }
                    } else {
                        // Normal matchmaking buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onQuickMatchmaking,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NervaNeonGreen,
                                    contentColor = Color(0xFF051C14)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Rival Rápido", fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }

                            Button(
                                onClick = onCreatePrivateLobby,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1E3A8A),
                                    contentColor = NervaWhite
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircleOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Crear Sala", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        // Join with code row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = roomCodeInput,
                                onValueChange = { roomCodeInput = it.uppercase() },
                                placeholder = { Text("Código de Sala (ej. NERVA-1024)", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NervaCyan,
                                    unfocusedBorderColor = Color(0xFF1F3A60),
                                    focusedTextColor = NervaWhite,
                                    unfocusedTextColor = NervaWhite
                                )
                            )

                            Button(
                                onClick = {
                                    if (roomCodeInput.isNotBlank()) {
                                        onJoinLobby(roomCodeInput)
                                        roomCodeInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NervaCyan, contentColor = NervaBlueDeep)
                            ) {
                                Text("Unirse", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    if (statusMessage != null && !isSearching) {
                        Text(
                            text = statusMessage,
                            color = NervaGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 3. NAVIGATION TABS
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF08142A))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "Salas en Vivo" to Icons.Default.MeetingRoom,
                    "Managers Online" to Icons.Default.People,
                    "Ranking ELO" to Icons.Default.Leaderboard,
                    "Supabase" to Icons.Default.CloudSync
                ).forEachIndexed { index, (label, icon) ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF00FF88) else Color.Transparent)
                            .clickable { selectedTab = index }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF051C14) else NervaTextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = label,
                                color = if (isSelected) Color(0xFF051C14) else NervaTextMuted,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. TAB CONTENTS
        when (selectedTab) {
            0 -> {
                // SALAS EN VIVO
                if (lobbiesList.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No hay salas públicas abiertas en este momento.", color = NervaTextMuted, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("¡Crea una sala nueva o usa Rival Rápido!", color = NervaCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    items(lobbiesList) { lobby ->
                        LobbyCard(lobby = lobby, onJoin = { onJoinLobby(lobby.roomCode) })
                    }
                }
            }

            1 -> {
                // MANAGERS ONLINE
                items(presenceList) { presence ->
                    PresenceCard(presence = presence, onChallenge = { onChallengeManager(presence) })
                }
            }

            2 -> {
                // RANKING ELO
                items(leaderboardList) { entry ->
                    LeaderboardCard(entry = entry)
                }
            }

            3 -> {
                // SUPABASE CONFIG & SCHEMA INFO
                item {
                    SupabaseInfoCard(isConfigured = isSupabaseConfigured, onRefresh = onRefresh)
                }
            }
        }
    }
}

@Composable
fun LobbyCard(lobby: MultiplayerLobby, onJoin: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NervaBlueSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0284C7))))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = lobby.roomCode, color = NervaNeonGreen, fontWeight = FontWeight.Black, fontSize = 13.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF0F3B2C))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text("SALA PÚBLICA", color = NervaNeonGreen, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(text = "Anfitrión: ${lobby.hostClubName} (${lobby.hostElo} ELO)", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = onJoin,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NervaCyan, contentColor = NervaBlueDeep),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("Desafiar", fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun PresenceCard(presence: MultiplayerPresence, onChallenge: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (presence.status == "looking_for_match") NervaGold else NervaNeonGreen)
                )
                Column {
                    Text(text = "${presence.clubName} • ${presence.managerName}", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        text = "División ${presence.clubDivision} • ${presence.eloRating} ELO • ${presence.pingMs}ms",
                        color = NervaTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Button(
                onClick = onChallenge,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A402E), contentColor = NervaNeonGreen),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Jugar 1v1", fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
            }
        }
    }
}

@Composable
fun LeaderboardCard(entry: MultiplayerLeaderboardEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "#${entry.rank}",
                    color = when (entry.rank) {
                        1 -> NervaGold
                        2 -> Color(0xFFE2E8F0)
                        3 -> Color(0xFFCD7F32)
                        else -> NervaTextMuted
                    },
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
                Column {
                    Text(text = entry.clubName, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        text = "DT: ${entry.managerName} • ${entry.divisionTier} • ${entry.wins}V ${entry.draws}E ${entry.losses}D",
                        color = NervaTextMuted,
                        fontSize = 9.5.sp
                    )
                }
            }

            Text(
                text = "${entry.eloRating} ELO",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun SupabaseInfoCard(isConfigured: Boolean, onRefresh: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF06182E)),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF00FF88), Color(0xFF00D4FF))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "⚡ CONEXIÓN SUPABASE CLOUD MULTIPLAYER",
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
            Text(
                text = "NERVA FC cuenta con esquema SQL completo de tablas y políticas RLS listo para ejecutar en Supabase:",
                color = NervaTextMuted,
                fontSize = 11.sp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF040F1D))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text("• public.multiplayer_presence (jugadores online)", color = NervaNeonGreen, fontSize = 10.sp)
                Text("• public.multiplayer_lobbies (salas y matchmaking)", color = NervaCyan, fontSize = 10.sp)
                Text("• public.multiplayer_matches (partidos 30m en tiempo real)", color = NervaGold, fontSize = 10.sp)
                Text("• public.multiplayer_match_events (goles, faltas, tarjetas)", color = Color(0xFFF472B6), fontSize = 10.sp)
                Text("• public.multiplayer_leaderboard (ranking global ELO)", color = Color(0xFF60A5FA), fontSize = 10.sp)
            }

            Text(
                text = "Archivo SQL generado en: supabase/multiplayer_schema.sql",
                color = NervaCyanAccent,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onRefresh,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NervaNeonGreen, contentColor = Color(0xFF051C14)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Verificar Conexión y Actualizar", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}
