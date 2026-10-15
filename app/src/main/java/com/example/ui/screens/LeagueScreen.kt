package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.model.Match
import com.example.ui.components.ClubBadge
import com.example.ui.theme.*

@Composable
fun LeagueScreen(
    clubs: List<Club>,
    matches: List<Match>,
    onPlayMatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Clasificación", "Partidos", "Estadísticas")

    // Sort clubs by Points descending, then Goal Difference, then Goals For
    val sortedClubs = remember(clubs) {
        clubs.sortedWith(
            compareByDescending<Club> { it.points }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NervaBlueSurface,
            contentColor = NervaCyan,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.testTag("league_tab_$index"),
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (selectedTab == index) NervaCyan else NervaTextMuted
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> StandingsTab(sortedClubs)
            1 -> FixturesTab(matches, clubs, onPlayMatch)
            2 -> StatsTab(sortedClubs)
        }
    }
}

@Composable
private fun StandingsTab(sortedClubs: List<Club>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Header explanation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "SUPERLIGA NERVA • TEMPORADA 1",
                            color = NervaCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "8 Clubes en contienda por el campeonato de managers",
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NervaGold.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "JORNADA 7/14",
                            color = NervaGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Table Column Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "#", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                Text(text = "CLUB", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(text = "PJ", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                Text(text = "PG", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text(text = "PE", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text(text = "PP", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text(text = "DG", color = NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
                Text(text = "PTS", color = NervaCyan, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(34.dp), textAlign = TextAlign.Center)
            }
        }

        // Club Rows
        itemsIndexed(sortedClubs) { index, club ->
            val pos = index + 1
            val isUser = club.isUserClub
            val isRivalSpotlight = club.id in listOf("c_chilenos", "c_nazionale", "c_tamber")

            val posIndicatorColor = when {
                pos <= 2 -> NervaCyan
                pos <= 4 -> NervaBlueElectric
                pos >= 7 -> NervaRed
                else -> NervaTextMuted
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("standing_row_${club.id}")
                    .border(
                        width = if (isUser) 1.5.dp else if (isRivalSpotlight) 1.dp else 0.dp,
                        color = if (isUser) NervaCyan else if (isRivalSpotlight) NervaBlueElectric.copy(alpha = 0.5f) else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUser) NervaBlueContainer.copy(alpha = 0.7f) else NervaBlueSurface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position badge
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .clip(CircleShape)
                            .background(posIndicatorColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pos.toString(),
                            color = posIndicatorColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Club Badge & Name
                    ClubBadge(club = club, size = 28.dp)
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = club.name,
                                color = if (isUser) NervaCyan else NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            if (isUser) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "★",
                                    color = NervaCyan,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Text(
                            text = club.managerName,
                            color = NervaTextMuted,
                            fontSize = 10.sp
                        )
                    }

                    // Stats Columns
                    Text(text = club.played.toString(), color = NervaWhite, fontSize = 12.sp, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                    Text(text = club.won.toString(), color = NervaGreen, fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text(text = club.drawn.toString(), color = NervaGold, fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text(text = club.lost.toString(), color = NervaRed, fontSize = 12.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                    Text(
                        text = (if (club.goalDifference > 0) "+${club.goalDifference}" else "${club.goalDifference}"),
                        color = NervaWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = club.points.toString(),
                        color = NervaCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.width(34.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun FixturesTab(
    matches: List<Match>,
    clubs: List<Club>,
    onPlayMatch: () -> Unit
) {
    var selectedRound by remember { mutableIntStateOf(7) }
    val rounds = listOf(6, 7, 8, 9)

    val roundMatches = remember(matches, selectedRound) {
        matches.filter { it.round == selectedRound }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Round selector chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rounds.forEach { round ->
                FilterChip(
                    selected = selectedRound == round,
                    onClick = { selectedRound = round },
                    label = { Text("Jornada $round", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NervaCyan,
                        selectedLabelColor = NervaBlueDeep,
                        containerColor = NervaBlueSurface,
                        labelColor = NervaWhite
                    )
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(roundMatches) { _, match ->
                val homeClub = clubs.firstOrNull { it.id == match.homeClubId } ?: return@itemsIndexed
                val awayClub = clubs.firstOrNull { it.id == match.awayClubId } ?: return@itemsIndexed
                val isUserMatch = homeClub.isUserClub || awayClub.isUserClub

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NervaBlueSurface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (isUserMatch) Brush.horizontalGradient(listOf(NervaCyan, NervaBlueElectric))
                        else Brush.linearGradient(listOf(NervaBlueSurfaceVariant, NervaBlueSurfaceVariant))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Home Club
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                ClubBadge(club = homeClub, size = 32.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = homeClub.name,
                                    color = if (homeClub.isUserClub) NervaCyan else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            // Score or Time
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NervaBlueContainer)
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (match.isPlayed) {
                                    Text(
                                        text = "${match.homeScore} - ${match.awayScore}",
                                        color = NervaWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp
                                    )
                                } else {
                                    Text(
                                        text = "POR JUGAR",
                                        color = NervaCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Away Club
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = awayClub.name,
                                    color = if (awayClub.isUserClub) NervaCyan else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                ClubBadge(club = awayClub, size = 32.dp)
                            }
                        }

                        // Scorers if played
                        if (match.isPlayed && (match.homeScorers.isNotEmpty() || match.awayScorers.isNotEmpty())) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = NervaBlueSurfaceVariant, thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Goles: ${(match.homeScorers + match.awayScorers).joinToString(", ")}",
                                color = NervaTextMuted,
                                fontSize = 10.sp
                            )
                        }

                        // Play in 2D button if this is an unplayed match for the user
                        if (!match.isPlayed && isUserMatch) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onPlayMatch,
                                modifier = Modifier.fillMaxWidth().height(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NervaCyan,
                                    contentColor = NervaBlueDeep
                                )
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simular Partido en Motor 2D", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsTab(sortedClubs: List<Club>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "LÍDERES DE LA TEMPORADA",
                color = NervaCyan,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🏆 Tabla de Goleadores (Pichichi)",
                        color = NervaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Divider(color = NervaBlueSurfaceVariant)

                    StatLeaderRow(rank = 1, name = "Alexis Valenzuela", club = "Chilenos FC", stat = "7 Goles", accentColor = NervaGold)
                    StatLeaderRow(rank = 2, name = "Gabriel Nova", club = "NERVA CF", stat = "6 Goles", accentColor = NervaCyan)
                    StatLeaderRow(rank = 3, name = "Ciro Immobilex", club = "La Nazionale", stat = "6 Goles", accentColor = NervaWhite)
                    StatLeaderRow(rank = 4, name = "Benjamín Soto", club = "Chilenos FC", stat = "5 Goles", accentColor = NervaWhite)
                    StatLeaderRow(rank = 5, name = "Mateo Tamber Jr.", club = "Tamber CF", stat = "4 Goles", accentColor = NervaWhite)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NervaBlueSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🎯 Máximos Asistentes",
                        color = NervaCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Divider(color = NervaBlueSurfaceVariant)

                    StatLeaderRow(rank = 1, name = "Mateo Tamber Jr.", club = "Tamber CF", stat = "5 Asist.", accentColor = NervaGold)
                    StatLeaderRow(rank = 2, name = "Santiago Rey", club = "NERVA CF", stat = "4 Asist.", accentColor = NervaCyan)
                    StatLeaderRow(rank = 3, name = "Federico Barella", club = "La Nazionale", stat = "3 Asist.", accentColor = NervaWhite)
                }
            }
        }
    }
}

@Composable
private fun StatLeaderRow(
    rank: Int,
    name: String,
    club: String,
    stat: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${rank}º",
            color = accentColor,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            modifier = Modifier.width(28.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = NervaWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = club,
                color = NervaTextMuted,
                fontSize = 11.sp
            )
        }
        Text(
            text = stat,
            color = accentColor,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
        )
    }
}
