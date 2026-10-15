package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.BallState
import com.example.engine.MatchEngineState
import com.example.model.Club
import com.example.ui.theme.*

@Composable
fun MatchEngine2DCanvas(
    state: MatchEngineState,
    homeClub: Club,
    awayClub: Club,
    modifier: Modifier = Modifier
) {
    val homeColor = NervaCyan
    val awayColor = Color(awayClub.primaryColorHex)

    // Current thought of the ball holder or main actor
    val activeHolder = state.players.firstOrNull { it.id == state.ball.holderId }
    val holderThought = activeHolder?.let { "${it.name}: \"${it.aiThought}\"" } ?: state.lastActionDescription

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, NervaCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("match_engine_2d_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Draw pitch background with alternating grass cuts
            val stripes = 12
            val stripeHeight = h / stripes
            for (i in 0 until stripes) {
                val stripeColor = if (i % 2 == 0) Color(0xFF1E5B33) else Color(0xFF184E2B)
                drawRect(
                    color = stripeColor,
                    topLeft = Offset(0f, i * stripeHeight),
                    size = Size(w, stripeHeight)
                )
            }

            // 2. Draw Pitch Tactical Markings
            val linePaint = Color.White.copy(alpha = 0.45f)
            val strokeThick = 2.dp.toPx()
            val margin = 12.dp.toPx()

            // Outer boundary
            drawRect(
                color = linePaint,
                topLeft = Offset(margin, margin),
                size = Size(w - 2 * margin, h - 2 * margin),
                style = Stroke(width = strokeThick)
            )

            // Halfway line
            drawLine(
                color = linePaint,
                start = Offset(margin, h / 2f),
                end = Offset(w - margin, h / 2f),
                strokeWidth = strokeThick
            )

            // Center Circle
            drawCircle(
                color = linePaint,
                radius = 36.dp.toPx(),
                center = Offset(w / 2f, h / 2f),
                style = Stroke(width = strokeThick)
            )

            // Center Spot
            drawCircle(
                color = linePaint,
                radius = 3.dp.toPx(),
                center = Offset(w / 2f, h / 2f)
            )

            // Top Penalty Box (Defended by Home)
            drawRect(
                color = linePaint,
                topLeft = Offset(w * 0.22f, margin),
                size = Size(w * 0.56f, 48.dp.toPx()),
                style = Stroke(width = strokeThick)
            )

            // Top Penalty Spot (0.50f, 0.18f in ratio)
            drawCircle(
                color = linePaint,
                radius = 2.5.dp.toPx(),
                center = Offset(w * 0.50f, margin + 34.dp.toPx())
            )

            // Top Goal Nets (Netting Pattern)
            drawRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(w * 0.38f, margin - 8.dp.toPx()),
                size = Size(w * 0.24f, 8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // Bottom Penalty Box (Defended by Away)
            drawRect(
                color = linePaint,
                topLeft = Offset(w * 0.22f, h - margin - 48.dp.toPx()),
                size = Size(w * 0.56f, 48.dp.toPx()),
                style = Stroke(width = strokeThick)
            )

            // Bottom Penalty Spot
            drawCircle(
                color = linePaint,
                radius = 2.5.dp.toPx(),
                center = Offset(w * 0.50f, h - margin - 34.dp.toPx())
            )

            // Bottom Goal Nets
            drawRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(w * 0.38f, h - margin),
                size = Size(w * 0.24f, 8.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // 3. Draw Players
            state.players.forEach { p ->
                if (p.isRedCarded) return@forEach // Expelled player off pitch

                val px = p.x * w
                val py = p.y * h
                val isHolder = p.id == state.ball.holderId
                val playerColor = if (p.isHomeTeam) homeColor else awayColor
                val radius = 7.5.dp.toPx()

                // Glow ring if ball holder
                if (isHolder) {
                    drawCircle(
                        color = Color.Yellow.copy(alpha = 0.6f),
                        radius = radius + 6.dp.toPx(),
                        center = Offset(px, py)
                    )
                }

                // Celebration golden halo
                if (p.isCelebrating) {
                    drawCircle(
                        color = NervaGold.copy(alpha = 0.7f),
                        radius = radius + 8.dp.toPx(),
                        center = Offset(px, py)
                    )
                }

                // Player Shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.45f),
                    radius = radius,
                    center = Offset(px + 1.5.dp.toPx(), py + 2.5.dp.toPx())
                )

                // Player Jersey Body
                drawCircle(
                    color = playerColor,
                    radius = radius,
                    center = Offset(px, py)
                )

                // Outer border
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = Offset(px, py),
                    style = Stroke(width = 1.2.dp.toPx())
                )

                // Yellow card marker on player if carded
                if (p.yellowCards > 0) {
                    drawRect(
                        color = Color(0xFFFBBF24),
                        topLeft = Offset(px + radius - 1.dp.toPx(), py - radius - 3.dp.toPx()),
                        size = Size(4.dp.toPx(), 6.dp.toPx())
                    )
                }
            }

            // 4. Draw Ball with physics
            val bx = state.ball.x * w
            val by = state.ball.y * h

            // Ball shadow
            drawCircle(
                color = Color.Black.copy(alpha = 0.5f),
                radius = 5.dp.toPx(),
                center = Offset(bx + 1.5.dp.toPx(), by + 2.dp.toPx())
            )

            // Ball Glow
            drawCircle(
                color = Color.White.copy(alpha = 0.35f),
                radius = 7.dp.toPx(),
                center = Offset(bx, by)
            )

            // Ball White Core
            drawCircle(
                color = Color.White,
                radius = 4.5.dp.toPx(),
                center = Offset(bx, by)
            )

            // Ball Center Pattern
            drawCircle(
                color = Color.Black,
                radius = 2.dp.toPx(),
                center = Offset(bx, by)
            )
        }

        // Overlay 1: Special Event Banners (GOAL, PENALTY, RED CARD, YELLOW CARD)
        state.bannerEventTitle?.let { bannerText ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                val bannerBg = when {
                    bannerText.contains("GOOOL") -> Brush.horizontalGradient(listOf(NervaCyan, NervaGold))
                    bannerText.contains("PENALTI") -> Brush.horizontalGradient(listOf(Color(0xFFE11D48), Color(0xFFFB7185)))
                    bannerText.contains("ROJA") || bannerText.contains("EXPULSIÓN") -> Brush.horizontalGradient(listOf(Color(0xFFDC2626), Color(0xFF991B1B)))
                    else -> Brush.horizontalGradient(listOf(NervaBlueElectric, NervaCyan))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(bannerBg)
                        .border(1.5.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(horizontal = 22.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = bannerText,
                        color = NervaBlueDeep,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Overlay 2: Live Action and Player AI Thinking Ticker at bottom of canvas
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NervaBlueDeep.copy(alpha = 0.90f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.minute}' • ${state.lastActionDescription}",
                    color = NervaCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                activeHolder?.let { holder ->
                    Text(
                        text = "🧠 ${holder.name}: ${holder.aiThought}",
                        color = NervaGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
