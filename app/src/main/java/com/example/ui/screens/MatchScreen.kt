package com.example.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.MatchEngineState
import com.example.model.Club
import com.example.model.Mentality
import com.example.ui.components.ClubBadge
import com.example.ui.components.MatchEngine2DCanvas
import com.example.ui.theme.*

@Composable
fun MatchScreen(
    state: MatchEngineState?,
    homeClub: Club,
    awayClub: Club,
    onTogglePlayPause: () -> Unit,
    onSetSpeed: (Int) -> Unit = {},
    onChangeMentality: (Mentality) -> Unit,
    onResumeSecondHalf: () -> Unit = {},
    onSkipToEnd: () -> Unit = {},
    onCloseSummary: () -> Unit,
    showSummaryDialog: Boolean,
    modifier: Modifier = Modifier
) {
    if (state == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(NervaBlueDeep),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Preparando terreno de juego...",
                color = NervaCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. SCOREBOARD CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("match_scoreboard_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(NervaCyan, NervaBlueElectric))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Match clock and status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "LIGA OFICIAL (30 MIN + AÑADIDO)",
                                color = NervaCyan,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                            if (state.isInStoppageTime) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFDC2626))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "+${state.addedStoppageMinutes}' AÑADIDO",
                                        color = NervaWhite,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when {
                                        state.isFinished -> NervaGold
                                        state.isHalftime -> Color(0xFFF59E0B)
                                        state.isInStoppageTime -> Color(0xFFEF4444)
                                        else -> NervaGreen
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = when {
                                    state.isFinished -> "FINAL"
                                    state.isHalftime -> "ENTRETIEMPO"
                                    else -> state.formattedClock
                                },
                                color = NervaBlueDeep,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // 4th Official Stoppage Time LED Board Banner
                    if (state.stoppageBoardNotice != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.stoppageBoardNotice,
                                color = Color(0xFFF87171),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Teams & Score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Home Team
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            ClubBadge(club = homeClub, size = 44.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = homeClub.name,
                                color = NervaCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Big Score
                        Text(
                            text = "${state.homeScore} - ${state.awayScore}",
                            color = NervaWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )

                        // Away Team
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            ClubBadge(club = awayClub, size = 44.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = awayClub.name,
                                color = NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Scorers
                    if (state.homeScorers.isNotEmpty() || state.awayScorers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = NervaBlueSurfaceVariant, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.homeScorers.joinToString("\n"),
                                color = NervaTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = state.awayScorers.joinToString("\n"),
                                color = NervaTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }

        // 2. 2D MATCH ENGINE CANVAS
        item {
            MatchEngine2DCanvas(
                state = state,
                homeClub = homeClub,
                awayClub = awayClub
            )
        }

        // 3. MATCH ENGINE CONTROLS BAR
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (state.isHalftime) {
                        Button(
                            onClick = onResumeSecondHalf,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NervaGold,
                                contentColor = NervaBlueDeep
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("▶ Iniciar 2° Tiempo (15'-30')", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    } else {
                        // Play / Pause
                        Button(
                            onClick = onTogglePlayPause,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isPlaying) NervaGold else NervaCyan,
                                contentColor = NervaBlueDeep
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (state.isPlaying) "Pausar" else "Reanudar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!state.isFinished) {
                        Button(
                            onClick = onSkipToEnd,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF312E81),
                                contentColor = NervaWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "⏩ Simular al Final",
                                color = NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. LIVE TACTICAL ADJUSTMENT BUTTONS
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "ORDEN DESDE EL BANQUILLO (EN VIVO)",
                        color = NervaCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { onChangeMentality(Mentality.DEFENSIVE) },
                            modifier = Modifier.weight(1f).height(34.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NervaBlueContainer, contentColor = NervaWhite),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("🛡 Defender", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onChangeMentality(Mentality.BALANCED) },
                            modifier = Modifier.weight(1f).height(34.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NervaBlueContainer, contentColor = NervaWhite),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("⚖ Equilibrado", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onChangeMentality(Mentality.ALL_OUT_ATTACK) },
                            modifier = Modifier.weight(1f).height(34.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NervaCyan, contentColor = NervaBlueDeep),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("⚡ Ataque Total", fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 5. LIVE STATS COMPARISON
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "ESTADÍSTICAS DEL PARTIDO",
                        color = NervaCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )

                    LiveStatBar(
                        label = "Posesión",
                        homeValue = "${state.stats.homePossession}%",
                        awayValue = "${state.stats.awayPossession}%",
                        homeRatio = state.stats.homePossession / 100f
                    )

                    LiveStatBar(
                        label = "Remates Totales",
                        homeValue = "${state.stats.homeShots}",
                        awayValue = "${state.stats.awayShots}",
                        homeRatio = if (state.stats.homeShots + state.stats.awayShots > 0)
                            state.stats.homeShots.toFloat() / (state.stats.homeShots + state.stats.awayShots) else 0.5f
                    )

                    LiveStatBar(
                        label = "Tiros a Puerta",
                        homeValue = "${state.stats.homeShotsOnTarget}",
                        awayValue = "${state.stats.awayShotsOnTarget}",
                        homeRatio = if (state.stats.homeShotsOnTarget + state.stats.awayShotsOnTarget > 0)
                            state.stats.homeShotsOnTarget.toFloat() / (state.stats.homeShotsOnTarget + state.stats.awayShotsOnTarget) else 0.5f
                    )

                    LiveStatBar(
                        label = "Faltas Cometidas",
                        homeValue = "${state.stats.homeFouls}",
                        awayValue = "${state.stats.awayFouls}",
                        homeRatio = if (state.stats.homeFouls + state.stats.awayFouls > 0)
                            state.stats.homeFouls.toFloat() / (state.stats.homeFouls + state.stats.awayFouls) else 0.5f
                    )

                    LiveStatBar(
                        label = "Tarjetas 🟨 / 🟥",
                        homeValue = "🟨${state.stats.homeYellowCards} 🟥${state.stats.homeRedCards}",
                        awayValue = "🟨${state.stats.awayYellowCards} 🟥${state.stats.awayRedCards}",
                        homeRatio = 0.5f
                    )

                    if (state.stats.homePenalties > 0 || state.stats.awayPenalties > 0) {
                        LiveStatBar(
                            label = "Penaltis Señalados",
                            homeValue = "${state.stats.homePenalties}",
                            awayValue = "${state.stats.awayPenalties}",
                            homeRatio = if (state.stats.homePenalties + state.stats.awayPenalties > 0)
                                state.stats.homePenalties.toFloat() / (state.stats.homePenalties + state.stats.awayPenalties) else 0.5f
                        )
                    }
                }
            }
        }

        // 6. LIVE COMMENTARY FEED
        item {
            Text(
                text = "MINUTO A MINUTO (INCIDENCIAS Y DECISIONES ARBITRALES)",
                color = NervaTextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        items(state.commentaries) { comm ->
            val cardBg = when {
                comm.isGoal -> NervaCyan.copy(alpha = 0.22f)
                comm.cardType == "RED" -> Color(0xFFDC2626).copy(alpha = 0.25f)
                comm.cardType == "YELLOW" -> Color(0xFFFBBF24).copy(alpha = 0.22f)
                comm.isPenalty -> Color(0xFFE11D48).copy(alpha = 0.22f)
                else -> NervaBlueSurface
            }

            val badgeBg = when {
                comm.isGoal -> NervaCyan
                comm.cardType == "RED" -> Color(0xFFDC2626)
                comm.cardType == "YELLOW" -> Color(0xFFFBBF24)
                comm.isPenalty -> Color(0xFFE11D48)
                else -> NervaBlueContainer
            }

            val badgeTextColor = when {
                comm.isGoal || comm.cardType == "YELLOW" -> NervaBlueDeep
                else -> NervaWhite
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${comm.minute}'",
                            color = badgeTextColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = comm.text,
                        color = when {
                            comm.isGoal -> NervaCyan
                            comm.cardType == "RED" -> Color(0xFFFCA5A5)
                            comm.cardType == "YELLOW" -> Color(0xFFFDE68A)
                            comm.isPenalty -> Color(0xFFFDA4AF)
                            else -> NervaWhite
                        },
                        fontSize = 12.sp,
                        fontWeight = if (comm.isImportant || comm.isGoal) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }

    // MATCH SUMMARY POPUP DIALOG
    if (showSummaryDialog) {
        AlertDialog(
            onDismissRequest = onCloseSummary,
            containerColor = NervaBlueSurface,
            title = {
                Text(
                    text = "¡Final del Partido!",
                    color = NervaCyan,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${homeClub.name} ${state.homeScore} - ${state.awayScore} ${awayClub.name}",
                        color = NervaWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    val won = state.homeScore > state.awayScore
                    val draw = state.homeScore == state.awayScore

                    val resultMsg = when {
                        won -> "¡Gran victoria! Sumas +3 puntos en la tabla y +2.5M € en taquilla y patrocinio."
                        draw -> "Empate trabajado. Sumas +1 punto en la tabla y +1.0M € de ingresos."
                        else -> "Derrota dolorosa. Ajusta tus tácticas y plantilla para la próxima fecha."
                    }

                    Text(
                        text = resultMsg,
                        color = if (won) NervaGreen else if (draw) NervaGold else NervaRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onCloseSummary,
                    colors = ButtonDefaults.buttonColors(containerColor = NervaCyan, contentColor = NervaBlueDeep),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Volver a la Liga", fontWeight = FontWeight.Black)
                }
            }
        )
    }
}

@Composable
private fun LiveStatBar(
    label: String,
    homeValue: String,
    awayValue: String,
    homeRatio: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = homeValue, color = NervaCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = NervaTextMuted, fontSize = 11.sp)
            Text(text = awayValue, color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(NervaBlueContainer)
        ) {
            Box(
                modifier = Modifier
                    .weight(homeRatio.coerceIn(0.05f, 0.95f))
                    .fillMaxHeight()
                    .background(NervaCyan)
            )
            Box(
                modifier = Modifier
                    .weight((1f - homeRatio).coerceIn(0.05f, 0.95f))
                    .fillMaxHeight()
                    .background(NervaBlueElectric)
            )
        }
    }
}
