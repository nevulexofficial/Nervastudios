package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.theme.NervaCyan
import com.example.ui.theme.NervaWhite

@Composable
fun ClubBadge(
    club: Club,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    val primaryColor = Color(club.primaryColorHex)
    val secondaryColor = Color(club.secondaryColorHex)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3))
            .background(
                Brush.linearGradient(
                    listOf(primaryColor, secondaryColor)
                )
            )
            .border(
                width = 1.5.dp,
                color = if (club.isUserClub) NervaCyan else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(size / 3)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = club.shortName,
            color = NervaWhite,
            fontWeight = FontWeight.Black,
            fontSize = (size.value * 0.36f).sp,
            letterSpacing = 0.5.sp
        )
    }
}
