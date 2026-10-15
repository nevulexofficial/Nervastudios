package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Club
import com.example.model.League
import com.example.model.LeagueDivision
import com.example.model.ScheduledMatchSlot
import com.example.ui.components.ClubBadge
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun CompetitionsScreen(
    userClub: Club,
    leagues: List<League>,
    divisions: Map<Int, LeagueDivision>,
    selectedDivisionNumber: Int,
    scheduledSlots: List<ScheduledMatchSlot>,
    onSelectDivision: (Int) -> Unit,
    onSimulateSlot: (String) -> Unit,
    onPlayMatch: () -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    onClubClick: (Club) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf("LIGA_GAMMA") } // "LIGA_GAMMA", "COPAS", "HORARIOS_5H"

    val activeLeague = leagues.firstOrNull { it.id == "gamma_nerva" } ?: leagues.firstOrNull()
    val currentDivision = divisions[selectedDivisionNumber] ?: divisions[12]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .testTag("competitions_screen_gamma_nerva")
    ) {
        // 1. Top Header Banner: Liga Gamma Nerva
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF0369A1),
                            Color(0xFF0D2566),
                            Color(0xFF1E1B4B)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
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
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0284C7).copy(alpha = 0.3f))
                            .border(1.5.dp, NervaCyanAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activeLeague?.iconEmoji ?: "⚡",
                            fontSize = 20.sp
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = activeLeague?.name ?: "Liga Gamma Nerva",
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                letterSpacing = 1.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NervaCyanAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "12 DIVISIONES • MÁX 30 CLUBES",
                                    color = NervaCyanAccent,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = "⭐️ Todo nuevo jugador empieza en División 12 • Sistema oficial de ascensos y descensos",
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // SubTab Switcher
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0B1B3D))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf(
                        "LIGA_GAMMA" to "12 Divisiones",
                        "HORARIOS_5H" to "Partidos 5h",
                        "COPAS" to "Copas"
                    ).forEach { (tabKey, tabLabel) ->
                        val isSelected = activeSubTab == tabKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NervaBlueElectric else Color.Transparent)
                                .clickable { activeSubTab = tabKey }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tabLabel,
                                color = if (isSelected) NervaWhite else NervaTextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. Scheduled Matches Ticker Bar (Every 5 Hours: [5:00AM, 10:00AM, 3:00PM, 8:00PM])
        ScheduledMatchesBar(
            slots = scheduledSlots,
            onSimulateSlot = onSimulateSlot
        )

        // 3. Main Content based on selected subtab
        when (activeSubTab) {
            "LIGA_GAMMA" -> {
                DivisionStandingsView(
                    divisions = divisions,
                    selectedDivisionNumber = selectedDivisionNumber,
                    currentDivision = currentDivision,
                    userClub = userClub,
                    slots = scheduledSlots,
                    onSelectDivision = onSelectDivision,
                    onPlayMatch = onPlayMatch,
                    onSimulateSlot = onSimulateSlot,
                    onClubClick = onClubClick
                )
            }

            "HORARIOS_5H" -> {
                ScheduledMatchesFullTab(
                    slots = scheduledSlots,
                    onSimulateSlot = onSimulateSlot,
                    onPlayMatch = onPlayMatch
                )
            }

            "COPAS" -> {
                CupsGridView()
            }
        }
    }
}

