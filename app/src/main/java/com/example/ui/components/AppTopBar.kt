package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.theme.*

@Composable
fun AppTopBar(
    userClub: Club,
    title: String? = null,
    subtitle: String? = null,
    onSettingsClick: () -> Unit = {},
    onFlagClick: (() -> Unit)? = null,
    onMailClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Section: Section Title or Brand
        if (title != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = NervaCyanAccent,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = title,
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 0.5.sp
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            color = NervaTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        // Right Section: Budget + Club + Icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Budget Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NervaCardDark)
                    .border(1.dp, Color(0xFF1B3B70), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(NervaGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$",
                            color = NervaGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Column {
                        Text(
                            text = userClub.formattedBudget,
                            color = NervaWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "PRESUPUESTO",
                            color = NervaTextMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Club Badge & Season
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NervaShieldLogo(size = 28.dp)

                // Editable Club Flag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable(enabled = onFlagClick != null) { onFlagClick?.invoke() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = userClub.flag,
                        fontSize = 14.sp
                    )
                }

                Column {
                    Text(
                        text = userClub.name,
                        color = NervaWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "TEMPORADA 2026/27",
                        color = NervaTextMuted,
                        fontSize = 9.sp
                    )
                }
            }

            // Action Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onMailClick,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("topbar_mail_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Buzón y Referrals",
                        tint = NervaWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Alertas",
                        tint = NervaWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onSettingsClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = NervaWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
