package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.model.Player
import com.example.model.PositionType
import com.example.ui.components.AppTopBar
import com.example.ui.components.PlayerItemRow
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun SquadScreen(
    userClub: Club,
    squad: List<Player>,
    onNavigate: (ScreenDestination) -> Unit,
    onPlayerClick: (Player) -> Unit,
    onOpenSponsors: () -> Unit = {},
    onEditPlayerFlag: (Player) -> Unit = {},
    onEditClubFlag: () -> Unit = {},
    onOpenMail: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<PositionType?>(null) }

    val filteredSquad = remember(squad, selectedFilter) {
        if (selectedFilter == null) squad else squad.filter { it.position == selectedFilter }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaSidebarDarker)
            .testTag("squad_screen")
    ) {
        AppTopBar(
            userClub = userClub,
            title = "PLANTILLA DEL CLUB",
            subtitle = "${squad.size} Jugadores inscritos • Media 84 OVR",
            onFlagClick = onEditClubFlag,
            onMailClick = onOpenMail
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Filter chips on left
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("Todos (${squad.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NervaBlueElectric,
                        selectedLabelColor = NervaWhite,
                        containerColor = NervaCardDark,
                        labelColor = NervaTextMuted
                    )
                )
                PositionType.values().forEach { pos ->
                    val count = squad.count { it.position == pos }
                    FilterChip(
                        selected = selectedFilter == pos,
                        onClick = { selectedFilter = pos },
                        label = { Text("${pos.displayName} ($count)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NervaBlueElectric,
                            selectedLabelColor = NervaWhite,
                            containerColor = NervaCardDark,
                            labelColor = NervaTextMuted
                        )
                    )
                }
            }

            // PATROCINADORES Button on Right
            Button(
                onClick = onOpenSponsors,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7A4A)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 7.dp),
                modifier = Modifier.testTag("btn_patrocinadores")
            ) {
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = null,
                    tint = NervaWhite,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "PATROCINADORES",
                    color = NervaWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredSquad) { player ->
                PlayerItemRow(
                    player = player,
                    onClick = { onPlayerClick(player) },
                    onFlagClick = { onEditPlayerFlag(player) },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (player.isStarter) "TITULAR" else "SUPLENTE",
                                color = if (player.isStarter) NervaCyanAccent else NervaTextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = player.formattedWage,
                                color = NervaGreen,
                                fontSize = 10.sp
                            )
                        }
                    }
                )
            }
        }
    }
}
