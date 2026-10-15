package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Path
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

/**
 * Geometric Crown Icon from NERVA logo design.
 * Matches the official solid white 3-peak crown silhouette.
 */
@Composable
fun NervaCrownIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            // Start at bottom left
            moveTo(w * 0.10f, h * 0.90f)
            // Left angled peak
            lineTo(w * 0.04f, h * 0.22f)
            // Left inner valley
            lineTo(w * 0.35f, h * 0.58f)
            // Center taller peak
            lineTo(w * 0.50f, h * 0.06f)
            // Right inner valley
            lineTo(w * 0.65f, h * 0.58f)
            // Right angled peak
            lineTo(w * 0.96f, h * 0.22f)
            // Bottom right
            lineTo(w * 0.90f, h * 0.90f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

/**
 * Pantalla de Carga oficial de NERVA Football Manager.
 *
 * Muestra la carga de assets iniciales (estadio VIP, motor 2D, plantillas de las 12 divisiones,
 * patrocinadores y cliente Supabase) con una barra de progreso que al llegar al 100%
 * permite entrar automáticamente a la aplicación sin necesidad de presionar ningún botón.
 */
@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    var currentAssetLabel by remember { mutableStateOf("Iniciando motor gráfico...") }

    LaunchedEffect(Unit) {
        // Step 1: Textures, stadium and 2D engine
        currentAssetLabel = "Cargando texturas del estadio VIP y motor 2D..."
        progress.animateTo(0.25f, animationSpec = tween(550, easing = LinearEasing))
        delay(120)

        // Step 2: Shields, squads and 12 divisions
        currentAssetLabel = "Cargando escudos, plantillas y 12 divisiones de Liga Gamma..."
        progress.animateTo(0.55f, animationSpec = tween(650, easing = LinearEasing))
        delay(120)

        // Step 3: Tactics, sponsors and audio engine
        currentAssetLabel = "Calibrando tácticas, patrocinadores y motor de música..."
        progress.animateTo(0.85f, animationSpec = tween(550, easing = LinearEasing))
        delay(120)

        // Step 4: Finalize assets & automatic entry
        currentAssetLabel = "¡Assets cargados! Entrando a NERVA FC..."
        progress.animateTo(1f, animationSpec = tween(350, easing = LinearEasing))
        delay(300)

        // Entrada automática garantizada al completar la carga
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("splash_screen")
            .clickable { onFinished() } // Permite saltar manualmente si el usuario lo desea
    ) {
        // 1. Fondo fotográfico del estadio VIP al atardecer
        Image(
            painter = painterResource(id = R.drawable.bg_nerva_splash),
            contentDescription = "NERVA Stadium Dusk VIP Suite",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Viñeta oscura cinematográfica y gradiente sutil
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x77020612),
                            Color(0xCC020612)
                        ),
                        radius = 1200f
                    )
                )
        )

        // Superior Izquierda: Lema
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 36.dp, top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "TU VISIÓN.",
                color = NervaWhite.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            )
            Text(
                text = "TU EQUIPO.",
                color = NervaWhite.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            )
            Text(
                text = "TU HISTORIA.",
                color = NervaWhite.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            )
        }

        // Superior Derecha: Pilares del juego
        Text(
            text = "PLANIFICA   •   FICHA   •   COMPITE",
            color = NervaWhite.copy(alpha = 0.85f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 36.dp, top = 28.dp)
        )

        // Centro: Logotipo Oficial NERVA FOOTBALL MANAGER
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Corona geométrica blanca estilizada idéntica a la imagen oficial
            NervaCrownIcon(
                modifier = Modifier
                    .size(width = 54.dp, height = 38.dp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Título principal NERVA
            Text(
                text = "NERVA",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 56.sp,
                letterSpacing = 8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo con líneas horizontales a los lados
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .height(1.5.dp)
                        .background(Color.White.copy(alpha = 0.85f))
                )
                Text(
                    text = "FOOTBALL MANAGER",
                    color = Color.White.copy(alpha = 0.92f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 3.sp
                )
                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .height(1.5.dp)
                        .background(Color.White.copy(alpha = 0.85f))
                )
            }
        }

        // Parte Inferior: Barra de carga blanca estilizada y texto "CARGANDO..."
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra de progreso encapsulada
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .border(0.75.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = progress.value)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Texto inferior "CARGANDO..." con indicador dinámico de assets
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "CARGANDO...",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )

                Text(
                    text = "$currentAssetLabel (${(progress.value * 100).toInt()}%)",
                    color = NervaCyanAccent.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
