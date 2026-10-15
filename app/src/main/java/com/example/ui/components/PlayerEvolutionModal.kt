package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Player
import com.example.model.SeasonEvolutionPoint
import com.example.ui.theme.*
import kotlin.math.roundToInt

enum class ChartMetricType(val title: String, val unit: String, val color: Color) {
    MARKET_VALUE("Valor de Mercado", "€", NervaCyanAccent),
    FITNESS("Estado Físico / Resistencia", "%", Color(0xFF10B981)),
    RATING("Calificación por Partido", "pts", Color(0xFFFBBF24))
}

@Composable
fun PlayerEvolutionModal(
    player: Player,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val history = remember(player) { player.getResolvedHistory() }
    var selectedMetric by remember { mutableStateOf(ChartMetricType.MARKET_VALUE) }
    var selectedPointIndex by remember { mutableStateOf<Int?>(history.size - 1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, NervaCyanAccent.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .testTag("player_evolution_modal"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = NervaSidebarDarker)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header: Player identity, rating pill and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OverallBadge(overall = player.overall, size = 46)

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PositionBadge(position = player.position)
                                Text(
                                    text = "${player.nationality} ${player.name}",
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "#${player.number}",
                                    color = NervaCyanAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "${player.clubName} • ${player.age} años • ${player.specificPos}",
                                color = NervaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = NervaWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Divider(color = Color(0xFF1B3B70), thickness = 1.dp)

                // Key Summary Indicators Row (Value, Stamina, Morale, Wage)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatHighlightCard(
                        title = "VALOR ACTUAL",
                        value = player.formattedValue,
                        subtitle = "▲ +16% temporada",
                        color = NervaCyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatHighlightCard(
                        title = "ESTADO FÍSICO",
                        value = "${player.stamina}%",
                        subtitle = if (player.stamina >= 85) "En Forma" else "Cansado",
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    StatHighlightCard(
                        title = "MORAL",
                        value = player.morale,
                        subtitle = "Alta confianza",
                        color = Color(0xFFFBBF24),
                        modifier = Modifier.weight(1f)
                    )
                    StatHighlightCard(
                        title = "SALARIO",
                        value = player.formattedWage,
                        subtitle = "Contrato 2028",
                        color = Color(0xFF93C5FD),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Metric Selector Tabs (Value vs Fitness vs Rating)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MÉTRICA:",
                        color = NervaTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ChartMetricType.values().forEach { metric ->
                        val isSelected = selectedMetric == metric
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) metric.color.copy(alpha = 0.2f) else NervaCardDark)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) metric.color else Color(0xFF1B3B70),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedMetric = metric
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(metric.color)
                                )
                                Text(
                                    text = metric.title,
                                    color = if (isSelected) NervaWhite else NervaTextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // THE INTERACTIVE CHART CANVAS (Weight 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF07142E))
                        .border(1.dp, Color(0xFF1A3563), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    InteractiveEvolutionChart(
                        history = history,
                        metric = selectedMetric,
                        selectedIndex = selectedPointIndex,
                        onSelectPoint = { selectedPointIndex = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Selected Point Inspector Card
                val activePoint = selectedPointIndex?.let { history.getOrNull(it) } ?: history.last()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NervaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(selectedMetric.color.copy(alpha = 0.8f), NervaBlueElectric)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(selectedMetric.color)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "JORNADA ${activePoint.matchday}",
                                    color = NervaSidebarDarker,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Column {
                                Text(
                                    text = when (selectedMetric) {
                                        ChartMetricType.MARKET_VALUE -> "Valor: $ " + String.format("%,d", activePoint.marketValue).replace(",", ".")
                                        ChartMetricType.FITNESS -> "Condición Física: ${activePoint.stamina}%"
                                        ChartMetricType.RATING -> "Puntuación de Partido: ${activePoint.matchRating} ★"
                                    },
                                    color = NervaWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Nota: ${activePoint.note}",
                                    color = NervaTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Mini compare against initial matchday J1
                        val j1Val = history.first().marketValue
                        val diffVal = activePoint.marketValue - j1Val
                        val diffSign = if (diffVal >= 0) "+$ " else "-$ "
                        val diffColor = if (diffVal >= 0) Color(0xFF10B981) else Color(0xFFEF4444)

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "vs. Inicio de temporada",
                                color = NervaTextMuted,
                                fontSize = 9.sp
                            )
                            Text(
                                text = "$diffSign" + String.format("%,d", kotlin.math.abs(diffVal)).replace(",", "."),
                                color = diffColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatHighlightCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NervaCardDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1B3B70))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                color = NervaTextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                color = color,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                color = NervaTextMuted,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun InteractiveEvolutionChart(
    history: List<SeasonEvolutionPoint>,
    metric: ChartMetricType,
    selectedIndex: Int?,
    onSelectPoint: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

    BoxWithConstraints(modifier = modifier) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        // Extract values
        val values = history.map {
            when (metric) {
                ChartMetricType.MARKET_VALUE -> it.marketValue.toDouble()
                ChartMetricType.FITNESS -> it.stamina.toDouble()
                ChartMetricType.RATING -> it.matchRating.toDouble()
            }
        }

        val rawMin = values.minOrNull() ?: 0.0
        val rawMax = values.maxOrNull() ?: 1.0
        val paddingFactor = 0.15
        val range = if (rawMax == rawMin) 1.0 else (rawMax - rawMin)
        val minVal = (rawMin - range * paddingFactor).coerceAtLeast(0.0)
        val maxVal = rawMax + range * paddingFactor

        val chartPaddingLeft = 40.dp.value
        val chartPaddingRight = 20.dp.value
        val chartPaddingTop = 20.dp.value
        val chartPaddingBottom = 30.dp.value

        val plotW = (width - chartPaddingLeft - chartPaddingRight).coerceAtLeast(10f)
        val plotH = (height - chartPaddingTop - chartPaddingBottom).coerceAtLeast(10f)

        // Calculate positions
        val stepX = plotW / (history.size - 1).coerceAtLeast(1)
        val points = values.mapIndexed { index, v ->
            val px = chartPaddingLeft + index * stepX
            val normalized = ((v - minVal) / (maxVal - minVal)).coerceIn(0.0, 1.0)
            val py = chartPaddingTop + (1.0 - normalized).toFloat() * plotH
            Offset(px, py)
        }

        // Tap gestures to pick nearest point
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(history, metric) {
                    detectTapGestures { tapOffset ->
                        val clickedIdx = points.indices.minByOrNull { i ->
                            kotlin.math.abs(points[i].x - tapOffset.x)
                        }
                        if (clickedIdx != null) {
                            onSelectPoint(clickedIdx)
                        }
                    }
                }
        ) {
            val gridColor = Color(0xFF162D55)
            val strokeOne = 1.dp.toPx()

            // 1. Draw 4 horizontal grid lines
            val gridLines = 4
            for (i in 0..gridLines) {
                val gy = chartPaddingTop + (plotH / gridLines) * i
                drawLine(
                    color = gridColor,
                    start = Offset(chartPaddingLeft, gy),
                    end = Offset(chartPaddingLeft + plotW, gy),
                    strokeWidth = strokeOne
                )
            }

            // 2. Draw Area fill under curve
            val path = Path()
            val fillPath = Path()

            points.forEachIndexed { i, p ->
                if (i == 0) {
                    path.moveTo(p.x, p.y)
                    fillPath.moveTo(p.x, chartPaddingTop + plotH)
                    fillPath.lineTo(p.x, p.y)
                } else {
                    val prev = points[i - 1]
                    val cx = (prev.x + p.x) / 2f
                    path.cubicTo(cx, prev.y, cx, p.y, p.x, p.y)
                    fillPath.cubicTo(cx, prev.y, cx, p.y, p.x, p.y)
                }
            }
            fillPath.lineTo(points.last().x, chartPaddingTop + plotH)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(metric.color.copy(alpha = 0.35f), Color.Transparent),
                    startY = chartPaddingTop,
                    endY = chartPaddingTop + plotH
                )
            )

            // 3. Draw main curve line
            drawPath(
                path = path,
                color = metric.color,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 4. Draw Point circles
            points.forEachIndexed { index, p ->
                val isSelected = selectedIndex == index
                if (isSelected) {
                    // Vertical guide line
                    drawLine(
                        color = metric.color.copy(alpha = 0.5f),
                        start = Offset(p.x, chartPaddingTop),
                        end = Offset(p.x, chartPaddingTop + plotH),
                        strokeWidth = 1.5.dp.toPx()
                    )

                    // Outer pulse halo
                    drawCircle(
                        color = metric.color.copy(alpha = 0.35f),
                        radius = 11.dp.toPx(),
                        center = p
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6.dp.toPx(),
                        center = p
                    )
                    drawCircle(
                        color = metric.color,
                        radius = 4.dp.toPx(),
                        center = p
                    )
                } else {
                    drawCircle(
                        color = Color(0xFF07142E),
                        radius = 4.dp.toPx(),
                        center = p
                    )
                    drawCircle(
                        color = metric.color,
                        radius = 3.dp.toPx(),
                        center = p,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }

        // Matchday Labels along X Axis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            history.forEachIndexed { i, pt ->
                val isSelected = selectedIndex == i
                Text(
                    text = pt.label,
                    color = if (isSelected) metric.color else NervaTextMuted,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                    modifier = Modifier.clickable { onSelectPoint(i) }
                )
            }
        }
    }
}
