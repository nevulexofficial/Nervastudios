package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Club
import com.example.model.Player
import com.example.ui.components.AppTopBar
import com.example.ui.theme.*
import com.example.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    userClub: Club,
    squad: List<Player>,
    onNavigate: (ScreenDestination) -> Unit,
    onOpenMail: () -> Unit = {},
    onOpenClubEditor: () -> Unit = {},
    onPlayerClick: (Player) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0038D1),
                        Color(0xFF0D47A1),
                        Color(0xFF071B55)
                    )
                )
            )
            .testTag("home_screen_exact")
    ) {
        // Top Bar
        AppTopBar(
            userClub = userClub,
            onSettingsClick = { onNavigate(ScreenDestination.AJUSTES) },
            onMailClick = onOpenMail
        )

        // Main Body: Split between Hero on Left and Grid Cards on Right
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // LEFT COLUMN: Title, Slogan, and Player #10 Hero
            Box(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
            ) {
                // Background Player Hero image
                Image(
                    painter = painterResource(id = R.drawable.bg_nerva_home_hero),
                    contentDescription = "Player #10",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Gradient Overlay to ensure text legibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xEE041444),
                                    Color(0x88041444),
                                    Color(0x33041444),
                                    Color(0xDD041444)
                                )
                            )
                        )
                )

                // Text Overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = userClub.name.uppercase(),
                            color = NervaWhite,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "LIGA GAMMA NERVA • DIVISIÓN ${userClub.divisionNumber}",
                            color = NervaCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "\"${userClub.motto}\"",
                            color = Color(0xFFFFD700),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0F2B66).copy(alpha = 0.8f))
                                    .border(1.dp, NervaCyanAccent.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                    .clickable { onOpenClubEditor() }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "✏️ Personalizar Club",
                                    color = NervaWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF063B2C).copy(alpha = 0.9f))
                                    .border(1.dp, Color(0xFF00FF88).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                    .clickable { onNavigate(ScreenDestination.MULTIJUGADOR) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "🌐 Multijugador 1v1",
                                    color = Color(0xFF00FF88),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Text(
                        text = "${userClub.name}  |  DESDE 2026",
                        color = NervaWhite.copy(alpha = 0.6f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // RIGHT COLUMN: 5 Navigation Cards Grid
            Column(
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Row: CONTINUAR PARTIDA (wide) and PLANTILLA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // CONTINUAR PARTIDA Card
                    HomeActionCard(
                        title = "CONTINUAR\nPARTIDA",
                        imageRes = R.drawable.bg_stadium_banner,
                        onClick = { onNavigate(ScreenDestination.PARTIDOS) },
                        modifier = Modifier.weight(1.3f),
                        showCrownWatermark = true,
                        testTag = "card_continuar_partida"
                    )

                    // PLANTILLA Card
                    HomeActionCard(
                        title = "PLANTILLA",
                        imageRes = R.drawable.img_card_locker,
                        onClick = { onNavigate(ScreenDestination.PLANTILLA) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_plantilla"
                    )
                }

                // Bottom Row: TÁCTICAS, TRASPASOS, AJUSTES
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HomeActionCard(
                        title = "TÁCTICAS",
                        imageRes = R.drawable.bg_nerva_splash,
                        onClick = { onNavigate(ScreenDestination.TACTICAS) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_tacticas"
                    )

                    HomeActionCard(
                        title = "TRASPASOS",
                        imageRes = R.drawable.img_card_locker,
                        onClick = { onNavigate(ScreenDestination.MERCADO) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_traspasos"
                    )

                    HomeActionCard(
                        title = "AJUSTES",
                        imageRes = R.drawable.bg_stadium_banner,
                        onClick = { onNavigate(ScreenDestination.AJUSTES) },
                        modifier = Modifier.weight(1f),
                        testTag = "card_ajustes"
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showCrownWatermark: Boolean = false,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NervaCardDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E3A70))
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Darkening gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0x66061026),
                                Color(0xAA061026),
                                Color(0xEE061026)
                            )
                        )
                    )
            )

            if (showCrownWatermark) {
                Text(
                    text = "👑",
                    fontSize = 54.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 4.dp),
                    color = Color.White.copy(alpha = 0.15f)
                )
            }

            // Title + Chevron
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = NervaWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    lineHeight = 18.sp
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Ir",
                        tint = NervaWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
