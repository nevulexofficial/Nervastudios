package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NervaBlueElectric
import com.example.ui.theme.NervaCyanAccent
import com.example.ui.theme.NervaRoyalBlue
import com.example.ui.theme.NervaWhite

// Shield shape
val ShieldShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    moveTo(w * 0.15f, 0f)
    lineTo(w * 0.85f, 0f)
    cubicTo(w * 0.95f, 0f, w, h * 0.1f, w, h * 0.25f)
    cubicTo(w, h * 0.65f, w * 0.6f, h * 0.9f, w * 0.5f, h)
    cubicTo(w * 0.4f, h * 0.9f, 0f, h * 0.65f, 0f, h * 0.25f)
    cubicTo(0f, h * 0.1f, w * 0.05f, 0f, w * 0.15f, 0f)
    close()
}

@Composable
fun NervaShieldLogo(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(ShieldShape)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0055FF), Color(0xFF0A2266), Color(0xFF06143A))
                )
            )
            .border(1.5.dp, Color.White, ShieldShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            // Crown
            Text(
                text = "👑",
                fontSize = (size.value * 0.26f).sp
            )
            Text(
                text = "NERVA",
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = (size.value * 0.16f).sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "FC",
                color = NervaCyanAccent,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.12f).sp
            )
        }
    }
}
