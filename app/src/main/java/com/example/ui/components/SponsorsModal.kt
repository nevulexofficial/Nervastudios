package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Sponsor
import com.example.ui.theme.*

@Composable
fun SponsorsModal(
    sponsors: List<Sponsor>,
    onToggleSponsor: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val totalWeeklyIncome = sponsors.filter { it.isSigned }.sumOf { it.weeklyIncome }
    val totalWinBonus = sponsors.filter { it.isSigned }.sumOf { it.winBonus }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("sponsors_modal"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NervaCardDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E3A70))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NervaGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = null,
                                tint = NervaGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PATROCINADORES DE NERVA FC",
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Gestión de contratos comerciales y sponsors",
                                color = NervaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = NervaWhite.copy(alpha = 0.7f)
                        )
                    }
                }

                // Summary Financial Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NervaSidebarDark),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E3A70))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "INGRESO SEMANAL", color = NervaTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "$ " + String.format("%,d", totalWeeklyIncome).replace(",", "."),
                                color = NervaGreen,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color(0xFF1E3A70)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "BONO POR VICTORIA", color = NervaTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "+$ " + String.format("%,d", totalWinBonus).replace(",", "."),
                                color = NervaCyanAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Sponsors List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(sponsors) { sponsor ->
                        SponsorItemCard(
                            sponsor = sponsor,
                            onToggle = { onToggleSponsor(sponsor.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SponsorItemCard(
    sponsor: Sponsor,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (sponsor.isSigned) Color(0xFF0C1B3E) else Color(0xFF071228)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (sponsor.isSigned) NervaCyanAccent.copy(alpha = 0.6f) else Color(0xFF1A3358)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Logo Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (sponsor.isSigned) NervaBlueElectric.copy(alpha = 0.3f) else Color(0xFF132240))
                    .border(1.dp, if (sponsor.isSigned) NervaCyanAccent else Color(0xFF233B68), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sponsor.logoTag,
                    color = if (sponsor.isSigned) NervaWhite else NervaTextMuted,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = sponsor.name,
                        color = NervaWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    if (sponsor.isSigned) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Activo",
                            tint = NervaCyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = "${sponsor.type} • ${sponsor.contractDurationMonths} meses de contrato",
                    color = NervaTextMuted,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sponsor.formattedWeeklyIncome,
                        color = NervaGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = sponsor.formattedWinBonus,
                        color = NervaCyanAccent,
                        fontSize = 10.sp
                    )
                }
            }

            // Action Button
            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sponsor.isSigned) Color(0xFF334155) else NervaBlueElectric
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (sponsor.isSigned) "Renovar" else "Firmar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NervaWhite
                )
            }
        }
    }
}
