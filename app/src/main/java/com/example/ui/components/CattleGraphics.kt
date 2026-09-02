package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * High quality vector illustration of a Dairy Cow (Black & White Holstein / Indian cross)
 */
@Composable
fun CattleAvatar(
    animalType: String,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    status: String = "स्वस्थ"
) {
    val isBuffalo = animalType.contains("भैंस")
    val isCalf = animalType.contains("बछड़ा")
    val isBrownCow = animalType.contains("G003") || animalType.contains("गिर")

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = when {
                        isBuffalo -> listOf(Color(0xFF263238), Color(0xFF37474F))
                        isBrownCow -> listOf(Color(0xFFD7CCC8), Color(0xFFBCAAA4))
                        isCalf -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                        else -> listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0))
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (status == "बीमार") Color(0xFFE53935) else Color(0x33000000),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.85f)) {
            val w = this.size.width
            val h = this.size.height

            if (isBuffalo) {
                // Curled buffalo horns
                val leftHorn = Path().apply {
                    moveTo(w * 0.32f, h * 0.35f)
                    cubicTo(w * 0.15f, h * 0.20f, w * 0.10f, h * 0.40f, w * 0.20f, h * 0.50f)
                }
                val rightHorn = Path().apply {
                    moveTo(w * 0.68f, h * 0.35f)
                    cubicTo(w * 0.85f, h * 0.20f, w * 0.90f, h * 0.40f, w * 0.80f, h * 0.50f)
                }
                drawPath(leftHorn, Color(0xFF212121), style = Stroke(width = w * 0.08f))
                drawPath(rightHorn, Color(0xFF212121), style = Stroke(width = w * 0.08f))

                // Buffalo head (dark slate)
                drawRoundRect(
                    color = Color(0xFF1E272C),
                    topLeft = Offset(w * 0.24f, h * 0.28f),
                    size = Size(w * 0.52f, h * 0.58f),
                    cornerRadius = CornerRadius(w * 0.18f, h * 0.22f)
                )

                // Snout (lighter grey)
                drawRoundRect(
                    color = Color(0xFF37474F),
                    topLeft = Offset(w * 0.30f, h * 0.60f),
                    size = Size(w * 0.40f, h * 0.24f),
                    cornerRadius = CornerRadius(w * 0.12f, h * 0.12f)
                )

                // Nostrils
                drawCircle(Color(0xFF10171A), radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.72f))
                drawCircle(Color(0xFF10171A), radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.72f))

                // Eyes
                drawCircle(Color(0xFFCFD8DC), radius = w * 0.045f, center = Offset(w * 0.35f, h * 0.42f))
                drawCircle(Color(0xFFCFD8DC), radius = w * 0.045f, center = Offset(w * 0.65f, h * 0.42f))
                drawCircle(Color.Black, radius = w * 0.025f, center = Offset(w * 0.35f, h * 0.42f))
                drawCircle(Color.Black, radius = w * 0.025f, center = Offset(w * 0.65f, h * 0.42f))
            } else if (isBrownCow) {
                // Gir/Jersey Cow (Warm Reddish Brown)
                // Horns
                val hornPath = Path().apply {
                    moveTo(w * 0.35f, h * 0.30f)
                    cubicTo(w * 0.28f, h * 0.12f, w * 0.20f, h * 0.22f, w * 0.25f, h * 0.28f)
                    moveTo(w * 0.65f, h * 0.30f)
                    cubicTo(w * 0.72f, h * 0.12f, w * 0.80f, h * 0.22f, w * 0.75f, h * 0.28f)
                }
                drawPath(hornPath, Color(0xFF4E342E), style = Stroke(width = w * 0.07f))

                // Drooping ears
                val leftEar = Path().apply {
                    moveTo(w * 0.25f, h * 0.38f)
                    quadraticTo(w * 0.05f, h * 0.45f, w * 0.15f, h * 0.62f)
                }
                val rightEar = Path().apply {
                    moveTo(w * 0.75f, h * 0.38f)
                    quadraticTo(w * 0.95f, h * 0.45f, w * 0.85f, h * 0.62f)
                }
                drawPath(leftEar, Color(0xFF8D6E63), style = Stroke(width = w * 0.08f))
                drawPath(rightEar, Color(0xFF8D6E63), style = Stroke(width = w * 0.08f))

                // Head
                drawRoundRect(
                    color = Color(0xFFA15D38),
                    topLeft = Offset(w * 0.25f, h * 0.25f),
                    size = Size(w * 0.50f, h * 0.60f),
                    cornerRadius = CornerRadius(w * 0.16f, h * 0.20f)
                )

                // White patch on forehead
                drawOval(
                    color = Color(0xFFFFF3E0),
                    topLeft = Offset(w * 0.42f, h * 0.30f),
                    size = Size(w * 0.16f, h * 0.18f)
                )

                // Muzzle (pinkish brown)
                drawRoundRect(
                    color = Color(0xFFD7CCC8),
                    topLeft = Offset(w * 0.32f, h * 0.62f),
                    size = Size(w * 0.36f, h * 0.22f),
                    cornerRadius = CornerRadius(w * 0.10f, h * 0.10f)
                )

                // Nostrils
                drawCircle(Color(0xFF4E342E), radius = w * 0.035f, center = Offset(w * 0.43f, h * 0.73f))
                drawCircle(Color(0xFF4E342E), radius = w * 0.035f, center = Offset(w * 0.57f, h * 0.73f))

                // Eyes
                drawCircle(Color(0xFF3E2723), radius = w * 0.04f, center = Offset(w * 0.36f, h * 0.45f))
                drawCircle(Color(0xFF3E2723), radius = w * 0.04f, center = Offset(w * 0.64f, h * 0.45f))
            } else {
                // Classic Holstein / White cow / Calf
                val hornPath = Path().apply {
                    moveTo(w * 0.34f, h * 0.28f)
                    lineTo(w * 0.24f, h * 0.16f)
                    moveTo(w * 0.66f, h * 0.28f)
                    lineTo(w * 0.76f, h * 0.16f)
                }
                drawPath(hornPath, Color(0xFFB0BEC5), style = Stroke(width = w * 0.06f))

                // Ears
                val earL = Path().apply {
                    moveTo(w * 0.26f, h * 0.34f)
                    lineTo(w * 0.10f, h * 0.38f)
                    lineTo(w * 0.22f, h * 0.46f)
                }
                val earR = Path().apply {
                    moveTo(w * 0.74f, h * 0.34f)
                    lineTo(w * 0.90f, h * 0.38f)
                    lineTo(w * 0.78f, h * 0.46f)
                }
                drawPath(earL, Color(0xFF263238))
                drawPath(earR, Color(0xFF263238))

                // Head (White)
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.24f, h * 0.25f),
                    size = Size(w * 0.52f, h * 0.60f),
                    cornerRadius = CornerRadius(w * 0.18f, h * 0.20f)
                )

                // Black Holstein patches
                val patch1 = Path().apply {
                    moveTo(w * 0.24f, h * 0.25f)
                    lineTo(w * 0.45f, h * 0.25f)
                    quadraticTo(w * 0.42f, h * 0.45f, w * 0.24f, h * 0.48f)
                    close()
                }
                val patch2 = Path().apply {
                    moveTo(w * 0.60f, h * 0.32f)
                    quadraticTo(w * 0.76f, h * 0.30f, w * 0.76f, h * 0.50f)
                    lineTo(w * 0.62f, h * 0.48f)
                    close()
                }
                drawPath(patch1, Color(0xFF212121))
                drawPath(patch2, Color(0xFF212121))

                // Pink muzzle
                drawRoundRect(
                    color = Color(0xFFFFCDD2),
                    topLeft = Offset(w * 0.30f, h * 0.62f),
                    size = Size(w * 0.40f, h * 0.22f),
                    cornerRadius = CornerRadius(w * 0.10f, h * 0.10f)
                )

                // Nostrils
                drawCircle(Color(0xFFB71C1C), radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.73f))
                drawCircle(Color(0xFFB71C1C), radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.73f))

                // Big friendly eyes
                drawCircle(Color(0xFF212121), radius = w * 0.045f, center = Offset(w * 0.36f, h * 0.44f))
                drawCircle(Color(0xFF212121), radius = w * 0.045f, center = Offset(w * 0.64f, h * 0.44f))
                drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.37f, h * 0.43f))
                drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.65f, h * 0.43f))
            }
        }
    }
}

