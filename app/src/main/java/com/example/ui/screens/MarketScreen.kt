package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InitialData
import com.example.model.Club
import com.example.model.Player
import com.example.model.PositionType
import com.example.model.TransferOffer
import com.example.ui.components.AppTopBar
import com.example.ui.components.NervaShieldLogo
import com.example.ui.theme.*

@Composable
fun MarketScreen(
    userClub: Club,
    marketPlayers: List<Player>,
    userSquad: List<Player>,
    incomingOffers: List<TransferOffer>,
    isOfferDialogOpen: Boolean,
    playerForOffer: Player?,
    onOpenOfferDialog: (Player) -> Unit,
    onCloseOfferDialog: () -> Unit,
    onSubmitOffer: (Long) -> Unit,
    onToggleTransferList: (Player) -> Unit,
    onAcceptOffer: (String) -> Unit,
    onRejectOffer: (String) -> Unit,
    onPlayerClick: (Player) -> Unit = {},
    onOpenMail: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf("JUGADORES") } // JUGADORES, OFERTAS, FICHAJES
    var searchQuery by remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf("Todas") }
    var selectedAge by remember { mutableStateOf("Todas") }
    var selectedPrice by remember { mutableStateOf("Todos") }
    var currentPage by remember { mutableIntStateOf(1) }

    val filteredPlayers = remember(marketPlayers, searchQuery, selectedPosition) {
        marketPlayers.filter { player ->
            (searchQuery.isBlank() || player.name.contains(searchQuery, ignoreCase = true)) &&
            (selectedPosition == "Todas" || player.specificPos == selectedPosition || player.position.displayName == selectedPosition)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaSidebarDarker)
    ) {
        // Top Bar
        AppTopBar(
            userClub = userClub,
            title = "MERCADO DE FICHAJES",
            subtitle = "Busca, negocia y refuerza tu equipo",
            onMailClick = onOpenMail
        )

        // Subtabs Pills Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("JUGADORES", "OFERTAS", "FICHAJES").forEach { tab ->
                val isSelected = selectedSubTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) NervaBlueElectric else NervaCardDark)
                        .clickable { selectedSubTab = tab }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = when (tab) {
                                "JUGADORES" -> Icons.Default.Person
                                "OFERTAS" -> Icons.Default.Description
                                else -> Icons.Default.Handshake
                            },
                            contentDescription = null,
                            tint = if (isSelected) NervaWhite else NervaTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = tab,
                            color = if (isSelected) NervaWhite else NervaTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Main Layout: Player Grid on Left (weight 2.6) + "MI CLUB" Sidebar on Right (weight 1.1)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LEFT: Search, Filters, and 8 Cards Grid
            Column(
                modifier = Modifier
                    .weight(2.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Filter Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar jugador...", color = NervaTextDim, fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NervaTextMuted, modifier = Modifier.size(16.dp)) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .testTag("market_search_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NervaCardDark,
                            unfocusedContainerColor = NervaCardDark,
                            focusedBorderColor = NervaBlueElectric,
                            unfocusedBorderColor = NervaCardBorder,
                            focusedTextColor = NervaWhite,
                            unfocusedTextColor = NervaWhite
                        )
                    )

                    // Position Dropdown / Filter Pill
                    FilterDropdownPill(
                        label = "Posición",
                        currentValue = selectedPosition,
                        options = listOf("Todas", "DC", "MC", "MCO", "DFC", "LI", "LD", "POR"),
                        onSelect = { selectedPosition = it },
                        modifier = Modifier.weight(0.9f)
                    )

                    // Age Filter Pill
                    FilterDropdownPill(
                        label = "Edad",
                        currentValue = selectedAge,
                        options = listOf("Todas", "< 23", "23-28", "> 28"),
                        onSelect = { selectedAge = it },
                        modifier = Modifier.weight(0.8f)
                    )

                    // Price Filter Pill
                    FilterDropdownPill(
                        label = "Precio",
                        currentValue = selectedPrice,
                        options = listOf("Todos", "< 50M", "50M-80M", "> 80M"),
                        onSelect = { selectedPrice = it },
                        modifier = Modifier.weight(0.8f)
                    )

                    // FILTRAR Button
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NervaBlueElectric,
                            contentColor = NervaWhite
                        ),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("FILTRAR", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Grid of 8 Players (2 rows of 4)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPlayers.take(8)) { player ->
                        MarketPlayerCardExact(
                            player = player,
                            onFicharClick = { onOpenOfferDialog(player) },
                            onPlayerClick = { onPlayerClick(player) }
                        )
                    }
                }

                // Bottom Pagination (< 1 2 3 4 5 >)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "‹",
                        color = NervaTextMuted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { if (currentPage > 1) currentPage-- }
                            .padding(horizontal = 8.dp)
                    )

                    (1..5).forEach { page ->
                        val isPageActive = currentPage == page
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isPageActive) NervaBlueElectric else Color.Transparent)
                                .clickable { currentPage = page },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$page",
                                color = if (isPageActive) NervaWhite else NervaTextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isPageActive) FontWeight.Black else FontWeight.Normal
                            )
                        }
                    }

                    Text(
                        text = "›",
                        color = NervaTextMuted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { if (currentPage < 5) currentPage++ }
                            .padding(horizontal = 8.dp)
                    )
                }
            }

            // RIGHT SIDEBAR: "MI CLUB" + Últimos fichajes + Ventas recientes + Banner
            Column(
                modifier = Modifier
                    .weight(1.05f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // MI CLUB Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NervaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(NervaCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "MI CLUB",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            NervaShieldLogo(size = 36.dp)
                            Column {
                                Text(
                                    text = userClub.name,
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Presupuesto disponible",
                                    color = NervaTextMuted,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = userClub.formattedBudget,
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Últimos Fichajes
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NervaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(NervaCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Últimos fichajes",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )

                        InitialData.recentMarketPurchases.forEach { purchase ->
                            RecentRow(
                                badgeText = purchase.clubBadgeText,
                                playerName = purchase.playerShort,
                                amount = purchase.amountFormatted
                            )
                        }
                    }
                }

                // Ventas Recientes
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NervaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(NervaCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Ventas recientes",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )

                        InitialData.recentMarketSales.forEach { sale ->
                            RecentRow(
                                badgeText = sale.clubBadgeText,
                                playerName = sale.playerShort,
                                amount = sale.amountFormatted
                            )
                        }
                    }
                }

                // Info Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF07193F)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF0F3277))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            tint = NervaCyanAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "El mercado de fichajes",
                                color = NervaWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Se actualiza cada 24 horas. ¡Encuentra al jugador perfecto para tu proyecto!",
                                color = NervaTextMuted,
                                fontSize = 9.sp,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Offer Dialog
    if (isOfferDialogOpen && playerForOffer != null) {
        TransferOfferDialogExact(
            player = playerForOffer,
            clubBudget = userClub.budget,
            onDismiss = onCloseOfferDialog,
            onSubmit = onSubmitOffer
        )
    }
}

@Composable
private fun MarketPlayerCardExact(
    player: Player,
    onFicharClick: () -> Unit,
    onPlayerClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onPlayerClick() }
            .border(1.dp, NervaCardBorder, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = NervaCardDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Top Row: OVR Pill on Left, Club badge on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blue Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NervaBlueElectric)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${player.overall}  ${player.specificPos}",
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }

                // Club crest initials
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player.clubName.take(1),
                        color = NervaWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Player Avatar Silhouette / Portrait
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF142B58))
                        .border(1.dp, NervaCyanAccent.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 24.sp
                    )
                }
            }

            // Player Name
            Text(
                text = player.name,
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Age & Flag
            Text(
                text = "${player.age} años | ${player.nationality}",
                color = NervaTextMuted,
                fontSize = 10.sp
            )

            // Stats Matrix (2 columns x 4 attributes matching Image 5)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF07142E), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPillText(label = "RIT", value = player.pace)
                    StatPillText(label = "TIR", value = player.shooting)
                    StatPillText(label = "PAS", value = player.passing)
                    StatPillText(label = "FIS", value = player.physical)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPillText(label = "REG", value = player.dribbling)
                    StatPillText(label = "DEF", value = player.defending)
                    StatPillText(label = "FIS", value = player.physical)
                    Text(text = " ", fontSize = 8.sp)
                }
            }

            // Price Text
            Text(
                text = player.formattedValue,
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Action Buttons: FICHAR + Cart Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onFicharClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NervaBlueElectric,
                        contentColor = NervaWhite
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("FICHAR", fontSize = 10.sp, fontWeight = FontWeight.Black)
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, NervaCardBorder, RoundedCornerShape(4.dp))
                        .clickable { onFicharClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = NervaWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPillText(label: String, value: Int) {
    Text(
        text = "$label $value",
        color = NervaTextMuted,
        fontSize = 8.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun RecentRow(badgeText: String, playerName: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = badgeText.take(1), color = NervaWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = playerName, color = NervaWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = amount, color = NervaTextMuted, fontSize = 10.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NervaTextDim, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun FilterDropdownPill(
    label: String,
    currentValue: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(NervaCardDark)
                .border(1.dp, NervaCardBorder, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = label, color = NervaTextDim, fontSize = 8.sp)
                    Text(text = currentValue, color = NervaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = NervaTextMuted, modifier = Modifier.size(16.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(NervaCardDark)
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt, color = NervaWhite, fontSize = 11.sp) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TransferOfferDialogExact(
    player: Player,
    clubBudget: Long,
    onDismiss: () -> Unit,
    onSubmit: (Long) -> Unit
) {
    var offerMillions by remember { mutableFloatStateOf((player.marketValue / 1_000_000f)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NervaCardDark,
        title = {
            Text(
                text = "Oferta por ${player.name}",
                color = NervaCyanAccent,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "${player.clubName} • ${player.specificPos} • ${player.overall} OVR",
                    color = NervaTextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "Monto ofrecido: $ " + String.format("%.1fM", offerMillions),
                    color = NervaWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Slider(
                    value = offerMillions,
                    onValueChange = { offerMillions = it },
                    valueRange = (player.marketValue / 1_000_000f * 0.7f)..(player.marketValue / 1_000_000f * 1.6f),
                    colors = SliderDefaults.colors(
                        thumbColor = NervaBlueElectric,
                        activeTrackColor = NervaBlueElectric,
                        inactiveTrackColor = Color(0xFF142B58)
                    )
                )
                Text(
                    text = "Tu presupuesto: $ " + String.format("%,d", clubBudget).replace(",", "."),
                    color = if ((offerMillions * 1_000_000L) <= clubBudget) NervaGreen else NervaRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit((offerMillions * 1_000_000L).toLong()) },
                colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric, contentColor = NervaWhite)
            ) {
                Text("Confirmar Fichaje", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = NervaTextMuted)
            }
        }
    )
}
