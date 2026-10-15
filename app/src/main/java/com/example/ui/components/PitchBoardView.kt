package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Formation
import com.example.model.Player
import com.example.model.TacticConfig
import com.example.ui.theme.*

@Composable
fun PitchBoardView(
    formation: Formation,
    starterPlayers: List<Player>,
    selectedPlayerId: String? = null,
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    val slots = TacticConfig.getSlotsForFormation(formation)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F2B1D), Color(0xFF133B26), Color(0xFF0F2B1D))
                )
            )
            .border(2.dp, NervaCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .testTag("pitch_board_view")
    ) {
        val width = maxWidth
        val height = maxHeight

        // Draw pitch markings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val lineColor = Color.White.copy(alpha = 0.25f)
            val stroke = Stroke(width = 2.dp.toPx())

            // Pitch border
            drawRect(
                color = lineColor,
                topLeft = Offset(16.dp.toPx(), 16.dp.toPx()),
                size = Size(w - 32.dp.toPx(), h - 32.dp.toPx()),
                style = stroke
            )

            // Halfway line
            drawLine(
                color = lineColor,
                start = Offset(16.dp.toPx(), h / 2f),
                end = Offset(w - 16.dp.toPx(), h / 2f),
                strokeWidth = 2.dp.toPx()
            )

            // Center circle
            drawCircle(
                color = lineColor,
                radius = 42.dp.toPx(),
                center = Offset(w / 2f, h / 2f),
                style = stroke
            )

            // Top Penalty Box (Attacking goal)
            drawRect(
                color = lineColor,
                topLeft = Offset(w * 0.25f, 16.dp.toPx()),
                size = Size(w * 0.5f, 54.dp.toPx()),
                style = stroke
            )

            // Bottom Penalty Box (Home goal)
            drawRect(
                color = lineColor,
                topLeft = Offset(w * 0.25f, h - 16.dp.toPx() - 54.dp.toPx()),
                size = Size(w * 0.5f, 54.dp.toPx()),
                style = stroke
            )
        }

        // Overlay player nodes
        slots.forEachIndexed { i, slot ->
            val player = starterPlayers.getOrNull(i)
            val isSelected = player?.id == selectedPlayerId

            val posX = width * slot.xNormalized - 28.dp
            val posY = height * slot.yNormalized - 28.dp

            Box(
                modifier = Modifier
                    .offset(x = posX, y = posY)
                    .width(56.dp)
                    .clickable {
                        if (player != null) {
                            onPlayerClick(player)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Player Jersey circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) NervaCyan else NervaBlueElectric
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.5.dp,
                                color = if (isSelected) Color.White else NervaCyan,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = player?.overall?.toString() ?: slot.role,
                            color = if (isSelected) NervaBlueDeep else NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Player Name pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NervaBlueDeep.copy(alpha = 0.85f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = player?.name?.split(" ")?.lastOrNull() ?: slot.role,
                            color = if (isSelected) NervaCyan else NervaWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
