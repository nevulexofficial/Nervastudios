package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

data class FlagOption(val flag: String, val countryName: String)

val commonFlags = listOf(
    FlagOption("🇨🇱", "Chile"),
    FlagOption("🇦🇷", "Argentina"),
    FlagOption("🇧🇷", "Brasil"),
    FlagOption("🇺🇾", "Uruguay"),
    FlagOption("🇨🇴", "Colombia"),
    FlagOption("🇪🇸", "España"),
    FlagOption("🇮🇹", "Italia"),
    FlagOption("🇩🇪", "Alemania"),
    FlagOption("🇫🇷", "Francia"),
    FlagOption("🇵🇹", "Portugal"),
    FlagOption("🏴󠁧󠁢󠁥󠁮󠁧󠁿", "Inglaterra"),
    FlagOption("🇳🇱", "Países Bajos"),
    FlagOption("🇲🇽", "México"),
    FlagOption("🇵🇪", "Perú"),
    FlagOption("🇪🇨", "Ecuador"),
    FlagOption("🇻🇪", "Venezuela"),
    FlagOption("🇵🇾", "Paraguay"),
    FlagOption("🇺🇸", "Estados Unidos"),
    FlagOption("🇯🇵", "Japón"),
    FlagOption("🇰🇷", "Corea del Sur")
)

@Composable
fun FlagPickerModal(
    title: String,
    currentFlag: String,
    onSelectFlag: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customFlagText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("flag_picker_modal"),
            shape = RoundedCornerShape(16.dp),
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NervaBlueElectric.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = NervaCyanAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = title,
                                color = NervaWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Bandera actual: $currentFlag",
                                color = NervaCyanAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
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

                Text(
                    text = "Selecciona una bandera oficial o escribe tu propio emoji/código:",
                    color = NervaTextMuted,
                    fontSize = 11.sp
                )

                // Common flags grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(commonFlags) { item ->
                        val isSelected = item.flag == currentFlag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NervaBlueElectric.copy(alpha = 0.4f) else NervaSidebarDark)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) NervaCyanAccent else Color(0xFF1E3A70),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onSelectFlag(item.flag) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = item.flag, fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.countryName,
                                    color = if (isSelected) NervaWhite else NervaTextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Custom flag entry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = customFlagText,
                        onValueChange = { customFlagText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe emoji (ej: 🏆 o 🇨🇱)", fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NervaCyanAccent,
                            unfocusedBorderColor = Color(0xFF1E3A70),
                            focusedTextColor = NervaWhite,
                            unfocusedTextColor = NervaWhite
                        )
                    )

                    Button(
                        onClick = {
                            if (customFlagText.isNotBlank()) {
                                onSelectFlag(customFlagText.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NervaBlueElectric),
                        shape = RoundedCornerShape(8.dp),
                        enabled = customFlagText.isNotBlank()
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
