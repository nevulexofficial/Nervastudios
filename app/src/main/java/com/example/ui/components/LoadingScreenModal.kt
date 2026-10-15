package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.Club
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LoadingScreenModal(
    userClub: Club,
    onFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    var currentStepText by remember { mutableStateOf("Iniciando motor gráfico NERVA FC...") }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(Unit) {
        currentStepText = "Cargando 'Liga Gamma Nerva' y sus 12 divisiones de 30 clubes..."
        progress.animateTo(0.25f, tween(500, easing = LinearEasing))
        delay(120)

        currentStepText = "Sincronizando patrocinadores oficiales (Fly Emirates, Nike, Red Bull, Spotify)..."
        progress.animateTo(0.55f, tween(600, easing = LinearEasing))
        delay(120)

        currentStepText = "Calibrando calendario de partidos automáticos [5:00AM, 10:00AM, 3:00PM, 8:00PM]..."
        progress.animateTo(0.85f, tween(500, easing = LinearEasing))
        delay(120)

        currentStepText = "¡Todo listo! Bienvenido a NERVA FC"
        progress.animateTo(1f, tween(400, easing = LinearEasing))
        delay(300)

        onFinished()
    }

    Dialog(
        onDismissRequest = onFinished,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF030A18))
                .testTag("pantalla_de_carga_nerva")
                .clickable { onFinished() }
        ) {
            // Background Stadium
            Image(
                painter = painterResource(id = R.drawable.bg_nerva_splash),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Vignette Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color.Transparent, Color(0xCC040D20), Color(0xF8030917)),
                            radius = 1200f
                        )
                    )
            )

            // Close button in case user wants to skip immediately
            IconButton(
                onClick = onFinished,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = NervaWhite.copy(alpha = 0.7f))
            }

            // Central Loading Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Animated Glowing Club Shield
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(NervaCyanAccent.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )
                        .border(2.dp, NervaCyanAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    NervaShieldLogo(size = 80.dp)
                }

                Text(
                    text = userClub.name.uppercase(),
                    color = NervaWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "\"${userClub.motto}\"",
                    color = Color(0xFFFFD700),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar Container
                Column(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF0F1E36))
                            .border(1.dp, Color(0xFF1E355B), RoundedCornerShape(5.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress.value)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(NervaCyanAccent, NervaBlueElectric, Color(0xFF00F0FF))
                                    )
                                )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currentStepText,
                            color = NervaCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Text(
                            text = "${(progress.value * 100).toInt()}%",
                            color = NervaWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Toca la pantalla en cualquier momento para continuar",
                    color = NervaTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
