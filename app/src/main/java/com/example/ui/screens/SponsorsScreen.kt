package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.model.Sponsor
import com.example.ui.components.AppTopBar
import com.example.ui.theme.*

@Composable
fun SponsorsScreen(
    userClub: Club,
    sponsors: List<Sponsor>,
    onToggleSponsor: (String) -> Unit,
    onOpenMail: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalWeeklyIncome = sponsors.filter { it.isSigned }.sumOf { it.weeklyIncome }
    val totalWinBonus = sponsors.filter { it.isSigned }.sumOf { it.winBonus }
    val signedCount = sponsors.count { it.isSigned }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaBlueDeep)
            .testTag("sponsors_screen_exclusive")
    ) {
        // App Top Bar
        AppTopBar(
            userClub = userClub,
            title = "PATROCINADORES OFICIALES",
            subtitle = "Contratos comerciales y sponsors exclusivos",
            onMailClick = onOpenMail
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Commercial Header Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF071838)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(NervaCyanAccent, NervaBlueElectric, Color(0xFFEAB308))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "PANEL COMERCIAL DE ${userClub.name.uppercase()}",
                                        color = NervaWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                }
                                Text(
                                    text = "Marcas mundiales que financian tu ascenso en Liga Gamma Nerva",
                                    color = NervaCyanAccent,
                                    fontSize = 11.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NervaGreen.copy(alpha = 0.2f))
                                    .border(1.dp, NervaGreen, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$signedCount / ${sponsors.size} ACTIVOS",
                                    color = NervaGreen,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Financial Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Weekly Revenue
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C234E))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("INGRESOS SEMANALES", color = NervaTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "$ " + String.format("%,d", totalWeeklyIncome).replace(",", ".") + "/sem",
                                        color = NervaGreen,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            // Match Win Bonus
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C234E))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("BONO POR VICTORIA", color = NervaTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "+$ " + String.format("%,d", totalWinBonus).replace(",", ".") + " / PG",
                                        color = NervaCyanAccent,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            // Season Estimation
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C234E))
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("PRESUPUESTO DEL CLUB", color = NervaTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = userClub.formattedBudget,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Section Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PATROCINADORES DISPONIBLES & CONTRATOS",
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Toca para firmar o rescindir",
                        color = NervaTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // 3. Sponsor Cards with Dedicated Brand Vector Logos
            items(sponsors) { sponsor ->
                ExclusiveSponsorCard(
                    sponsor = sponsor,
                    onToggle = { onToggleSponsor(sponsor.id) }
                )
            }
        }
    }
}

