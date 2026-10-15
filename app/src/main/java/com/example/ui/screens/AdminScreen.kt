package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun AdminScreen(
    userClub: Club,
    squad: List<Player>,
    leagues: List<League>,
    currentBackgroundTheme: AppBackgroundTheme,
    isMusicPlaying: Boolean,
    currentTrackIndex: Int,
    musicVolume: Float,
    customAudioUrl: String,
    onAddFunds: (Long) -> Unit,
    onSetFunds: (Long) -> Unit,
    onBoostAllStamina: () -> Unit,
    onBoostAllOverall: () -> Unit,
    onClearDisciplinaryCards: () -> Unit,
    onUpgradeFacilities: () -> Unit,
    onUpdateClubReputation: (Int) -> Unit,
    onWinNextMatchGuaranteed: () -> Unit,
    onUpdatePlayerStats: (String, Int, Long) -> Unit,
    onSetBackgroundTheme: (AppBackgroundTheme) -> Unit,
    onCreatePlayer: (
        name: String,
        pos: String,
        role: String,
        nat: String,
        age: Int,
        ov: Int,
        pac: Int,
        sho: Int,
        pas: Int,
        dri: Int,
        def: Int,
        phy: Int,
        valM: Long,
        dest: String
    ) -> Unit,
    onCreateLeague: (
        name: String,
        divs: Int,
        maxClubs: Int,
        promotesFrom: String?,
        icon: String,
        banner: String
    ) -> Unit,
    onUpdateLeagueBannerAndIcon: (String, String, String) -> Unit,
    onToggleMusic: () -> Unit,
    onNextTrack: () -> Unit,
    onSetMusicVolume: (Float) -> Unit,
    onSetCustomAudioUrl: (String) -> Unit,
    onSelectTrack: (Int) -> Unit = {},
    onPlayWhistle: () -> Unit = {},
    onPlayGoalHorn: () -> Unit = {},
    onPlayCrowdCheer: () -> Unit = {},
    onPreviewLoadingScreen: () -> Unit = {},
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("CHEATS") } // CHEATS, THEMES, PLAYER, LEAGUE, AUDIO

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .testTag("admin_screen_exclusive")
    ) {
        // 1. Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF7F1D1D), Color(0xFF1E1B4B), Color(0xFF0F172A))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = { onNavigate(ScreenDestination.HOME) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFFF87171)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "PANEL ADMIN & CONTROL TOTAL",
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                letterSpacing = 1.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFEF4444).copy(alpha = 0.3f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "MODO DIOS",
                                    color = Color(0xFFFCA5A5),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = "Presupuestos, fondos, creador de jugadores, creación de ligas con ascensos, música y fondos",
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // Quick Navigation Tabs
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf(
                        Pair("CHEATS", "⚡ Trucos"),
                        Pair("THEMES", "🎨 Fondos"),
                        Pair("PLAYER", "👤 Jugador"),
                        Pair("LEAGUE", "🏆 Ligas"),
                        Pair("AUDIO", "🎵 Audio")
                    )
                    tabs.forEach { (key, label) ->
                        val isSel = activeTab == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) Color(0xFFEF4444) else Color.Transparent)
                                .clickable { activeTab = key }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSel) NervaWhite else NervaTextMuted,
                                fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            when (activeTab) {
                "CHEATS" -> CheatsTab(
                    userClub = userClub,
                    onAddFunds = onAddFunds,
                    onSetFunds = onSetFunds,
                    onBoostAllStamina = onBoostAllStamina,
                    onBoostAllOverall = onBoostAllOverall,
                    onClearDisciplinaryCards = onClearDisciplinaryCards,
                    onUpgradeFacilities = onUpgradeFacilities,
                    onWinNextMatchGuaranteed = onWinNextMatchGuaranteed
                )

                "THEMES" -> ThemesTab(
                    currentBackgroundTheme = currentBackgroundTheme,
                    leagues = leagues,
                    onSetBackgroundTheme = onSetBackgroundTheme,
                    onUpdateLeagueBannerAndIcon = onUpdateLeagueBannerAndIcon
                )

                "PLAYER" -> PlayerCreatorTab(
                    onCreatePlayer = onCreatePlayer
                )

                "LEAGUE" -> LeagueCreatorTab(
                    existingLeagues = leagues,
                    onCreateLeague = onCreateLeague
                )

                "AUDIO" -> AudioTab(
                    isMusicPlaying = isMusicPlaying,
                    currentTrackIndex = currentTrackIndex,
                    musicVolume = musicVolume,
                    customAudioUrl = customAudioUrl,
                    onToggleMusic = onToggleMusic,
                    onNextTrack = onNextTrack,
                    onSelectTrack = onSelectTrack,
                    onSetMusicVolume = onSetMusicVolume,
                    onSetCustomAudioUrl = onSetCustomAudioUrl,
                    onPlayWhistle = onPlayWhistle,
                    onPlayGoalHorn = onPlayGoalHorn,
                    onPlayCrowdCheer = onPlayCrowdCheer,
                    onPreviewLoadingScreen = onPreviewLoadingScreen
                )
            }
        }
    }
}