/**
 * Rich Role Card Avatar (Farmer, Vet Doctor, District Officer)
 */
@Composable
fun RoleAvatar(
    roleTitle: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val isFarmer = roleTitle.contains("किसान")
    val isVet = roleTitle.contains("चिकित्सक")
    val isOfficer = roleTitle.contains("अधिकारी")

    val bgGradient = when {
        isFarmer -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        isVet -> listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
        else -> listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(bgGradient)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.85f)) {
            val w = this.size.width
            val h = this.size.height

            if (isFarmer) {
                // Farmer with pagri (turban) and kurta
                // Pagri (White / Saffron Turban folds)
                val turban = Path().apply {
                    moveTo(w * 0.22f, h * 0.32f)
                    cubicTo(w * 0.20f, h * 0.12f, w * 0.80f, h * 0.10f, w * 0.78f, h * 0.32f)
                    cubicTo(w * 0.65f, h * 0.36f, w * 0.35f, h * 0.36f, w * 0.22f, h * 0.32f)
                }
                drawPath(turban, Color(0xFFFFF8E1))
                drawPath(turban, Color(0xFFFFB74D), style = Stroke(width = 2.dp.toPx()))

                // Face
                drawRoundRect(
                    color = Color(0xFFD7A779),
                    topLeft = Offset(w * 0.32f, h * 0.30f),
                    size = Size(w * 0.36f, h * 0.36f),
                    cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
                )

                // Mustache
                val stache = Path().apply {
                    moveTo(w * 0.40f, h * 0.52f)
                    quadraticTo(w * 0.50f, h * 0.50f, w * 0.60f, h * 0.52f)
                    quadraticTo(w * 0.50f, h * 0.56f, w * 0.40f, h * 0.52f)
                }
                drawPath(stache, Color(0xFF3E2723))

                // Smile
                val smile = Path().apply {
                    moveTo(w * 0.43f, h * 0.57f)
                    quadraticTo(w * 0.50f, h * 0.62f, w * 0.57f, h * 0.57f)
                }
                drawPath(smile, Color(0xFF3E2723), style = Stroke(width = 2.dp.toPx()))

                // Eyes
                drawCircle(Color(0xFF3E2723), radius = w * 0.035f, center = Offset(w * 0.41f, h * 0.42f))
                drawCircle(Color(0xFF3E2723), radius = w * 0.035f, center = Offset(w * 0.59f, h * 0.42f))

                // White Kurta & Gamcha (cloth on shoulder)
                drawRoundRect(
                    color = Color(0xFFF5F5F5),
                    topLeft = Offset(w * 0.15f, h * 0.66f),
                    size = Size(w * 0.70f, h * 0.34f),
                    cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
                )
                // Red/green gamcha fold
                drawRoundRect(
                    color = Color(0xFF2E7D32),
                    topLeft = Offset(w * 0.18f, h * 0.68f),
                    size = Size(w * 0.18f, h * 0.32f),
                    cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
                )
            } else if (isVet) {
                // Veterinarian Doctor with Stethoscope & Lab coat
                // Doctor Face
                drawRoundRect(
                    color = Color(0xFFE0BB95),
                    topLeft = Offset(w * 0.32f, h * 0.24f),
                    size = Size(w * 0.36f, h * 0.36f),
                    cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
                )
                // Hair
                drawRoundRect(
                    color = Color(0xFF212121),
                    topLeft = Offset(w * 0.30f, h * 0.16f),
                    size = Size(w * 0.40f, h * 0.16f),
                    cornerRadius = CornerRadius(w * 0.10f, w * 0.10f)
                )
                // Eyes & Smile
                drawCircle(Color(0xFF212121), radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.36f))
                drawCircle(Color(0xFF212121), radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.36f))
                val smile = Path().apply {
                    moveTo(w * 0.44f, h * 0.48f)
                    quadraticTo(w * 0.50f, h * 0.53f, w * 0.56f, h * 0.48f)
                }
                drawPath(smile, Color(0xFF3E2723), style = Stroke(width = 2.dp.toPx()))

                // White Lab Coat
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.15f, h * 0.60f),
                    size = Size(w * 0.70f, h * 0.40f),
                    cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
                )
                // Blue Shirt Collar
                drawRoundRect(
                    color = Color(0xFF0288D1),
                    topLeft = Offset(w * 0.38f, h * 0.60f),
                    size = Size(w * 0.24f, h * 0.16f)
                )

                // Stethoscope around neck
                val steth = Path().apply {
                    moveTo(w * 0.28f, h * 0.62f)
                    cubicTo(w * 0.30f, h * 0.85f, w * 0.70f, h * 0.85f, w * 0.72f, h * 0.62f)
                }
                drawPath(steth, Color(0xFF0D47A1), style = Stroke(width = 3.dp.toPx()))
                // Stethoscope chest piece
                drawCircle(Color(0xFF90A4AE), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.84f))
            } else {
                // District Officer (Spectacles, formal shirt, desk badge)
                drawRoundRect(
                    color = Color(0xFFD7A779),
                    topLeft = Offset(w * 0.32f, h * 0.24f),
                    size = Size(w * 0.36f, h * 0.36f),
                    cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
                )
                // Hair
                drawRoundRect(
                    color = Color(0xFF263238),
                    topLeft = Offset(w * 0.30f, h * 0.16f),
                    size = Size(w * 0.40f, h * 0.16f),
                    cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
                )
                // Spectacles
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(w * 0.36f, h * 0.34f),
                    size = Size(w * 0.12f, h * 0.09f),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
                drawRect(
                    color = Color(0xFF263238),
                    topLeft = Offset(w * 0.36f, h * 0.34f),
                    size = Size(w * 0.12f, h * 0.09f),
                    style = Stroke(width = 1.8.dp.toPx())
                )
                drawRect(
                    color = Color(0xFF263238),
                    topLeft = Offset(w * 0.52f, h * 0.34f),
                    size = Size(w * 0.12f, h * 0.09f),
                    style = Stroke(width = 1.8.dp.toPx())
                )
                drawLine(
                    color = Color(0xFF263238),
                    start = Offset(w * 0.48f, h * 0.38f),
                    end = Offset(w * 0.52f, h * 0.38f),
                    strokeWidth = 2.dp.toPx()
                )

                // Formal Blue Shirt & Tie
                drawRoundRect(
                    color = Color(0xFF3F51B5),
                    topLeft = Offset(w * 0.15f, h * 0.60f),
                    size = Size(w * 0.70f, h * 0.40f),
                    cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
                )
                // Tie
                val tie = Path().apply {
                    moveTo(w * 0.47f, h * 0.62f)
                    lineTo(w * 0.53f, h * 0.62f)
                    lineTo(w * 0.55f, h * 0.88f)
                    lineTo(w * 0.50f, h * 0.96f)
                    lineTo(w * 0.45f, h * 0.88f)
                    close()
                }
                drawPath(tie, Color(0xFFC2185B))
            }
        }
    }
}

/**
 * PashuSetu Hero Banner Card Graphic for Welcome Screen
 */
@Composable
fun PashuSetuHeroGraphic(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFC8E6C9),
                        Color(0xFFA5D6A7)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // Sun in background
            drawCircle(
                color = Color(0xFFFFE082),
                radius = w * 0.22f,
                center = Offset(w * 0.75f, h * 0.30f)
            )

            // Green hills in background
            val hillBack = Path().apply {
                moveTo(0f, h * 0.65f)
                cubicTo(w * 0.3f, h * 0.45f, w * 0.7f, h * 0.55f, w, h * 0.50f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillBack, Color(0xFF81C784))

            val hillFront = Path().apply {
                moveTo(0f, h * 0.72f)
                cubicTo(w * 0.4f, h * 0.60f, w * 0.75f, h * 0.70f, w, h * 0.62f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillFront, Color(0xFF4CAF50))
        }

        // Overlay with Farmer and Cattle visual composition
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(width = 240.dp, height = 180.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Cattle avatar on right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                CattleAvatar(animalType = "गाय", size = 110.dp)
            }
            // Farmer on left
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                RoleAvatar(roleTitle = "किसान", size = 120.dp)
            }
        }
    }
}