@Composable
fun ExclusiveSponsorCard(
    sponsor: Sponsor,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (sponsor.isSigned) NervaCyanAccent.copy(alpha = 0.8f) else Color(0xFF1E355B),
                RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (sponsor.isSigned) Color(0xFF0B1F44) else Color(0xFF07142E)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Vector / Graphic Brand Logo Box
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(getSponsorBrandBg(sponsor.id))
                        .border(1.5.dp, getSponsorBrandBorder(sponsor.id), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    SponsorBrandLogoCanvas(sponsorId = sponsor.id, logoTag = sponsor.logoTag)
                }

                // Brand details
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = sponsor.name,
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        if (sponsor.isSigned) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Activo",
                                tint = NervaCyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "${sponsor.type} • ${sponsor.contractDurationMonths} meses • ${sponsor.description}",
                        color = NervaTextMuted,
                        fontSize = 11.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Semanal:", color = NervaTextMuted, fontSize = 10.sp)
                            Text(
                                text = sponsor.formattedWeeklyIncome,
                                color = NervaGreen,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Victoria:", color = NervaTextMuted, fontSize = 10.sp)
                            Text(
                                text = sponsor.formattedWinBonus,
                                color = NervaCyanAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Button Action
                Button(
                    onClick = onToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (sponsor.isSigned) Color(0xFF334155) else NervaBlueElectric
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (sponsor.isSigned) "Firmado ✓" else "Firmar",
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SponsorBrandLogoCanvas(sponsorId: String, logoTag: String) {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        when {
            sponsorId.contains("fly") || logoTag.contains("FLY") -> {
                // Emirates Gold Wings + Red Typography
                val gold = Color(0xFFFFD700)
                val path = Path().apply {
                    moveTo(w * 0.15f, h * 0.65f)
                    cubicTo(w * 0.3f, h * 0.2f, w * 0.7f, h * 0.2f, w * 0.85f, h * 0.65f)
                    lineTo(w * 0.5f, h * 0.45f)
                    close()
                }
                drawPath(path, color = gold)
                drawCircle(color = Color(0xFFEF4444), radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.62f))
            }

            sponsorId.contains("nike") || logoTag.contains("NIKE") -> {
                // Nike Swoosh Dynamic Arc
                val swooshPath = Path().apply {
                    moveTo(w * 0.15f, h * 0.55f)
                    cubicTo(w * 0.35f, h * 0.82f, w * 0.75f, h * 0.80f, w * 0.90f, h * 0.25f)
                    cubicTo(w * 0.65f, h * 0.65f, w * 0.35f, h * 0.60f, w * 0.15f, h * 0.55f)
                    close()
                }
                drawPath(swooshPath, color = Color.White)
            }

            sponsorId.contains("redbull") || logoTag.contains("BULL") -> {
                // Red Bull Charging Silhouette with Yellow Sun
                drawCircle(color = Color(0xFFFFCC00), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.5f))
                // Left Horn/Bull
                drawCircle(color = Color(0xFFDC2626), radius = w * 0.15f, center = Offset(w * 0.38f, h * 0.5f))
                // Right Horn/Bull
                drawCircle(color = Color(0xFFDC2626), radius = w * 0.15f, center = Offset(w * 0.62f, h * 0.5f))
            }

            sponsorId.contains("spotify") || logoTag.contains("SPOT") -> {
                // Spotify Concentric Sound Waves in Vibrant Green
                drawCircle(color = Color(0xFF1ED760), radius = w * 0.45f, center = Offset(w * 0.5f, h * 0.5f))
                val waveColor = Color.Black
                drawArc(
                    color = waveColor,
                    startAngle = 210f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(w * 0.2f, h * 0.22f),
                    size = Size(w * 0.6f, h * 0.5f),
                    style = Stroke(width = 3.5f)
                )
                drawArc(
                    color = waveColor,
                    startAngle = 210f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(w * 0.25f, h * 0.34f),
                    size = Size(w * 0.5f, h * 0.4f),
                    style = Stroke(width = 3.2f)
                )
                drawArc(
                    color = waveColor,
                    startAngle = 210f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(w * 0.32f, h * 0.48f),
                    size = Size(w * 0.36f, h * 0.3f),
                    style = Stroke(width = 2.8f)
                )
            }

            sponsorId.contains("sony") || logoTag.contains("PS5") || logoTag.contains("SONY") -> {
                // PlayStation △ ◯ ✕ ▢ Symbols
                val colorCyan = Color(0xFF00F0FF)
                val colorWhite = Color.White
                // Triangle
                val tri = Path().apply {
                    moveTo(w * 0.32f, h * 0.25f)
                    lineTo(w * 0.20f, h * 0.45f)
                    lineTo(w * 0.44f, h * 0.45f)
                    close()
                }
                drawPath(tri, color = Color(0xFF10B981), style = Stroke(width = 2.5f))
                // Circle
                drawCircle(color = Color(0xFFEF4444), radius = w * 0.10f, center = Offset(w * 0.70f, h * 0.35f), style = Stroke(width = 2.5f))
                // Cross X
                drawLine(color = Color(0xFF3B82F6), start = Offset(w * 0.22f, h * 0.60f), end = Offset(w * 0.42f, h * 0.80f), strokeWidth = 2.5f)
                drawLine(color = Color(0xFF3B82F6), start = Offset(w * 0.42f, h * 0.60f), end = Offset(w * 0.22f, h * 0.80f), strokeWidth = 2.5f)
                // Square
                drawRect(color = Color(0xFFEC4899), topLeft = Offset(w * 0.60f, h * 0.60f), size = Size(w * 0.20f, w * 0.20f), style = Stroke(width = 2.5f))
            }

            sponsorId.contains("adidas") || logoTag.contains("ADI") -> {
                // Adidas 3 Diagonal Stripes
                val white = Color.White
                drawRoundRect(color = white, topLeft = Offset(w * 0.22f, h * 0.55f), size = Size(w * 0.12f, h * 0.30f))
                drawRoundRect(color = white, topLeft = Offset(w * 0.42f, h * 0.40f), size = Size(w * 0.12f, h * 0.45f))
                drawRoundRect(color = white, topLeft = Offset(w * 0.62f, h * 0.25f), size = Size(w * 0.12f, h * 0.60f))
            }

            else -> {
                // Crypto / Tech Hexagon Crest
                val gold = Color(0xFFFFD700)
                val hex = Path().apply {
                    moveTo(w * 0.5f, h * 0.1f)
                    lineTo(w * 0.85f, h * 0.3f)
                    lineTo(w * 0.85f, h * 0.7f)
                    lineTo(w * 0.5f, h * 0.9f)
                    lineTo(w * 0.15f, h * 0.7f)
                    lineTo(w * 0.15f, h * 0.3f)
                    close()
                }
                drawPath(hex, color = gold, style = Stroke(width = 3f))
                drawCircle(color = NervaCyanAccent, radius = w * 0.16f, center = Offset(w * 0.5f, h * 0.5f))
            }
        }
    }
}

fun getSponsorBrandBg(id: String): Color {
    return when {
        id.contains("fly") -> Color(0xFF800000)
        id.contains("nike") -> Color(0xFF111827)
        id.contains("redbull") -> Color(0xFF0F172A)
        id.contains("spotify") -> Color(0xFF0B291A)
        id.contains("sony") -> Color(0xFF0C1938)
        id.contains("adidas") -> Color(0xFF0F172A)
        else -> Color(0xFF1E1B4B)
    }
}

fun getSponsorBrandBorder(id: String): Color {
    return when {
        id.contains("fly") -> Color(0xFFFFD700)
        id.contains("nike") -> Color(0xFFFFFFFF)
        id.contains("redbull") -> Color(0xFFEF4444)
        id.contains("spotify") -> Color(0xFF1ED760)
        id.contains("sony") -> Color(0xFF00F0FF)
        id.contains("adidas") -> Color(0xFFFFFFFF)
        else -> Color(0xFF8B5CF6)
    }
}