@Composable
private fun CheatsTab(
    userClub: Club,
    onAddFunds: (Long) -> Unit,
    onSetFunds: (Long) -> Unit,
    onBoostAllStamina: () -> Unit,
    onBoostAllOverall: () -> Unit,
    onClearDisciplinaryCards: () -> Unit,
    onUpgradeFacilities: () -> Unit,
    onWinNextMatchGuaranteed: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("FINANZAS & PRESUPUESTO ILIMITADO", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 13.sp)

            Text("Presupuesto Actual: €${userClub.budget / 1_000_000}M • Monedas de Club: ${userClub.coins}", color = NervaWhite, fontSize = 12.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { onAddFunds(50_000_000L) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = NervaBlueDeep)
                ) {
                    Text("+€50M Fichajes", fontWeight = FontWeight.Black)
                }
                Button(
                    onClick = { onAddFunds(200_000_000L) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = NervaBlueDeep)
                ) {
                    Text("+€200M Jeque", fontWeight = FontWeight.Black)
                }
                Button(
                    onClick = { onSetFunds(1_000_000_000L) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1), contentColor = NervaWhite)
                ) {
                    Text("€1.000 Millones (Infinito)", fontWeight = FontWeight.Black)
                }
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            Text("GESTIÓN DE LA PLANTILLA & ESTADÍSTICAS", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 13.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onBoostAllStamina,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7), contentColor = NervaWhite)
                ) {
                    Text("⚡ Recuperar 100% Energía", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBoostAllOverall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6), contentColor = NervaWhite)
                ) {
                    Text("🌟 +5 Media a Todos los Jugadores", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onClearDisciplinaryCards,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48), contentColor = NervaWhite)
                ) {
                    Text("🛡 Limpiar Tarjetas Rojas y Amarillas", fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            Text("INSTALACIONES Y PRÓXIMO PARTIDO", color = Color(0xFFF97316), fontWeight = FontWeight.Black, fontSize = 13.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onUpgradeFacilities,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14B8A6), contentColor = NervaBlueDeep)
                ) {
                    Text("🏟 Mejorar Instalaciones al Máximo", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onWinNextMatchGuaranteed,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = NervaBlueDeep)
                ) {
                    Text("🏆 Victoria Garantizada en Próximo Partido", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun ThemesTab(
    currentBackgroundTheme: AppBackgroundTheme,
    leagues: List<League>,
    onSetBackgroundTheme: (AppBackgroundTheme) -> Unit,
    onUpdateLeagueBannerAndIcon: (String, String, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("TEMAS DE FONDO DEL VIDEOJUEGO", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 13.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AppBackgroundTheme.values().forEach { theme ->
                    val isSel = currentBackgroundTheme == theme
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSetBackgroundTheme(theme) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(theme.primaryColorHex)),
                        border = if (isSel) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NervaCyanAccent, Color(0xFFFFD700)))) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(theme.previewIcon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(theme.title, color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 11.sp)
                            Text(theme.subtitle, color = NervaTextMuted, fontSize = 9.sp, maxLines = 1)
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            Text("PERSONALIZACIÓN DE ICONOS Y BANNERS DE LIGA", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 13.sp)

            leagues.forEach { league ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF07142E))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(league.name, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Banner: ${league.bannerStyle} • Icono: ${league.iconEmoji}", color = NervaTextMuted, fontSize = 10.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("⚡", "🏆", "👑", "🔥", "⚽").forEach { ic ->
                            Button(
                                onClick = { onUpdateLeagueBannerAndIcon(league.id, league.bannerStyle, ic) },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(34.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                            ) {
                                Text(ic, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerCreatorTab(
    onCreatePlayer: (
        name: String, pos: String, role: String, nat: String,
        age: Int, ov: Int, pac: Int, sho: Int, pas: Int,
        dri: Int, def: Int, phy: Int, valM: Long, dest: String
    ) -> Unit
) {
    var pName by remember { mutableStateOf("Galáctico Nerva") }
    var pPos by remember { mutableStateOf("DEL") }
    var pNat by remember { mutableStateOf("🇪🇸") }
    var pAge by remember { mutableStateOf("21") }
    var pOv by remember { mutableStateOf("94") }
    var pPac by remember { mutableStateOf("96") }
    var pSho by remember { mutableStateOf("95") }
    var pPas by remember { mutableStateOf("92") }
    var pDri by remember { mutableStateOf("95") }
    var pDef by remember { mutableStateOf("60") }
    var pPhy by remember { mutableStateOf("88") }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("CREADOR EXCLUSIVO DE JUGADORES", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 13.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = pName,
                    onValueChange = { pName = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.weight(2f),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
                )
                OutlinedTextField(
                    value = pNat,
                    onValueChange = { pNat = it },
                    label = { Text("Bandera") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
                )
                OutlinedTextField(
                    value = pAge,
                    onValueChange = { pAge = it },
                    label = { Text("Edad") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                val positions = listOf("DEL", "MED", "DEF", "POR")
                positions.forEach { pos ->
                    val isSel = pPos == pos
                    Button(
                        onClick = { pPos = pos },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSel) NervaCyanAccent else Color(0xFF1E293B),
                            contentColor = if (isSel) NervaBlueDeep else NervaWhite
                        )
                    ) {
                        Text(pos, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text("Estadísticas (0-99):", color = NervaTextMuted, fontSize = 11.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = pOv, onValueChange = { pOv = it }, label = { Text("OVR") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pPac, onValueChange = { pPac = it }, label = { Text("RIT") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pSho, onValueChange = { pSho = it }, label = { Text("TIR") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pPas, onValueChange = { pPas = it }, label = { Text("PAS") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pDri, onValueChange = { pDri = it }, label = { Text("REG") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pDef, onValueChange = { pDef = it }, label = { Text("DEF") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = pPhy, onValueChange = { pPhy = it }, label = { Text("FIS") }, modifier = Modifier.weight(1f))
            }

            Button(
                onClick = {
                    onCreatePlayer(
                        pName, pPos, "Estrella", pNat,
                        pAge.toIntOrNull() ?: 21,
                        pOv.toIntOrNull() ?: 90,
                        pPac.toIntOrNull() ?: 90,
                        pSho.toIntOrNull() ?: 90,
                        pPas.toIntOrNull() ?: 90,
                        pDri.toIntOrNull() ?: 90,
                        pDef.toIntOrNull() ?: 60,
                        pPhy.toIntOrNull() ?: 80,
                        120_000_000L,
                        "SQUAD"
                    )
                },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = NervaBlueDeep)
            ) {
                Text("CREAR E INCORPORAR A MI PLANTILLA (NERVA FC)", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun LeagueCreatorTab(
    existingLeagues: List<League>,
    onCreateLeague: (
        name: String, divs: Int, maxClubs: Int,
        promotesFrom: String?, icon: String, banner: String
    ) -> Unit
) {
    var lName by remember { mutableStateOf("Liga Alpha Imperial") }
    var lDivs by remember { mutableStateOf("12") }
    var lMaxClubs by remember { mutableStateOf("30") }
    var selectedPromotesFrom by remember { mutableStateOf<String?>(existingLeagues.firstOrNull()?.name) }
    var lIcon by remember { mutableStateOf("👑") }
    var lBanner by remember { mutableStateOf("GOLD_CHAMPION") }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("CREADOR DE LIGAS Y SISTEMA DE ASCENSOS", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 13.sp)

            OutlinedTextField(
                value = lName,
                onValueChange = { lName = it },
                label = { Text("Nombre de la Nueva Liga") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = lDivs,
                    onValueChange = { lDivs = it },
                    label = { Text("N° Divisiones (ej: 12)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = lMaxClubs,
                    onValueChange = { lMaxClubs = it },
                    label = { Text("Máx Clubes/Div (ej: 30)") },
                    modifier = Modifier.weight(1f)
                )
            }

            // CRITICAL USER REQUIREMENT: ¿De qué liga se asciende?
            Text("¿DE QUÉ LIGA SE ASCIENDE? (ORIGEN DE ASCENSO):", color = NervaCyanAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(existingLeagues) { leg ->
                    val isSel = selectedPromotesFrom == leg.name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NervaCyanAccent else Color(0xFF1E293B))
                            .clickable { selectedPromotesFrom = leg.name }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Asciende desde: ${leg.name}",
                            color = if (isSel) NervaBlueDeep else NervaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Text("Icono de la Liga:", color = NervaTextMuted, fontSize = 11.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("👑", "⚡", "🏆", "🔥", "🌟", "🛡️").forEach { ic ->
                    val isSel = lIcon == ic
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSel) Color(0xFFFFD700) else Color(0xFF1E293B))
                            .clickable { lIcon = ic },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(ic, fontSize = 18.sp)
                    }
                }
            }

            Button(
                onClick = {
                    onCreateLeague(
                        lName,
                        lDivs.toIntOrNull() ?: 12,
                        lMaxClubs.toIntOrNull() ?: 30,
                        selectedPromotesFrom,
                        lIcon,
                        lBanner
                    )
                },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = NervaBlueDeep)
            ) {
                Text("CREAR LIGA OFICIAL CON ASCENSO DIRECTO", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AudioTab(
    isMusicPlaying: Boolean,
    currentTrackIndex: Int,
    musicVolume: Float,
    customAudioUrl: String,
    onToggleMusic: () -> Unit,
    onNextTrack: () -> Unit,
    onSelectTrack: (Int) -> Unit,
    onSetMusicVolume: (Float) -> Unit,
    onSetCustomAudioUrl: (String) -> Unit,
    onPlayWhistle: () -> Unit,
    onPlayGoalHorn: () -> Unit,
    onPlayCrowdCheer: () -> Unit,
    onPreviewLoadingScreen: () -> Unit
) {
    var audioUrlInput by remember { mutableStateOf(customAudioUrl) }

    val presetTracks = listOf(
        Triple("👑", "Himno Nerva Champions", "Épica sinfónica orquestal con metales y marcha triunfal"),
        Triple("⚡", "Electro Matchday Hype", "Pumping trap & synth bass a 130 BPM estilo EA Sports FC"),
        Triple("🕹️", "Retro Stadium 16-Bit", "Chiptune arcade vintage nostálgico de consolas clásicas"),
        Triple("🇧🇷", "Samba de Janeiro", "Ritmo de batucada carnavalera, percusión viva y fiesta de gol")
    )

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "SISTEMA DE MÚSICA & AUDIO DEL JUEGO",
                        color = NervaCyanAccent,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "Configura bandas sonoras, streams MP3, volumen y efectos de sonido en vivo",
                        color = NervaTextMuted,
                        fontSize = 11.sp
                    )
                }

                // Test Loading Screen Button
                Button(
                    onClick = onPreviewLoadingScreen,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("🔄 Ver Pantalla de Carga", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Current Player Card with Equalizer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0E1F42))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isMusicPlaying) Color(0xFF10B981) else Color(0xFF334155)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isMusicPlaying) "🔊" else "🔈", fontSize = 24.sp)
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (isMusicPlaying) "MÚSICA REPRODUCIÉNDOSE" else "MÚSICA PAUSADA",
                                    color = if (isMusicPlaying) Color(0xFF34D399) else NervaTextMuted,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp
                                )

                                // Animated Equalizer visualizer
                                if (isMusicPlaying) {
                                    AnimatedEqualizer()
                                }
                            }

                            val trackName = if (customAudioUrl.isNotBlank() && currentTrackIndex == 4) {
                                "Stream Personalizado: $customAudioUrl"
                            } else {
                                presetTracks.getOrNull(currentTrackIndex)?.second ?: "Himno Nerva Champions"
                            }
                            Text(
                                text = trackName,
                                color = NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Play/Pause & Next Buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onToggleMusic,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isMusicPlaying) Color(0xFFEF4444) else Color(0xFF10B981)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (isMusicPlaying) "⏸ Pausar" else "▶ Reproducir",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = onNextTrack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("⏭ Siguiente", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Volume Control Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1B36), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Volumen General del Juego:", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("${(musicVolume * 100).toInt()}%", color = NervaCyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
                Slider(
                    value = musicVolume,
                    onValueChange = onSetMusicVolume,
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = NervaCyanAccent,
                        activeTrackColor = NervaCyanAccent,
                        inactiveTrackColor = Color(0xFF1E293B)
                    )
                )
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            // Presets Soundtracks Selection Grid
            Text(
                "BANDAS SONORAS PREESTABLECIDAS (TOCA PARA ASIGNAR):",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )

            presetTracks.forEachIndexed { idx, (icon, title, desc) ->
                val isCurrent = isMusicPlaying && currentTrackIndex == idx
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isCurrent) Color(0xFF142B59) else Color(0xFF0C1830))
                        .border(
                            1.dp,
                            if (isCurrent) NervaCyanAccent else Color(0xFF1E293B),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onSelectTrack(idx) }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(icon, fontSize = 24.sp)
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Pista ${idx + 1}: $title",
                                        color = if (isCurrent) NervaCyanAccent else NervaWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    if (isCurrent) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF10B981).copy(alpha = 0.25f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("SONANDO AHORA", color = Color(0xFF34D399), fontSize = 9.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                                Text(desc, color = NervaTextMuted, fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = { onSelectTrack(idx) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCurrent) Color(0xFF10B981) else Color(0xFF1E3A8A)
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isCurrent) "Sonando 🎶" else "Poner Música ▶",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            // Custom Audio URL & Stream
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C1830), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1E355B), RoundedCornerShape(10.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "MÚSICA PERSONALIZADA (URL / STREAM MP3 EN VIVO):",
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
                Text(
                    "Introduce cualquier enlace de audio directo (HTTP/HTTPS) para escuchar tu propia música durante los partidos y menús:",
                    color = NervaTextMuted,
                    fontSize = 11.sp
                )

                OutlinedTextField(
                    value = audioUrlInput,
                    onValueChange = { audioUrlInput = it },
                    placeholder = { Text("https://ejemplo.com/mi_musica_de_futbol.mp3", color = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NervaWhite,
                        unfocusedTextColor = NervaWhite,
                        focusedBorderColor = NervaCyanAccent,
                        unfocusedBorderColor = Color(0xFF334155)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSetCustomAudioUrl(audioUrlInput) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            contentColor = NervaBlueDeep
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("💾 Guardar y Asignar Música Personalizada", fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }

                    if (audioUrlInput.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                audioUrlInput = ""
                                onSetCustomAudioUrl("")
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Quitar URL", color = Color(0xFFEF4444), fontSize = 11.sp)
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF1E355B))

            // Sound Effects Soundboard
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C1830), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("EFECTOS DE ESTADIO (PROBAR SONIDOS EN VIVO):", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onPlayWhistle,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("📢 Silbato Árbitro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onPlayGoalHorn,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🚨 Bocina de Gol", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onPlayCrowdCheer,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🏟️ Rugido Estadio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Animated sound equalizer visualizer bars.
 */
@Composable
private fun AnimatedEqualizer() {
    val infiniteTransition = rememberInfiniteTransition(label = "eq")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(250, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 16f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(320, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f, targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(200, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 18f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(280, easing = LinearEasing), RepeatMode.Reverse),
        label = "h4"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(20.dp)
    ) {
        listOf(h1, h2, h3, h4).forEach { heightVal ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(heightVal.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color(0xFF34D399))
            )
        }
    }
}
