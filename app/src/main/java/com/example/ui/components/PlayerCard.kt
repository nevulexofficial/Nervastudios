package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.model.PositionType
import com.example.ui.theme.*

@Composable
fun PositionBadge(
    position: PositionType,
    modifier: Modifier = Modifier
) {
    val (bg, textColor) = when (position) {
        PositionType.POR -> Pair(Color(0xFFEAB308).copy(alpha = 0.2f), Color(0xFFFACC15))
        PositionType.DEF -> Pair(Color(0xFF2563EB).copy(alpha = 0.2f), Color(0xFF60A5FA))
        PositionType.MED -> Pair(Color(0xFF059669).copy(alpha = 0.2f), Color(0xFF34D399))
        PositionType.DEL -> Pair(Color(0xFFDC2626).copy(alpha = 0.2f), Color(0xFFF87171))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = position.displayName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OverallBadge(
    overall: Int,
    size: Int = 40,
    modifier: Modifier = Modifier
) {
    val ratingColor = when {
        overall >= 88 -> NervaCyan
        overall >= 84 -> NervaGreen
        overall >= 80 -> NervaGold
        else -> NervaTextMuted
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(NervaBlueSurfaceVariant)
            .border(1.5.dp, ratingColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = overall.toString(),
            color = NervaWhite,
            fontWeight = FontWeight.Black,
            fontSize = (size * 0.42f).sp
        )
    }
}

@Composable
fun PlayerItemRow(
    player: Player,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onFlagClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("player_row_${player.id}")
            .clickable { onClick() }
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) NervaCyan else NervaBlueSurfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) NervaBlueContainer.copy(alpha = 0.6f) else NervaBlueSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OverallBadge(overall = player.overall)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PositionBadge(position = player.position)

                    // Editable Flag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable(enabled = onFlagClick != null) { onFlagClick?.invoke() }
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = player.nationality,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = player.name,
                        color = NervaWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PAC ${player.pace} • DIS ${player.shooting} • PAS ${player.passing}",
                        color = NervaTextMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = player.formattedValue,
                        color = NervaCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}
