package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.OverallBadge
import com.example.ui.components.PitchBoardView
import com.example.ui.components.PlayerItemRow
import com.example.ui.theme.*

@Composable
fun TacticsScreen(
    tactic: TacticConfig,
    squad: List<Player>,
    playerToSwap: Player?,
    onSelectFormation: (Formation) -> Unit,
    onSelectPlayStyle: (PlayStyle) -> Unit,
    onSelectMentality: (Mentality) -> Unit,
    onSelectPressing: (PressingIntensity) -> Unit,
    onStartSwap: (Player) -> Unit,
    onCompleteSwap: (Player) -> Unit,
    onCancelSwap: () -> Unit,
    onInspectPlayer: (Player) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val starters = squad.filter { it.isStarter }
    val bench = squad.filter { !it.isStarter }

    var selectedSubTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("Campo & Alineación", "Instrucciones", "Suplentes (${bench.size})")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
    ) {
        // Swap Banner when swap is active
        if (playerToSwap != null) {
            Surface(
                color = NervaCyan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CAMBIANDO JUGADOR",
                            color = NervaBlueDeep,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Toca otro jugador para sustituir a ${playerToSwap.name}",
                            color = NervaBlueDeep,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onCancelSwap) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar",
                            tint = NervaBlueDeep
                        )
                    }
                }
            }
        }

        // Subtabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = NervaBlueSurface,
            contentColor = NervaCyan,
            divider = {}
        ) {
            subTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    modifier = Modifier.testTag("tactic_tab_$index"),
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedSubTab == index) NervaCyan else NervaTextMuted
                        )
                    }
                )
            }
        }

        when (selectedSubTab) {
            0 -> AlignmentTab(
                tactic = tactic,
                starters = starters,
                playerToSwap = playerToSwap,
                onSelectFormation = onSelectFormation,
                onPlayerClick = { player ->
                    if (playerToSwap == null) {
                        onStartSwap(player)
                    } else {
                        onCompleteSwap(player)
                    }
                }
            )
            1 -> InstructionsTab(
                tactic = tactic,
                onSelectPlayStyle = onSelectPlayStyle,
                onSelectMentality = onSelectMentality,
                onSelectPressing = onSelectPressing
            )
            2 -> BenchTab(
                bench = bench,
                playerToSwap = playerToSwap,
                onPlayerClick = { player ->
                    if (playerToSwap != null) {
                        onCompleteSwap(player)
                    } else {
                        onStartSwap(player)
                    }
                },
                onInspectPlayer = onInspectPlayer
            )
        }
    }
}

@Composable
private fun AlignmentTab(
    tactic: TacticConfig,
    starters: List<Player>,
    playerToSwap: Player?,
    onSelectFormation: (Formation) -> Unit,
    onPlayerClick: (Player) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Formation Selector Pills
        item {
            Text(
                text = "SISTEMA TÁCTICO",
                color = NervaCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Formation.values().forEach { form ->
                    val isSelected = tactic.formation == form
                    Button(
                        onClick = { onSelectFormation(form) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("formation_${form.displayName}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) NervaCyan else NervaBlueSurface,
                            contentColor = if (isSelected) NervaBlueDeep else NervaWhite
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = form.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Tactical 2D Pitch Board
        item {
            PitchBoardView(
                formation = tactic.formation,
                starterPlayers = starters,
                selectedPlayerId = playerToSwap?.id,
                onPlayerClick = onPlayerClick
            )
        }

        // Quick hint
        item {
            Text(
                text = "💡 Toca cualquier jugador del tablero para intercambiarlo con otro titular o suplente.",
                color = NervaTextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun InstructionsTab(
    tactic: TacticConfig,
    onSelectPlayStyle: (PlayStyle) -> Unit,
    onSelectMentality: (Mentality) -> Unit,
    onSelectPressing: (PressingIntensity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Play Style
        item {
            Text(
                text = "FILOSOFÍA DE JUEGO",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PlayStyle.values().forEach { style ->
                    val isSelected = tactic.playStyle == style
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectPlayStyle(style) }
                            .testTag("style_${style.name}")
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) NervaCyan else NervaBlueSurfaceVariant,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NervaBlueContainer else NervaBlueSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = style.displayName,
                                    color = if (isSelected) NervaCyan else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = style.description,
                                    color = NervaTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = NervaCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Mentality
        item {
            Text(
                text = "MENTALIDAD DEL EQUIPO",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Mentality.values().forEach { ment ->
                    val isSelected = tactic.mentality == ment
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectMentality(ment) }
                            .testTag("mentality_${ment.name}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NervaBlueElectric.copy(alpha = 0.3f) else NervaBlueSurface
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NervaCyan)) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = ment.displayName,
                                color = if (isSelected) NervaCyan else NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Ataque: ${(ment.attackBoost * 100).toInt()}% • Defensa: ${(ment.defenseBoost * 100).toInt()}%",
                                color = NervaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. Pressing
        item {
            Text(
                text = "PRESIÓN TRAS PÉRDIDA",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PressingIntensity.values().forEach { press ->
                    val isSelected = tactic.pressing == press
                    Button(
                        onClick = { onSelectPressing(press) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) NervaCyan else NervaBlueSurface,
                            contentColor = if (isSelected) NervaBlueDeep else NervaWhite
                        )
                    ) {
                        Text(
                            text = press.displayName.split(" ").first(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BenchTab(
    bench: List<Player>,
    playerToSwap: Player?,
    onPlayerClick: (Player) -> Unit,
    onInspectPlayer: (Player) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "BANQUILLO DE SUPLENTES",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Text(
                text = "Toca un jugador para ver su evolución, o pulsa 'Cambiar' para intercambiarlo.",
                color = NervaTextMuted,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(bench) { player ->
            val isSelected = player.id == playerToSwap?.id
            PlayerItemRow(
                player = player,
                isSelected = isSelected,
                onClick = { onInspectPlayer(player) },
                trailingContent = {
                    Button(
                        onClick = { onPlayerClick(player) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (playerToSwap != null) NervaCyan else NervaBlueContainer,
                            contentColor = if (playerToSwap != null) NervaBlueDeep else NervaWhite
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (playerToSwap != null) "Poner Aquí" else "Cambiar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}
