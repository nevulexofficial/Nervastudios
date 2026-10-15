package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Club
import com.example.ui.theme.*

@Composable
fun ClubEditorModal(
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
    onDismiss: () -> Unit
) {
    var clubName by remember { mutableStateOf(userClub.name) }
    var shortName by remember { mutableStateOf(userClub.shortName) }
    var motto by remember { mutableStateOf(userClub.motto) }
    var managerName by remember { mutableStateOf(userClub.managerName) }
    var stadiumName by remember { mutableStateOf(userClub.stadiumName) }

    var selectedShape by remember { mutableStateOf(userClub.badgeShape) }
    var selectedSymbol by remember { mutableStateOf(userClub.badgeSymbol) }
    var selectedPrimaryColor by remember { mutableLongStateOf(userClub.primaryColorHex) }
    var selectedSecondaryColor by remember { mutableLongStateOf(userClub.secondaryColorHex) }
    var selectedBorderColor by remember { mutableLongStateOf(userClub.badgeBorderColorHex) }
    var selectedBannerStyle by remember { mutableStateOf(userClub.bannerStyle) }

    val shapeOptions = listOf("SHIELD" to "Escudo", "CIRCLE" to "Circular", "HEXAGON" to "Hexágono", "DIAMOND" to "Diamante")
    val symbolOptions = listOf(
        "EAGLE" to "🦅 Águila",
        "LION" to "🦁 León",
        "CROWN" to "👑 Corona",
        "STAR" to "⭐️ Estrella",
        "FOOTBALL" to "⚽ Balón",
        "FIRE" to "🔥 Fuego",
        "LIGHTNING" to "⚡ Rayo"
    )

    val colorPalette = listOf(
        0xFF0D3AE8 to "Azul Nerva",
        0xFFEF4444 to "Rojo Furia",
        0xFF10B981 to "Verde Esmeralda",
        0xFFFFD700 to "Oro Champions",
        0xFF8B5CF6 to "Púrpura Imperial",
        0xFF00F0FF to "Cian Neón",
        0xFF090D16 to "Negro Carbón",
        0xFFE2E8F0 to "Plata Polar"
    )

    val bannerStyles = listOf(
        "CYBER_NEON" to "⚡ Cyber Neón",
        "STADIUM_NIGHT" to "🏟️ Estadio Nocturno",
        "GOLD_CHAMPIONS" to "👑 Oro Champions",
        "EMERALD_PITCH" to "🌱 Césped Esmeralda"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .padding(4.dp)
                .testTag("club_editor_modal"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF07142E)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(NervaCyanAccent, NervaBlueElectric))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = NervaCyanAccent)
                        Text(
                            text = "EDICIÓN AVANZADA DE MI CLUB",
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = NervaWhite)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFF1E355B))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // LIVE PREVIEW BADGE & MOTTO
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF091C3E))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Custom Badge Canvas
                            CustomBadgeCanvas(
                                shape = selectedShape,
                                symbol = selectedSymbol,
                                primaryColor = Color(selectedPrimaryColor),
                                secondaryColor = Color(selectedSecondaryColor),
                                borderColor = Color(selectedBorderColor),
                                modifier = Modifier.size(72.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (clubName.isNotBlank()) clubName else "MI CLUB",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = "\"${if (motto.isNotBlank()) motto else "Lema del club..."}\"",
                                    color = Color(0xFFFFD700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "DT: $managerName • Estadio: $stadiumName",
                                    color = NervaTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // 1. Text Fields: Nombre, Lema, Abreviatura
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = clubName,
                            onValueChange = { clubName = it },
                            label = { Text("Nombre del Club") },
                            modifier = Modifier.weight(1.5f),
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
                            label = { Text("Código (3 letras)") },
                            modifier = Modifier.weight(0.8f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NervaWhite,
                                unfocusedTextColor = NervaWhite,
                                focusedBorderColor = NervaCyanAccent,
                                unfocusedBorderColor = Color(0xFF1E355B)
                            )
                        )
                    }

                    // Lema del Club
                    OutlinedTextField(
                        value = motto,
                        onValueChange = { motto = it },
                        label = { Text("Lema Oficial del Club (Slogan)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NervaWhite,
                            unfocusedTextColor = NervaWhite,
                            focusedBorderColor = NervaCyanAccent,
                            unfocusedBorderColor = Color(0xFF1E355B)
                        )
                    )

                    // DT & Estadio
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = managerName,
                            onValueChange = { managerName = it },
                            label = { Text("Nombre del DT / Mánager") },
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
                            label = { Text("Nombre del Estadio") },
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

                    // 2. Forma del Escudo
                    Text("FORMA DEL ESCUDO:", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shapeOptions) { (key, label) ->
                            val isSelected = selectedShape == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NervaBlueElectric else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSelected) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { selectedShape = key }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(label, color = if (isSelected) NervaWhite else NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 3. Símbolo del Escudo
                    Text("SÍMBOLO DEL ESCUDO:", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(symbolOptions) { (key, label) ->
                            val isSelected = selectedSymbol == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NervaBlueElectric else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSelected) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { selectedSymbol = key }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(label, color = if (isSelected) NervaWhite else NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 4. Color Principal
                    Text("COLOR PRINCIPAL DEL CLUB:", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(colorPalette) { (hex, name) ->
                            val isSelected = selectedPrimaryColor == hex
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(2.dp, if (isSelected) NervaCyanAccent else Color.Transparent, CircleShape)
                                    .clickable { selectedPrimaryColor = hex },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Text("✓", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    // 5. Estilo de Banner
                    Text("ESTILO DE BANNER:", color = NervaWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(bannerStyles) { (key, label) ->
                            val isSelected = selectedBannerStyle == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NervaBlueElectric else Color(0xFF0F1E40))
                                    .border(1.dp, if (isSelected) NervaCyanAccent else Color(0xFF1E355B), RoundedCornerShape(8.dp))
                                    .clickable { selectedBannerStyle = key }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(label, color = if (isSelected) NervaWhite else NervaTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Bottom Save Action
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text("Cancelar", color = NervaWhite)
                    }

                    Button(
                        onClick = {
                            onSaveClub(
                                clubName, shortName, motto, managerName, stadiumName,
                                selectedPrimaryColor, selectedSecondaryColor,
                                selectedShape, selectedSymbol, selectedBorderColor,
                                selectedBannerStyle
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar Cambios", fontWeight = FontWeight.Black, color = NervaWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBadgeCanvas(
    shape: String,
    symbol: String,
    primaryColor: Color,
    secondaryColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val badgePath = Path()
        when (shape) {
            "CIRCLE" -> {
                drawCircle(color = primaryColor, radius = w * 0.46f, center = Offset(w / 2f, h / 2f))
                drawCircle(color = borderColor, radius = w * 0.46f, center = Offset(w / 2f, h / 2f), style = Stroke(width = 3.dp.toPx()))
            }
            "HEXAGON" -> {
                badgePath.moveTo(w * 0.5f, h * 0.05f)
                badgePath.lineTo(w * 0.95f, h * 0.28f)
                badgePath.lineTo(w * 0.95f, h * 0.72f)
                badgePath.lineTo(w * 0.5f, h * 0.95f)
                badgePath.lineTo(w * 0.05f, h * 0.72f)
                badgePath.lineTo(w * 0.05f, h * 0.28f)
                badgePath.close()
                drawPath(badgePath, color = primaryColor)
                drawPath(badgePath, color = borderColor, style = Stroke(width = 3.dp.toPx()))
            }
            "DIAMOND" -> {
                badgePath.moveTo(w * 0.5f, h * 0.05f)
                badgePath.lineTo(w * 0.95f, h * 0.5f)
                badgePath.lineTo(w * 0.5f, h * 0.95f)
                badgePath.lineTo(w * 0.05f, h * 0.5f)
                badgePath.close()
                drawPath(badgePath, color = primaryColor)
                drawPath(badgePath, color = borderColor, style = Stroke(width = 3.dp.toPx()))
            }
            else -> { // SHIELD
                badgePath.moveTo(w * 0.5f, h * 0.05f)
                badgePath.lineTo(w * 0.90f, h * 0.15f)
                badgePath.lineTo(w * 0.85f, h * 0.65f)
                badgePath.lineTo(w * 0.5f, h * 0.95f)
                badgePath.lineTo(w * 0.15f, h * 0.65f)
                badgePath.lineTo(w * 0.10f, h * 0.15f)
                badgePath.close()
                drawPath(badgePath, color = primaryColor)
                drawPath(badgePath, color = borderColor, style = Stroke(width = 3.dp.toPx()))
            }
        }

        // Symbol glyph inside
        when (symbol) {
            "CROWN" -> {
                val crownPath = Path().apply {
                    moveTo(w * 0.28f, h * 0.65f)
                    lineTo(w * 0.72f, h * 0.65f)
                    lineTo(w * 0.78f, h * 0.38f)
                    lineTo(w * 0.60f, h * 0.48f)
                    lineTo(w * 0.50f, h * 0.32f)
                    lineTo(w * 0.40f, h * 0.48f)
                    lineTo(w * 0.22f, h * 0.38f)
                    close()
                }
                drawPath(crownPath, color = Color(0xFFFFD700))
            }
            "STAR" -> {
                drawCircle(color = Color(0xFFFFD700), radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.5f))
            }
            "FOOTBALL" -> {
                drawCircle(color = Color.White, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.5f))
                drawCircle(color = Color.Black, radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.5f))
            }
            "LIGHTNING" -> {
                val bolt = Path().apply {
                    moveTo(w * 0.52f, h * 0.25f)
                    lineTo(w * 0.38f, h * 0.52f)
                    lineTo(w * 0.50f, h * 0.52f)
                    lineTo(w * 0.46f, h * 0.75f)
                    lineTo(w * 0.62f, h * 0.48f)
                    lineTo(w * 0.50f, h * 0.48f)
                    close()
                }
                drawPath(bolt, color = Color(0xFFFFD700))
            }
            else -> { // EAGLE / CREST
                val eagle = Path().apply {
                    moveTo(w * 0.5f, h * 0.35f)
                    lineTo(w * 0.75f, h * 0.45f)
                    lineTo(w * 0.60f, h * 0.65f)
                    lineTo(w * 0.50f, h * 0.55f)
                    lineTo(w * 0.40f, h * 0.65f)
                    lineTo(w * 0.25f, h * 0.45f)
                    close()
                }
                drawPath(eagle, color = Color.White)
            }
        }
    }
}
