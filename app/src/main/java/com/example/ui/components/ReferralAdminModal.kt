package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ReferralAdminModal(
    userClub: Club,
    squad: List<Player>,
    leagues: List<League>,
    isAdminUnlocked: Boolean,
    currentBackgroundTheme: AppBackgroundTheme = AppBackgroundTheme.ESTADIO_NOCTURNO,
    isMusicPlaying: Boolean = false,
    currentTrackIndex: Int = 0,
    musicVolume: Float = 0.75f,
    customAudioUrl: String = "",
    onUnlockAdmin: () -> Unit,
    onAddFunds: (Long) -> Unit,
    onSetFunds: (Long) -> Unit,
    onBoostAllStamina: () -> Unit,
    onBoostAllOverall: () -> Unit,
    onClearDisciplinaryCards: () -> Unit,
    onUpgradeFacilities: () -> Unit,
    onUpdateClubReputation: (Int) -> Unit,
    onWinNextMatchGuaranteed: () -> Unit,
    onUpdatePlayerStats: (playerId: String, newOverall: Int, newMarketValue: Long) -> Unit,
    onSetBackgroundTheme: (AppBackgroundTheme) -> Unit = {},
    onCreatePlayer: (
        name: String, position: PositionType, roleTag: String, nationality: String,
        age: Int, overall: Int, pace: Int, shooting: Int, passing: Int, dribbling: Int,
        defending: Int, physical: Int, marketValue: Long, destination: String
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _, _, _, _, _ -> },
    onCreateLeague: (
        name: String, divisionsCount: Int, maxClubs: Int, promotesFrom: String?,
        icon: String, banner: String
    ) -> Unit = { _, _, _, _, _, _ -> },
    onUpdateLeagueBannerAndIcon: (leagueId: String, banner: String, icon: String) -> Unit = { _, _, _ -> },
    onToggleMusic: () -> Unit = {},
    onNextTrack: () -> Unit = {},
    onSetMusicVolume: (Float) -> Unit = {},
    onSetCustomAudioUrl: (String) -> Unit = {},
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var referralInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccessMessage by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(if (isAdminUnlocked) 1 else 0) } // 0: Referral/Buzón, 1: Admin Panel
    var adminSubTab by remember { mutableStateOf("TRUCOS") } // "TRUCOS", "CREAR_JUGADOR", "CREAR_LIGA", "FONDOS_BANNERS", "MUSICA"
    val focusManager = LocalFocusManager.current

    // Player Creator Form State
    var newPlayerName by remember { mutableStateOf("") }
    var newPlayerPosition by remember { mutableStateOf(PositionType.DEL) }
    var newPlayerRoleTag by remember { mutableStateOf("DC") }
    var newPlayerNationality by remember { mutableStateOf("🇨🇱") }
    var newPlayerAge by remember { mutableStateOf("21") }
    var newPlayerOverall by remember { mutableStateOf("88") }
    var newPlayerPace by remember { mutableStateOf("89") }
    var newPlayerShooting by remember { mutableStateOf("88") }
    var newPlayerPassing by remember { mutableStateOf("82") }
    var newPlayerDribbling by remember { mutableStateOf("86") }
    var newPlayerDefending by remember { mutableStateOf("45") }
    var newPlayerPhysical by remember { mutableStateOf("80") }
    var newPlayerDestination by remember { mutableStateOf("SQUAD") } // "SQUAD" or "MARKET"

    // League Creator Form State
    var newLeagueName by remember { mutableStateOf("Superliga Nerva") }
    var newLeagueDivisions by remember { mutableStateOf("12") }
    var newLeagueMaxClubs by remember { mutableStateOf("30") }
    var newLeaguePromotesFrom by remember { mutableStateOf("Liga Gamma Nerva") }
    var newLeagueIcon by remember { mutableStateOf("👑") }
    var newLeagueBanner by remember { mutableStateOf("GOLD_CHAMPIONS") }

    // League Banner & Icon update
    var editLeagueIcon by remember { mutableStateOf("⚡") }
    var editLeagueBanner by remember { mutableStateOf("CYBER_NEON") }

    // Custom Audio input
    var audioUrlInput by remember { mutableStateOf(customAudioUrl) }

    fun processReferralCode() {
        val trimmed = referralInput.trim()
        if (trimmed.isEmpty()) return
        focusManager.clearFocus()

        if (trimmed.equals("iamnevulex", ignoreCase = true)) {
            onUnlockAdmin()
            activeTab = 1
            statusMessage = "🔱 ¡CÓDIGO SECRETO ACEPTADO! Acceso desbloqueado al Panel de Administración Maestro."
            isSuccessMessage = true
            referralInput = ""
            return
        }

        when (trimmed.uppercase()) {
            "GOL2026" -> {
                onAddFunds(2_500_000L)
                statusMessage = "🎉 ¡Código GOL2026 canjeado! +$2,500,000 en tu presupuesto."
                isSuccessMessage = true
            }
            "MANAGER100" -> {
                onAddFunds(5_000_000L)
                statusMessage = "🎉 ¡Código MANAGER100 canjeado! +$5,000,000 en tu presupuesto."
                isSuccessMessage = true
            }
            "NERVABONUS" -> {
                onAddFunds(10_000_000L)
                statusMessage = "🎉 ¡Código NERVABONUS canjeado! +$10,000,000 en tu presupuesto."
                isSuccessMessage = true
            }
            "CHAMPIONS" -> {
                onAddFunds(15_000_000L)
                statusMessage = "🎉 ¡Código CHAMPIONS canjeado! +$15,000,000 en tu presupuesto."
                isSuccessMessage = true
            }
            else -> {
                statusMessage = "❌ Código inválido. Introduce 'iamnevulex' para el Panel de Administrador."
                isSuccessMessage = false
            }
        }
        referralInput = ""
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .padding(4.dp)
                .testTag("referral_admin_modal"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF061126)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(
                    if (isAdminUnlocked) listOf(Color(0xFFFFD700), Color(0xFFFF8C00), NervaCyanAccent)
                    else listOf(NervaCyanAccent, NervaBlueElectric)
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isAdminUnlocked) Color(0xFFFFD700).copy(alpha = 0.2f) else NervaBlueElectric.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (isAdminUnlocked) "👑" else "✉️", fontSize = 20.sp)
                        }
                        Column {
                            Text(
                                text = if (isAdminUnlocked) "PANEL DE CONTROL MAESTRO (iamnevulex)" else "BUZÓN & CÓDIGOS DE RECOMPENSA",
                                color = if (isAdminUnlocked) Color(0xFFFFD700) else NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = if (isAdminUnlocked) "Modo Super-Admin activo • Acceso total a edición y creación" else "Canjea códigos promocionales o ingresa el código admin",
                                color = NervaCyanAccent,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = NervaWhite)
                    }
                }

                // Tab Selector (Buzón vs Admin Panel)
                if (isAdminUnlocked) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF081938))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            0 to "Buzón de Códigos",
                            1 to "👑 Panel Admin Maestro"
                        ).forEach { (idx, label) ->
                            val isSel = activeTab == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFF0F367A) else Color.Transparent)
                                    .clickable { activeTab = idx }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) Color(0xFFFFD700) else NervaTextMuted,
                                    fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFF1E355B))

                // Status Message Banner
                AnimatedVisibility(visible = statusMessage != null) {
                    statusMessage?.let { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSuccessMessage) Color(0xFF064E3B) else Color(0xFF7F1D1D))
                                .border(1.dp, if (isSuccessMessage) NervaGreen else Color(0xFFEF4444), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = msg,
                                color = NervaWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Body content based on activeTab
                if (activeTab == 0) {
                    // TAB 0: REFERRAL REDEMPTION
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF091C3E))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "CANJEAR CÓDIGO",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = referralInput,
                                        onValueChange = { referralInput = it },
                                        placeholder = { Text("Escribe 'iamnevulex' o 'GOL2026'...") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NervaWhite,
                                            unfocusedTextColor = NervaWhite,
                                            focusedBorderColor = NervaCyanAccent,
                                            unfocusedBorderColor = Color(0xFF1E355B)
                                        )
                                    )

                                    Button(
                                        onClick = { processReferralCode() },
                                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Canjear", color = NervaWhite, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }

                        // Preset Codes Help Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF081835))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("CÓDIGOS PÚBLICOS DISPONIBLES:", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                Text("• iamnevulex → Desbloquea Panel de Administrador Maestro", color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("• GOL2026 → +$2,500,000 en presupuesto", color = NervaWhite, fontSize = 11.sp)
                                Text("• MANAGER100 → +$5,000,000 en presupuesto", color = NervaWhite, fontSize = 11.sp)
                                Text("• NERVABONUS → +$10,000,000 en presupuesto", color = NervaWhite, fontSize = 11.sp)
                                Text("• CHAMPIONS → +$15,000,000 en presupuesto", color = NervaWhite, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    // TAB 1: ADMIN MASTER CONTROL PANEL (iamnevulex)
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Admin Subtabs: TRUCOS, CREAR_JUGADOR, CREAR_LIGA, FONDOS_BANNERS, MUSICA
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val subtabs = listOf(
                                "TRUCOS" to "⚡ Cheats & Finanzas",
                                "CREAR_JUGADOR" to "👤 Crear Jugador",
                                "CREAR_LIGA" to "🏆 Crear Ligas",
                                "FONDOS_BANNERS" to "🎨 Fondos & Banners",
                                "MUSICA" to "🎵 Música Personalizada"
                            )
                            items(subtabs) { (k, label) ->
                                val isSel = adminSubTab == k
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) NervaBlueElectric else Color(0xFF0A1B38))
                                        .border(1.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                        .clickable { adminSubTab = k }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSel) NervaWhite else NervaTextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Subtab Content
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            when (adminSubTab) {
                                "TRUCOS" -> {
                                    AdminCheatsSubtab(
                                        userClub = userClub,
                                        onAddFunds = onAddFunds,
                                        onBoostAllStamina = onBoostAllStamina,
                                        onBoostAllOverall = onBoostAllOverall,
                                        onClearDisciplinaryCards = onClearDisciplinaryCards,
                                        onUpgradeFacilities = onUpgradeFacilities,
                                        onWinNextMatchGuaranteed = onWinNextMatchGuaranteed
                                    )
                                }

                                "CREAR_JUGADOR" -> {
                                    AdminCreatePlayerSubtab(
                                        name = newPlayerName,
                                        onNameChange = { newPlayerName = it },
                                        position = newPlayerPosition,
                                        onPositionChange = { newPlayerPosition = it },
                                        roleTag = newPlayerRoleTag,
                                        onRoleTagChange = { newPlayerRoleTag = it },
                                        nationality = newPlayerNationality,
                                        onNationalityChange = { newPlayerNationality = it },
                                        age = newPlayerAge,
                                        onAgeChange = { newPlayerAge = it },
                                        overall = newPlayerOverall,
                                        onOverallChange = { newPlayerOverall = it },
                                        pace = newPlayerPace,
                                        onPaceChange = { newPlayerPace = it },
                                        shooting = newPlayerShooting,
                                        onShootingChange = { newPlayerShooting = it },
                                        passing = newPlayerPassing,
                                        onPassingChange = { newPlayerPassing = it },
                                        dribbling = newPlayerDribbling,
                                        onDribblingChange = { newPlayerDribbling = it },
                                        defending = newPlayerDefending,
                                        onDefendingChange = { newPlayerDefending = it },
                                        physical = newPlayerPhysical,
                                        onPhysicalChange = { newPlayerPhysical = it },
                                        destination = newPlayerDestination,
                                        onDestinationChange = { newPlayerDestination = it },
                                        onCreate = {
                                            val ov = newPlayerOverall.toIntOrNull() ?: 85
                                            val calculatedValue = (ov * 500_000L).coerceAtLeast(1_000_000L)
                                            onCreatePlayer(
                                                if (newPlayerName.isNotBlank()) newPlayerName else "Crack Nevulex",
                                                newPlayerPosition,
                                                newPlayerRoleTag,
                                                newPlayerNationality,
                                                newPlayerAge.toIntOrNull() ?: 21,
                                                ov,
                                                newPlayerPace.toIntOrNull() ?: 85,
                                                newPlayerShooting.toIntOrNull() ?: 85,
                                                newPlayerPassing.toIntOrNull() ?: 85,
                                                newPlayerDribbling.toIntOrNull() ?: 85,
                                                newPlayerDefending.toIntOrNull() ?: 85,
                                                newPlayerPhysical.toIntOrNull() ?: 85,
                                                calculatedValue,
                                                newPlayerDestination
                                            )
                                            newPlayerName = ""
                                        }
                                    )
                                }

                                "CREAR_LIGA" -> {
                                    AdminCreateLeagueSubtab(
                                        leagueName = newLeagueName,
                                        onLeagueNameChange = { newLeagueName = it },
                                        divisions = newLeagueDivisions,
                                        onDivisionsChange = { newLeagueDivisions = it },
                                        maxClubs = newLeagueMaxClubs,
                                        onMaxClubsChange = { newLeagueMaxClubs = it },
                                        promotesFrom = newLeaguePromotesFrom,
                                        onPromotesFromChange = { newLeaguePromotesFrom = it },
                                        icon = newLeagueIcon,
                                        onIconChange = { newLeagueIcon = it },
                                        banner = newLeagueBanner,
                                        onBannerChange = { newLeagueBanner = it },
                                        leagues = leagues,
                                        onCreate = {
                                            onCreateLeague(
                                                newLeagueName,
                                                newLeagueDivisions.toIntOrNull() ?: 12,
                                                newLeagueMaxClubs.toIntOrNull() ?: 30,
                                                newLeaguePromotesFrom,
                                                newLeagueIcon,
                                                newLeagueBanner
                                            )
                                        }
                                    )
                                }

                                "FONDOS_BANNERS" -> {
                                    AdminBackgroundsBannersSubtab(
                                        currentTheme = currentBackgroundTheme,
                                        onSelectTheme = onSetBackgroundTheme,
                                        activeLeague = leagues.firstOrNull { it.id == "gamma_nerva" } ?: leagues.firstOrNull(),
                                        onUpdateLeagueBanner = { banner, icon ->
                                            leagues.firstOrNull()?.let {
                                                onUpdateLeagueBannerAndIcon(it.id, banner, icon)
                                            }
                                        }
                                    )
                                }

                                "MUSICA" -> {
                                    AdminMusicSubtab(
                                        isPlaying = isMusicPlaying,
                                        trackIndex = currentTrackIndex,
                                        volume = musicVolume,
                                        customUrl = audioUrlInput,
                                        onCustomUrlChange = { audioUrlInput = it },
                                        onToggle = onToggleMusic,
                                        onNext = onNextTrack,
                                        onVolumeChange = onSetMusicVolume,
                                        onSaveUrl = { onSetCustomAudioUrl(audioUrlInput) }
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

@Composable
private fun AdminCheatsSubtab(
    userClub: Club,
    onAddFunds: (Long) -> Unit,
    onBoostAllStamina: () -> Unit,
    onBoostAllOverall: () -> Unit,
    onClearDisciplinaryCards: () -> Unit,
    onUpgradeFacilities: () -> Unit,
    onWinNextMatchGuaranteed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Quick Cheats Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onAddFunds(50_000_000L) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F367A)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+$50M Presupuesto", color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Black)
            }

            Button(
                onClick = onBoostAllStamina,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF065F46)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("⚡ 100% Energía", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onBoostAllOverall,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C2D12)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🌟 +5 Media Plantilla", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }

            Button(
                onClick = onClearDisciplinaryCards,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🧹 Limpiar Sanciones", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onUpgradeFacilities,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C1D95)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🏗️ Mejorar Estadio", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onWinNextMatchGuaranteed,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("🏆 Victoria Asegurada", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun AdminCreatePlayerSubtab(
    name: String, onNameChange: (String) -> Unit,
    position: PositionType, onPositionChange: (PositionType) -> Unit,
    roleTag: String, onRoleTagChange: (String) -> Unit,
    nationality: String, onNationalityChange: (String) -> Unit,
    age: String, onAgeChange: (String) -> Unit,
    overall: String, onOverallChange: (String) -> Unit,
    pace: String, onPaceChange: (String) -> Unit,
    shooting: String, onShootingChange: (String) -> Unit,
    passing: String, onPassingChange: (String) -> Unit,
    dribbling: String, onDribblingChange: (String) -> Unit,
    defending: String, onDefendingChange: (String) -> Unit,
    physical: String, onPhysicalChange: (String) -> Unit,
    destination: String, onDestinationChange: (String) -> Unit,
    onCreate: () -> Unit
) {
    val flags = listOf("🇨🇱", "🇦🇷", "🇧🇷", "🇺🇾", "🇨🇴", "🇪🇸", "🇫🇷", "🇩🇪", "🇵🇹", "🇬🇧", "🇲🇽", "🇯🇵")
    val roles = listOf("POR", "DFC", "LI", "LD", "MCD", "MC", "MCO", "EI", "ED", "DC")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nombre del Jugador") },
                modifier = Modifier.weight(1.5f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
            )

            OutlinedTextField(
                value = age,
                onValueChange = onAgeChange,
                label = { Text("Edad") },
                modifier = Modifier.weight(0.7f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
            )
        }

        // Role & Position selection
        Text("POSICIÓN & ROL:", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(roles) { r ->
                val isSel = roleTag == r
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) NervaBlueElectric else Color(0xFF0A1B38))
                        .border(1.dp, if (isSel) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(6.dp))
                        .clickable {
                            onRoleTagChange(r)
                            when (r) {
                                "POR" -> onPositionChange(PositionType.POR)
                                "DFC", "LI", "LD" -> onPositionChange(PositionType.DEF)
                                "MCD", "MC", "MCO" -> onPositionChange(PositionType.MED)
                                else -> onPositionChange(PositionType.DEL)
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(r, color = if (isSel) NervaWhite else NervaTextMuted, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        // Nationality Flag
        Text("NACIONALIDAD:", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(flags) { f ->
                val isSel = nationality == f
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isSel) Color(0xFF0F367A) else Color(0xFF0A1B38))
                        .border(1.5.dp, if (isSel) Color(0xFFFFD700) else Color.Transparent, CircleShape)
                        .clickable { onNationalityChange(f) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(f, fontSize = 16.sp)
                }
            }
        }

        // Stats grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(value = overall, onValueChange = onOverallChange, label = { Text("MEDIA") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = pace, onValueChange = onPaceChange, label = { Text("RIT") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = shooting, onValueChange = onShootingChange, label = { Text("TIR") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = passing, onValueChange = onPassingChange, label = { Text("PAS") }, modifier = Modifier.weight(1f), singleLine = true)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(value = dribbling, onValueChange = onDribblingChange, label = { Text("REG") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = defending, onValueChange = onDefendingChange, label = { Text("DEF") }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = physical, onValueChange = onPhysicalChange, label = { Text("FIS") }, modifier = Modifier.weight(1f), singleLine = true)
        }

        // Destination
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onDestinationChange("SQUAD") },
                colors = ButtonDefaults.buttonColors(containerColor = if (destination == "SQUAD") NervaBlueElectric else Color(0xFF1E293B)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Añadir a Mi Plantilla", fontSize = 11.sp, color = NervaWhite)
            }
            Button(
                onClick = { onDestinationChange("MARKET") },
                colors = ButtonDefaults.buttonColors(containerColor = if (destination == "MARKET") NervaBlueElectric else Color(0xFF1E293B)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Añadir al Mercado", fontSize = 11.sp, color = NervaWhite)
            }
        }

        Button(
            onClick = onCreate,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("⚡ CREAR JUGADOR AHORA", fontWeight = FontWeight.Black, color = NervaWhite)
        }
    }
}

@Composable
private fun AdminCreateLeagueSubtab(
    leagueName: String, onLeagueNameChange: (String) -> Unit,
    divisions: String, onDivisionsChange: (String) -> Unit,
    maxClubs: String, onMaxClubsChange: (String) -> Unit,
    promotesFrom: String, onPromotesFromChange: (String) -> Unit,
    icon: String, onIconChange: (String) -> Unit,
    banner: String, onBannerChange: (String) -> Unit,
    leagues: List<League>,
    onCreate: () -> Unit
) {
    val icons = listOf("🏆", "⚡", "👑", "🛡️", "🔥", "🌟", "⚽", "🌍")
    val bannerOptions = listOf("CYBER_NEON" to "Cyber Neón", "GOLD_CHAMPIONS" to "Oro Champions", "EMERALD_PITCH" to "Césped Esmeralda")
    val promoteOptions = listOf("Liga Gamma Nerva", "Liga Delta Regional", "Liga Beta Premier", "Ninguna (Liga Base)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = leagueName,
            onValueChange = onLeagueNameChange,
            label = { Text("Nombre de la Liga") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = divisions,
                onValueChange = onDivisionsChange,
                label = { Text("Nº Divisiones (ej: 12)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
            )

            OutlinedTextField(
                value = maxClubs,
                onValueChange = onMaxClubsChange,
                label = { Text("Máx Clubes / Div (ej: 30)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
            )
        }

        // ¿De qué liga se asciende?
        Text("¿DE QUÉ LIGA SE ASCIENDE?", color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Black)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(promoteOptions) { opt ->
                val isSel = promotesFrom == opt
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) NervaBlueElectric else Color(0xFF0A1B38))
                        .border(1.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF1E355B), RoundedCornerShape(6.dp))
                        .clickable { onPromotesFromChange(opt) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(opt, color = if (isSel) NervaWhite else NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Icono de Liga
        Text("ICONO DE LIGA:", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(icons) { ic ->
                val isSel = icon == ic
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSel) Color(0xFF0F367A) else Color(0xFF0A1B38))
                        .border(1.5.dp, if (isSel) Color(0xFFFFD700) else Color.Transparent, CircleShape)
                        .clickable { onIconChange(ic) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(ic, fontSize = 18.sp)
                }
            }
        }

        // Banner de Liga
        Text("BANNER DE LIGA:", color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(bannerOptions) { (key, label) ->
                val isSel = banner == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) NervaBlueElectric else Color(0xFF0A1B38))
                        .border(1.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF1E355B), RoundedCornerShape(6.dp))
                        .clickable { onBannerChange(key) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(label, color = if (isSel) NervaWhite else NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Button(
            onClick = onCreate,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3AE8)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("🏆 CREAR LIGA Y SUS DIVISIONES", fontWeight = FontWeight.Black, color = NervaWhite)
        }
    }
}

@Composable
private fun AdminBackgroundsBannersSubtab(
    currentTheme: AppBackgroundTheme,
    onSelectTheme: (AppBackgroundTheme) -> Unit,
    activeLeague: League?,
    onUpdateLeagueBanner: (String, String) -> Unit
) {
    var selectedBanner by remember { mutableStateOf(activeLeague?.bannerStyle ?: "CYBER_NEON") }
    var selectedIcon by remember { mutableStateOf(activeLeague?.iconEmoji ?: "⚡") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("FONDO DE PANTALLA PRINCIPAL DE LA APP:", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)

        // 6 Preset themes
        AppBackgroundTheme.values().forEach { theme ->
            val isSel = currentTheme == theme
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelectTheme(theme) }
                    .border(
                        1.dp,
                        if (isSel) Color(0xFFFFD700) else Color(0xFF1E355B),
                        RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(theme.secondaryColorHex)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(theme.previewIcon, fontSize = 22.sp)
                        Column {
                            Text(theme.title, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(theme.subtitle, color = NervaTextMuted, fontSize = 10.sp)
                        }
                    }

                    if (isSel) {
                        Text("ACTIVO ✓", color = Color(0xFFFFD700), fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }

        HorizontalDivider(color = Color(0xFF1E355B))

        // Banner e Icono de Liga Gamma Nerva
        Text("BANNER E ICONO DE LIGA GAMMA NERVA:", color = NervaWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icons = listOf("⚡", "🏆", "👑", "🔥", "⚽", "🌟")
            icons.forEach { ic ->
                val isSel = selectedIcon == ic
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSel) Color(0xFF0F367A) else Color(0xFF0A1B38))
                        .border(1.5.dp, if (isSel) Color(0xFFFFD700) else Color.Transparent, CircleShape)
                        .clickable {
                            selectedIcon = ic
                            onUpdateLeagueBanner(selectedBanner, ic)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(ic, fontSize = 18.sp)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "CYBER_NEON" to "⚡ Cyber Neón",
                "GOLD_CHAMPIONS" to "👑 Oro Champions",
                "EMERALD_PITCH" to "🌱 Césped Esmeralda"
            ).forEach { (bStyle, label) ->
                val isSel = selectedBanner == bStyle
                Button(
                    onClick = {
                        selectedBanner = bStyle
                        onUpdateLeagueBanner(bStyle, selectedIcon)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSel) NervaBlueElectric else Color(0xFF1E293B)
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(label, fontSize = 10.sp, color = NervaWhite)
                }
            }
        }
    }
}

@Composable
private fun AdminMusicSubtab(
    isPlaying: Boolean,
    trackIndex: Int,
    volume: Float,
    customUrl: String,
    onCustomUrlChange: (String) -> Unit,
    onToggle: () -> Unit,
    onNext: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSaveUrl: () -> Unit
) {
    val tracks = listOf(
        "Nerva Official Anthem — Gloria y Honor (Sinfonía)",
        "Cyber Kickoff 2026 — Synthwave Electro Bass",
        "Stadium Techno Beat — Warmup Arena Mix",
        "Champions Locker Room — Chill Acoustic Lo-Fi"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1E40))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("REPRODUCTOR DE MÚSICA & AMBIENTE OFICIAL", color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)

                // Current Track Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) Color(0xFF059669) else Color(0xFF334155)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isPlaying) "🔊" else "🔈", fontSize = 22.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tracks.getOrElse(trackIndex) { "Canción del Estadio" },
                            color = NervaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (isPlaying) "Reproduciendo en vivo • Ecualizador activo" else "Pausado • Toca Reproducir",
                            color = if (isPlaying) NervaGreen else NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // Controls: Play / Pause / Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onToggle,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isPlaying) Color(0xFFDC2626) else Color(0xFF059669)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isPlaying) "Pausar Música" else "Reproducir Música ▶", color = NervaWhite, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onNext,
                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Siguiente Pista ⏭", color = NervaWhite, fontWeight = FontWeight.Bold)
                    }
                }

                // Volume Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Volumen del Estadio:", color = NervaTextMuted, fontSize = 11.sp)
                        Text("${(volume * 100).toInt()}%", color = NervaCyanAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Slider(
                        value = volume,
                        onValueChange = onVolumeChange,
                        colors = SliderDefaults.colors(
                            thumbColor = NervaCyanAccent,
                            activeTrackColor = NervaBlueElectric
                        )
                    )
                }
            }
        }

        // Custom Stream URL
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF091630))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("STREAMING DE AUDIO PERSONALIZADO (URL):", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customUrl,
                        onValueChange = onCustomUrlChange,
                        placeholder = { Text("https://audio.stream/nerva.mp3") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = NervaWhite, unfocusedTextColor = NervaWhite)
                    )
                    Button(
                        onClick = onSaveUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Guardar", color = NervaWhite)
                    }
                }
            }
        }
    }
}
