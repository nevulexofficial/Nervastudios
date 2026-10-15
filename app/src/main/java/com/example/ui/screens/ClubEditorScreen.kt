package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.components.ClubBadge
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun ClubEditorScreen(
    userClub: Club,
    onSaveClub: (
        name: String,
        shortName: String,
        motto: String,
        managerName: String,
        stadiumName: String,
        primaryColorHex: Long,
        secondaryColorHex: Long,
        badgeShape: String,
        badgeSymbol: String,
        badgeBorderColorHex: Long,
        bannerStyle: String
    ) -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var clubName by remember { mutableStateOf(userClub.name) }
    var shortName by remember { mutableStateOf(userClub.shortName) }
    var motto by remember { mutableStateOf(userClub.motto) }
    var managerName by remember { mutableStateOf(userClub.managerName) }
    var stadiumName by remember { mutableStateOf(userClub.stadiumName) }
    var primaryColorHex by remember { mutableStateOf(userClub.primaryColorHex) }
    var secondaryColorHex by remember { mutableStateOf(userClub.secondaryColorHex) }
    var badgeShape by remember { mutableStateOf(userClub.badgeShape) }
    var badgeSymbol by remember { mutableStateOf(userClub.badgeSymbol) }
    var badgeBorderColorHex by remember { mutableStateOf(userClub.badgeBorderColorHex) }
    var bannerStyle by remember { mutableStateOf(userClub.bannerStyle) }

    val previewClub = userClub.copy(
        name = clubName.ifBlank { "MI CLUB" },
        shortName = shortName.ifBlank { "NER" },
        motto = motto,
        managerName = managerName,
        stadiumName = stadiumName,
        primaryColorHex = primaryColorHex,
        secondaryColorHex = secondaryColorHex,
        badgeShape = badgeShape,
        badgeSymbol = badgeSymbol,
        badgeBorderColorHex = badgeBorderColorHex,
        bannerStyle = bannerStyle
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .testTag("club_editor_screen")
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF0F2654))
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
                            tint = NervaCyanAccent
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "EDICIÓN AVANZADA DE MI CLUB",
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
                                    text = "EXCLUSIVO",
                                    color = NervaCyanAccent,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = "Personaliza nombre, lema oficial, escudo dinámico, estadio y colores de gala",
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        onSaveClub(
                            clubName, shortName, motto, managerName, stadiumName,
                            primaryColorHex, secondaryColorHex, badgeShape, badgeSymbol,
                            badgeBorderColorHex, bannerStyle
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NervaCyanAccent,
                        contentColor = NervaBlueDeep
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
        }

        // Two-column layout: Left Live Preview, Right Controls
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LEFT COLUMN: Live 3D Badge & Club Hero Card Preview
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF091632)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.verticalGradient(listOf(NervaCyanAccent, Color(0xFF1E3A8A)))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "VISTA PREVIA EN VIVO",
                            color = NervaCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Shield Preview
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF030D22))
                                .border(2.dp, Color(badgeBorderColorHex), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            ClubBadge(club = previewClub, size = 94.dp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = previewClub.name.uppercase(),
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "SIGLAS: ${previewClub.shortName} • DIV. 12",
                            color = Color(0xFFFFD700),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Club Motto Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F2654))
                                .border(1.dp, Color(0xFF1E40AF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "«${previewClub.motto.ifBlank { "Honor, Gloria y Pasión" }}»",
                                color = NervaCyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Bottom info: Manager & Stadium
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF07142E))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("DT Oficial:", color = NervaTextMuted, fontSize = 10.sp)
                            Text(previewClub.managerName, color = NervaWhite, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estadio:", color = NervaTextMuted, fontSize = 10.sp)
                            Text(previewClub.stadiumName, color = NervaCyanAccent, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Banner:", color = NervaTextMuted, fontSize = 10.sp)
                            Text(previewClub.bannerStyle, color = Color(0xFFFFD700), fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            // RIGHT COLUMN: Configuration Form
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07142E))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // SECTION 1: IDENTITY
                    Text("1. IDENTIDAD Y LEMA", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = clubName,
                            onValueChange = { clubName = it },
                            label = { Text("Nombre del Club") },
                            modifier = Modifier.weight(2f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite,
                                focusedBorderColor = NervaCyanAccent,
                                unfocusedBorderColor = Color(0xFF1E355B)
                            )
                        )
                        OutlinedTextField(
                            value = shortName,
                            onValueChange = { if (it.length <= 4) shortName = it.uppercase() },
                            label = { Text("Siglas (3)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite,
                                focusedBorderColor = NervaCyanAccent,
                                unfocusedBorderColor = Color(0xFF1E355B)
                            )
                        )
                    }

                    OutlinedTextField(
                        value = motto,
                        onValueChange = { motto = it },
                        label = { Text("Lema del Club (Slogan Oficial)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NervaWhite,
                            unfocusedTextColor = NervaWhite,
                            focusedBorderColor = NervaCyanAccent,
                            unfocusedBorderColor = Color(0xFF1E355B)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = managerName,
                            onValueChange = { managerName = it },
                            label = { Text("Nombre del Mánager (DT)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite,
                                focusedBorderColor = NervaCyanAccent,
                                unfocusedBorderColor = Color(0xFF1E355B)
                            )
                        )
                        OutlinedTextField(
                            value = stadiumName,
                            onValueChange = { stadiumName = it },
                            label = { Text("Estadio Local") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite,
                                focusedBorderColor = NervaCyanAccent,
                                unfocusedBorderColor = Color(0xFF1E355B)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0xFF1E355B))

                    // SECTION 2: SHIELD SHAPE & SYMBOL
                    Text("2. DISEÑO DEL ESCUDO & EMBLEMA", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)

                    Text("Forma del Escudo:", color = NervaTextMuted, fontSize = 11.sp)
                    val shapes = listOf("CLASSIC", "DIAMOND", "HEXAGON", "SHIELD", "CIRCLE", "CROWN")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shapes) { shape ->
                            val isSel = badgeShape == shape
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NervaCyanAccent else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSel) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { badgeShape = shape }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = shape,
                                    color = if (isSel) NervaBlueDeep else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            }
                        }
                    }

                    Text("Emblema Central:", color = NervaTextMuted, fontSize = 11.sp)
                    val symbols = listOf(
                        Pair("EAGLE", "🦅 Águila"),
                        Pair("CROWN", "👑 Corona"),
                        Pair("LION", "🦁 León"),
                        Pair("STAR", "⭐️ Estrella"),
                        Pair("FLAME", "🔥 Llama"),
                        Pair("LIGHTNING", "⚡ Rayo")
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(symbols) { (key, label) ->
                            val isSel = badgeSymbol == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { badgeSymbol = key }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) NervaBlueDeep else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF1E355B))

                    // SECTION 3: COLORS & BANNERS
                    Text("3. PALETA DE COLORES & BANNER", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)

                    val colorOptions = listOf(
                        Pair(0xFF0D3AE8, "Azul Eléctrico"),
                        Pair(0xFF00E5FF, "Cyan Neón"),
                        Pair(0xFFFFD700, "Oro Imperial"),
                        Pair(0xFF10B981, "Esmeralda"),
                        Pair(0xFFDC2626, "Carmesí"),
                        Pair(0xFF8B5CF6, "Púrpura Real"),
                        Pair(0xFF1E293B, "Carbón Stealth")
                    )

                    Text("Color Primario:", color = NervaTextMuted, fontSize = 11.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(colorOptions) { (hex, name) ->
                            val isSel = primaryColorHex == hex
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFF1E3A8A) else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSel) NervaCyanAccent else Color.Transparent, RoundedCornerShape(6.dp))
                                    .clickable { primaryColorHex = hex }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                )
                                Text(name, color = NervaWhite, fontSize = 10.sp)
                            }
                        }
                    }

                    Text("Estilo de Banner del Club:", color = NervaTextMuted, fontSize = 11.sp)
                    val banners = listOf("CYBER_BLUE", "GOLD_CHAMPION", "CRIMSON_FIRE", "EMERALD_TURF")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(banners) { ban ->
                            val isSel = bannerStyle == ban
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NervaCyanAccent else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSel) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { bannerStyle = ban }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = ban,
                                    color = if (isSel) NervaBlueDeep else NervaWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
