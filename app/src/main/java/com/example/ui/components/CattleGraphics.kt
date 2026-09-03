package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

/**
 * High quality realistic representation of Cattle (Gir Cow, Murrah Buffalo, Calf, Crossbred)
 */
@Composable
fun CattleAvatar(
    animalType: String,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    status: String = "स्वस्थ"
) {
    val isBuffalo = animalType.contains("भैंस") || animalType.contains("Murrah") || animalType.contains("Buffalo") || animalType.contains("म्हैस") || animalType.contains("ભેંસ") || animalType.contains("ਮੱਝ")
    val isCalf = animalType.contains("बछड़ा") || animalType.contains("Calf") || animalType.contains("वासरू") || animalType.contains("વાછરડું") || animalType.contains("ਵੱਛਾ")
    val isSick = status == "बीमार" || status.contains("Sick")

    val drawableRes = when {
        isBuffalo -> R.drawable.img_murrah_buffalo
        isCalf -> R.drawable.img_calf
        else -> R.drawable.img_gir_cow
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = when {
                        isBuffalo -> listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC))
                        isCalf -> listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8))
                        else -> listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
                    }
                )
            )
            .border(
                width = 1.5.dp,
                color = if (isSick) Color(0xFFE53935) else Color(0x33000000),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = animalType,
            modifier = Modifier
                .size(size * 0.94f)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
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
    val isFarmer = roleTitle.contains("किसान") || roleTitle.contains("Farmer") || roleTitle.contains("शेतकरी") || roleTitle.contains("ખેડૂત") || roleTitle.contains("ਕਿਸਾਨ")
    val isVet = roleTitle.contains("चिकित्सक") || roleTitle.contains("Vet") || roleTitle.contains("Doctor") || roleTitle.contains("वैद्य") || roleTitle.contains("ચિકિત્સક") || roleTitle.contains("ਡਾਕਟਰ")
    val isOfficer = roleTitle.contains("अधिकारी") || roleTitle.contains("Officer") || roleTitle.contains("અધિકારી") || roleTitle.contains("ਅਧਿਕਾਰੀ")

    val drawableRes = when {
        isFarmer -> R.drawable.img_farmer_avatar
        isVet -> R.drawable.img_vet_doctor
        else -> R.drawable.img_officer_avatar
    }

    val bgGradient = when {
        isFarmer -> listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))
        isVet -> listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
        else -> listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(bgGradient)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = roleTitle,
            modifier = Modifier
                .size(size * 0.94f)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
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
