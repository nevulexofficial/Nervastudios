package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.ui.theme.NervaCyanAccent
import com.example.ui.theme.NervaWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    var currentAssetLabel by remember { mutableStateOf("Iniciando motor gráfico...") }

    LaunchedEffect(Unit) {
        // Step 1: Textures and Stadium
        currentAssetLabel = "Cargando texturas del estadio y campo 2D..."
        progress.animateTo(0.25f, animationSpec = tween(600, easing = LinearEasing))
        delay(150)

        // Step 2: Shields, squads and editable flags
        currentAssetLabel = "Cargando escudos, plantillas y banderas editables..."
        progress.animateTo(0.55f, animationSpec = tween(700, easing = LinearEasing))
        delay(150)

        // Step 3: Tactics and sponsors
        currentAssetLabel = "Cargando motor de tácticas y patrocinadores..."
        progress.animateTo(0.85f, animationSpec = tween(600, easing = LinearEasing))
        delay(150)

        // Step 4: Finalize
        currentAssetLabel = "¡Todo listo! Bienvenido a NERVA FC"
        progress.animateTo(1f, animationSpec = tween(400, easing = LinearEasing))
        delay(300)

        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("splash_screen")
            .clickable { onFinished() }
    ) {
        // 1. Photographic Dusk Stadium Background
        Image(
            painter = painterResource(id = R.drawable.bg_nerva_splash),
            contentDescription = "NERVA Stadium Dusk",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Vignette Radial Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color(0xAA020612), Color(0xEE020612)),
                        radius = 1200f
                    )
                )
        )

        // Top Left Motto
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 32.dp, top = 24.dp)
        ) {
            Text(
                text = "TU VISIÓN.",
                color = NervaWhite.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "TU EQUIPO.",
                color = NervaWhite.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "TU HISTORIA.",
                color = NervaWhite.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        // Top Right Motto
        Text(
            text = "PLANIFICA   •   FICHA   •   COMPITE",
            color = NervaWhite.copy(alpha = 0.7f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 32.dp, top = 24.dp)
        )

        // Center Brand Emblem
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Crown
            Text(
                text = "👑",
                fontSize = 42.sp
            )
            Spacer(modifier = Modifier.height(2.dp))

            // Giant NERVA Title
            Text(
                text = "NERVA",
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = 58.sp,
                letterSpacing = 8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle with flanked rules
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(1.5.dp)
                        .background(NervaWhite.copy(alpha = 0.7f))
                )
                Text(
                    text = "FOOTBALL MANAGER",
                    color = NervaWhite.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 3.sp
                )
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(1.5.dp)
                        .background(NervaWhite.copy(alpha = 0.7f))
                )
            }
        }

        // Bottom Loading Progress Bar and Asset Loading Status
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Asset Step Status
            Text(
                text = currentAssetLabel,
                color = NervaCyanAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Track & Animated fill
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress.value)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.White, NervaCyanAccent, Color(0xFF00E5FF))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Loading label and percentage
            Row(
                modifier = Modifier.width(320.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CARGANDO ASSETS...",
                    color = NervaWhite.copy(alpha = 0.75f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "${(progress.value * 100).toInt()}%",
                    color = NervaCyanAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
