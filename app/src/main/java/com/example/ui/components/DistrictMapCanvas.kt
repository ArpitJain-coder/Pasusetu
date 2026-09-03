package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DistrictSummary

/**
 * Interactive district outbreak health map matching Screen 9 (जिला अधिकारी डैशबोर्ड)
 */
@Composable
fun DistrictHealthMap(
    summary: DistrictSummary,
    modifier: Modifier = Modifier,
    onZoneClick: (String, Int) -> Unit = { _, _ -> }
) {
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var selectedZoneName by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F6F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Canvas rendering district shape and polygon heat zones
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                val w = size.width
                val h = size.height

                // District boundary polygon (Jaipur district contours)
                val districtBoundary = Path().apply {
                    moveTo(w * 0.40f, h * 0.10f)
                    lineTo(w * 0.70f, h * 0.12f)
                    lineTo(w * 0.85f, h * 0.28f)
                    lineTo(w * 0.90f, h * 0.55f)
                    lineTo(w * 0.80f, h * 0.80f)
                    lineTo(w * 0.60f, h * 0.92f)
                    lineTo(w * 0.45f, h * 0.88f)
                    lineTo(w * 0.25f, h * 0.82f)
                    lineTo(w * 0.15f, h * 0.58f)
                    lineTo(w * 0.20f, h * 0.30f)
                    close()
                }

                // Base district fill
                drawPath(districtBoundary, Color(0xFFFFF8E1))
                drawPath(
                    districtBoundary,
                    Color(0xFFBCAAA4),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Sub-district division lines
                val internalLines = listOf(
                    Pair(Offset(w * 0.40f, h * 0.10f), Offset(w * 0.55f, h * 0.50f)),
                    Pair(Offset(w * 0.70f, h * 0.12f), Offset(w * 0.55f, h * 0.50f)),
                    Pair(Offset(w * 0.85f, h * 0.28f), Offset(w * 0.65f, h * 0.60f)),
                    Pair(Offset(w * 0.90f, h * 0.55f), Offset(w * 0.65f, h * 0.60f)),
                    Pair(Offset(w * 0.60f, h * 0.92f), Offset(w * 0.55f, h * 0.50f)),
                    Pair(Offset(w * 0.25f, h * 0.82f), Offset(w * 0.45f, h * 0.60f)),
                    Pair(Offset(w * 0.15f, h * 0.58f), Offset(w * 0.55f, h * 0.50f))
                )
                for ((start, end) in internalLines) {
                    drawLine(
                        color = Color(0xFFD7CCC8),
                        start = start,
                        end = end,
                        strokeWidth = 1.5.dp.toPx()
                    )
                }

                // Heat zone color polygons
                // Red Zone (North - Kotputli/Shahpura: 32 cases)
                val zoneNorth = Path().apply {
                    moveTo(w * 0.40f, h * 0.10f)
                    lineTo(w * 0.70f, h * 0.12f)
                    lineTo(w * 0.65f, h * 0.35f)
                    lineTo(w * 0.35f, h * 0.30f)
                    close()
                }
                drawPath(zoneNorth, Color(0xFFD32F2F).copy(alpha = 0.65f))

                // Orange Zone (Center - Sanganer/Chaksu: 18 cases)
                val zoneCenter = Path().apply {
                    moveTo(w * 0.35f, h * 0.30f)
                    lineTo(w * 0.65f, h * 0.35f)
                    lineTo(w * 0.62f, h * 0.58f)
                    lineTo(w * 0.32f, h * 0.55f)
                    close()
                }
                drawPath(zoneCenter, Color(0xFFFB8C00).copy(alpha = 0.65f))

                // Yellow Zone (West - Dudu: 12 cases)
                val zoneWest = Path().apply {
                    moveTo(w * 0.15f, h * 0.58f)
                    lineTo(w * 0.32f, h * 0.55f)
                    lineTo(w * 0.35f, h * 0.82f)
                    lineTo(w * 0.20f, h * 0.75f)
                    close()
                }
                drawPath(zoneWest, Color(0xFFFDD835).copy(alpha = 0.65f))

                // Green Zone (East - Bassi: 8 cases)
                val zoneEast = Path().apply {
                    moveTo(w * 0.65f, h * 0.35f)
                    lineTo(w * 0.90f, h * 0.55f)
                    lineTo(w * 0.78f, h * 0.75f)
                    lineTo(w * 0.62f, h * 0.58f)
                    close()
                }
                drawPath(zoneEast, Color(0xFF43A047).copy(alpha = 0.65f))

                // Deep Green Zone (South: 5 cases)
                val zoneSouth = Path().apply {
                    moveTo(w * 0.35f, h * 0.82f)
                    lineTo(w * 0.62f, h * 0.58f)
                    lineTo(w * 0.78f, h * 0.75f)
                    lineTo(w * 0.60f, h * 0.92f)
                    lineTo(w * 0.45f, h * 0.88f)
                    close()
                }
                drawPath(zoneSouth, Color(0xFF2E7D32).copy(alpha = 0.65f))
            }

            // Interactive Outbreak Count Badges overlay
            summary.zones.forEach { zone ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(
                            x = (zone.xPercent * 280).dp,
                            y = (zone.yPercent * 240).dp
                        )
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(zone.riskColor),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(38.dp)
                            .clickable {
                                selectedZoneName = zone.name
                                onZoneClick(zone.name, zone.cases)
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (zone.cases < 10) "0${zone.cases}" else "${zone.cases}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Map Controls (+ / - / layers) on top right
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = { zoomLevel = (zoomLevel + 0.2f).coerceAtMost(2.0f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom in",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                IconButton(
                    onClick = { zoomLevel = (zoomLevel - 0.2f).coerceAtLeast(0.8f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom out",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Layer icon button on bottom right
            IconButton(
                onClick = { /* Toggle layer */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(38.dp)
                    .shadow(3.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Layers",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Selected zone label tooltip if tapped
            selectedZoneName?.let { zoneName ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xDD000000)
                ) {
                    Text(
                        text = "क्षेत्र: $zoneName",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
