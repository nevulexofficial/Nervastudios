package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Club
import com.example.ui.components.AppTopBar
import com.example.ui.components.NervaShieldLogo
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    userClub: Club,
    modifier: Modifier = Modifier
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var highQuality2D by remember { mutableStateOf(true) }
    var autoSave by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NervaSidebarDarker)
    ) {
        AppTopBar(
            userClub = userClub,
            title = "AJUSTES DEL CLUB",
            subtitle = "Configuración del juego y opciones de gestión"
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Club Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaCardDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NervaShieldLogo(size = 72.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = userClub.name, color = NervaWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text(text = "Estadio: ${userClub.stadiumName}", color = NervaTextMuted, fontSize = 12.sp)
                    Text(text = "Director Técnico: ${userClub.managerName}", color = NervaCyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "NERVA FOOTBALL MANAGER 2026/27", color = NervaWhite.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Options List
            Card(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NervaCardDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "PREFERENCIAS DE SIMULACIÓN", color = NervaCyanAccent, fontWeight = FontWeight.Black, fontSize = 13.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Motor 2D con efectos avanzados", color = NervaWhite, fontSize = 13.sp)
                        Switch(
                            checked = highQuality2D,
                            onCheckedChange = { highQuality2D = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Comentarios en directo automáticos", color = NervaWhite, fontSize = 13.sp)
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Guardado automático de fichajes", color = NervaWhite, fontSize = 13.sp)
                        Switch(
                            checked = autoSave,
                            onCheckedChange = { autoSave = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NervaWhite, checkedTrackColor = NervaBlueElectric)
                        )
                    }
                }
            }
        }
    }
}
