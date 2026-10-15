package com.example.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.theme.NervaCyan
import com.example.ui.theme.NervaWhite
import java.io.File

@Composable
fun ClubBadge(
    club: Club,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val primaryColor = Color(club.primaryColorHex)
    val secondaryColor = Color(club.secondaryColorHex)
    val cornerRadius = size / 3

    // Decode custom uploaded logo if present
    val customBitmap = remember(club.customLogoUri) {
        val uriStr = club.customLogoUri
        if (!uriStr.isNullOrBlank()) {
            try {
                when {
                    uriStr.startsWith("data:image") -> {
                        val base64Data = uriStr.substringAfter("base64,")
                        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                    }
                    uriStr.startsWith("content://") -> {
                        context.contentResolver.openInputStream(Uri.parse(uriStr))?.use { stream ->
                            BitmapFactory.decodeStream(stream)?.asImageBitmap()
                        }
                    }
                    uriStr.startsWith("file://") -> {
                        val path = uriStr.removePrefix("file://")
                        BitmapFactory.decodeFile(path)?.asImageBitmap()
                    }
                    File(uriStr).exists() -> {
                        BitmapFactory.decodeFile(uriStr)?.asImageBitmap()
                    }
                    else -> null
                }
            } catch (_: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    listOf(primaryColor, secondaryColor)
                )
            )
            .border(
                width = 1.5.dp,
                color = if (club.isUserClub) NervaCyan else Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (customBitmap != null) {
            Image(
                bitmap = customBitmap,
                contentDescription = "Logo de ${club.name}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(cornerRadius)),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = club.shortName,
                color = NervaWhite,
                fontWeight = FontWeight.Black,
                fontSize = (size.value * 0.36f).sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