@Composable
private fun ScheduledMatchesBar(
    slots: List<ScheduledMatchSlot>,
    onSimulateSlot: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF091632)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Color(0xFF0284C7), Color(0xFF0D2566)))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = NervaCyanAccent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "PARTIDOS AUTOMÁTICOS CADA 5 HORAS:",
                    color = NervaWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }

            // 4 Slots: [5:00AM, 10:00AM, 3:00PM, 8:00PM]
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                slots.forEach { slot ->
                    val isDone = slot.isCompletedToday
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDone) Color(0xFF064E3B) else Color(0xFF1E293B))
                            .border(
                                1.dp,
                                if (isDone) NervaGreen else Color(0xFF334155),
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onSimulateSlot(slot.id) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = slot.label,
                                color = if (isDone) NervaGreen else NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            if (isDone) {
                                Text("✓", color = NervaGreen, fontSize = 9.sp, fontWeight = FontWeight.Black)
                            } else {
                                Text("⚡", color = Color(0xFFFFD700), fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DivisionStandingsView(
    divisions: Map<Int, LeagueDivision>,
    selectedDivisionNumber: Int,
    currentDivision: LeagueDivision?,
    userClub: Club,
    slots: List<ScheduledMatchSlot>,
    onSelectDivision: (Int) -> Unit,
    onPlayMatch: () -> Unit,
    onSimulateSlot: (String) -> Unit,
    onClubClick: (Club) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Horizontal Division Pills (1 to 12)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items((1..12).toList()) { divNum ->
                val isSelected = selectedDivisionNumber == divNum
                val isUserDiv = divNum == 12 // User club starts in Division 12

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isSelected -> NervaBlueElectric
                                isUserDiv -> Color(0xFF1E3A8A)
                                else -> Color(0xFF0A192F)
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isSelected -> NervaCyanAccent
                                isUserDiv -> Color(0xFFFFD700)
                                else -> Color(0xFF1E355B)
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onSelectDivision(divNum) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Div. $divNum",
                            color = if (isSelected) NervaWhite else if (isUserDiv) Color(0xFFFFD700) else NervaTextMuted,
                            fontWeight = if (isSelected || isUserDiv) FontWeight.Black else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                        if (isUserDiv) {
                            Text("⭐️", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Two-column Console Videogame Layout
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // LEFT COLUMN: 30-Club Standings Table
            Column(
                modifier = Modifier
                    .weight(1.85f)
                    .fillMaxHeight()
            ) {
                // Division Title & Legend
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentDivision?.name ?: "División $selectedDivisionNumber",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "30 Clubes • 1º-3º Ascenso Directo • 28º-30º Descenso",
                            color = NervaCyanAccent,
                            fontSize = 10.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(NervaGreen))
                            Text("Ascenso", color = NervaGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Text("Descenso", color = Color(0xFFEF4444), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Table Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1E40))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("POS", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(34.dp))
                        Text("CLUB", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("PJ", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                        Text("PG", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                        Text("PE", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                        Text("PP", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                        Text("DG", color = NervaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                        Text("PTS", color = NervaCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(36.dp), textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 30 Clubs Standings Table
                val clubs = currentDivision?.clubs ?: emptyList()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    itemsIndexed(clubs) { index, club ->
                        val pos = index + 1
                        val isAscension = pos <= 3
                        val isRelegation = pos >= 28
                        val isUser = club.isUserClub

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onClubClick(club) }
                                .border(
                                    width = if (isUser) 1.5.dp else 1.dp,
                                    color = when {
                                        isUser -> Color(0xFFFFD700)
                                        isAscension -> NervaGreen.copy(alpha = 0.5f)
                                        isRelegation -> Color(0xFFEF4444).copy(alpha = 0.5f)
                                        else -> Color(0xFF172B4D)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isUser -> Color(0xFF132F5C)
                                    isAscension -> Color(0xFF072320)
                                    isRelegation -> Color(0xFF280B10)
                                    else -> Color(0xFF091833)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Position badge
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isUser -> Color(0xFFFFD700)
                                                isAscension -> NervaGreen
                                                isRelegation -> Color(0xFFEF4444)
                                                else -> Color(0xFF1E293B)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$pos",
                                        color = if (isUser) Color.Black else NervaWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Club Flag & Name
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(club.flag, fontSize = 12.sp)
                                    Text(
                                        text = club.name,
                                        color = if (isUser) Color(0xFFFFD700) else NervaWhite,
                                        fontWeight = if (isUser) FontWeight.Black else FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        maxLines = 1
                                    )
                                    if (isUser) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color(0xFFFFD700))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text("TÚ", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }

                                // Stats
                                Text("${club.played}", color = NervaWhite, fontSize = 10.5.sp, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                                Text("${club.won}", color = NervaWhite, fontSize = 10.5.sp, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                                Text("${club.drawn}", color = NervaTextMuted, fontSize = 10.5.sp, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                                Text("${club.lost}", color = NervaTextMuted, fontSize = 10.5.sp, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
                                Text(
                                    text = (if (club.goalDifference > 0) "+${club.goalDifference}" else "${club.goalDifference}"),
                                    color = if (club.goalDifference > 0) NervaGreen else if (club.goalDifference < 0) Color(0xFFEF4444) else NervaTextMuted,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(30.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${club.points}",
                                    color = if (isUser) Color(0xFFFFD700) else NervaCyanAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.width(36.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // RIGHT COLUMN: Videogame Match Hub & Scheduled Slots Widget
            Column(
                modifier = Modifier
                    .weight(1.15f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. BIG NEXT MATCH BROADCAST CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF091632)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(NervaCyanAccent, Color(0xFF1E3A8A)))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "PRÓXIMO PARTIDO DE LIGA",
                            color = NervaCyanAccent,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Matchup VS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                ClubBadge(club = userClub, size = 42.dp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(userClub.shortName, color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("VS", color = Color(0xFFFCA5A5), fontWeight = FontWeight.Black, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🇨🇱", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("CHI", color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "⏱️ Formato Oficial: 30 min + tiempo añadido",
                            color = NervaTextMuted,
                            fontSize = 9.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onPlayMatch,
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NervaCyanAccent, contentColor = NervaBlueDeep)
                        ) {
                            Text("⚽ JUGAR PARTIDO 2D", fontWeight = FontWeight.Black, fontSize = 11.5.sp)
                        }
                    }
                }

                // 2. 5-HOUR AUTOMATIC SLOTS STATUS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF07142E))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "JORNADAS CADA 5 HORAS",
                                color = Color(0xFFFFD700),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text("Auto-sim", color = NervaTextMuted, fontSize = 9.sp)
                        }

                        slots.forEach { slot ->
                            val isDone = slot.isCompletedToday
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isDone) Color(0xFF064E3B) else Color(0xFF0F1E40))
                                    .border(1.dp, if (isDone) NervaGreen else Color(0xFF1E355B), RoundedCornerShape(6.dp))
                                    .clickable { onSimulateSlot(slot.id) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(if (isDone) "✓" else "⚡", color = if (isDone) NervaGreen else Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    Text(slot.label, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text(
                                    text = if (isDone) "Completado" else "Simular",
                                    color = if (isDone) NervaGreen else NervaCyanAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 3. TOP SCORERS (PICHICHI) MINI WIDGET
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF07142E))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "MÁXIMOS GOLEADORES",
                            color = NervaWhite,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black
                        )

                        val topScorers = listOf(
                            Triple("1. K. Mbappé (NER)", "NERVA FC", "18 ⚽"),
                            Triple("2. E. Haaland (MCI)", "Man City", "16 ⚽"),
                            Triple("3. Vinicius Jr (RMA)", "Real Madrid", "14 ⚽"),
                            Triple("4. L. Yamal (BAR)", "FC Barcelona", "12 ⚽")
                        )

                        topScorers.forEach { (name, club, goals) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, color = NervaCyanAccent, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                Text(goals, color = Color(0xFFFFD700), fontSize = 10.5.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduledMatchesFullTab(
    slots: List<ScheduledMatchSlot>,
    onSimulateSlot: (String) -> Unit,
    onPlayMatch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF091C3E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SISTEMA DE PARTIDOS AUTOMÁTICOS CADA 5 HORAS",
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "La liga oficial simula las jornadas a las 5:00 AM, 10:00 AM, 3:00 PM y 8:00 PM todos los días. Puedes forzar la simulación de cualquier jornada para avanzar la temporada y sumar puntos.",
                        color = NervaTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        items(slots) { slot ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (slot.isCompletedToday) Color(0xFF08201E) else Color(0xFF0A1934)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (slot.isCompletedToday) NervaGreen else Color(0xFF1E355B)
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NervaBlueElectric.copy(alpha = 0.3f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(slot.label, color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                            Text(slot.name, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        slot.lastSimulatedScore?.let { score ->
                            Text(
                                text = "Resultado: $score",
                                color = NervaGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } ?: Text(
                            text = "Pendiente de simular • Toca simular para computar resultados",
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { onSimulateSlot(slot.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (slot.isCompletedToday) Color(0xFF334155) else NervaBlueElectric
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (slot.isCompletedToday) "Volver a Simular" else "Simular Ahora ⚡",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CupsGridView() {
    val cups = listOf(
        "Copa de Campeones Gamma" to "FASE FINAL",
        "Copa de la Liga Nerva" to "SEMIFINALES",
        "Supercopa Cyber 2026" to "FINAL ÚNICA",
        "Copa Sudamericana Nerva" to "CUARTOS"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(cups) { (title, phase) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E40))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🏆", fontSize = 28.sp)
                        Column {
                            Text(title, color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(phase, color = NervaCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ver Cuadro", fontSize = 11.sp, color = NervaWhite)
                    }
                }
            }
        }
    }
}
