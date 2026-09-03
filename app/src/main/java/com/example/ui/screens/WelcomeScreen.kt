package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CattleAvatar
import com.example.ui.components.PashuSetuHeroGraphic
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onMobileLoginClick: () -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }
    val languages = listOf("हिंदी", "English", "मराठी", "ગુજરાતી", "ਪੰਜਾਬੀ")

    // Localized strings according to selected language
    val tagline = when (selectedLanguage) {
        "English" -> "Animal Health, Farmer's Prosperity"
        "मराठी" -> "पशूंचे आरोग्य, बळीराजाची समृद्धी"
        "ગુજરાતી" -> "પશુઓનું સ્વાસ્થ્ય, ખેડૂતોની સમૃદ્ધિ"
        "ਪੰਜਾਬੀ" -> "ਪਸ਼ੂਆਂ ਦੀ ਸਿਹਤ, ਕਿਸਾਨਾਂ ਦੀ ਖੁਸ਼ਹਾਲੀ"
        else -> "पशुओं का स्वास्थ्य, किसानों की समृद्धि"
    }

    val loginText = when (selectedLanguage) {
        "English" -> "Login / Register"
        "मराठी" -> "लॉगिन / नोंदणी करा"
        "ગુજરાતી" -> "લૉગિન / નોંધણી કરો"
        "ਪੰਜਾਬੀ" -> "ਲਾਗਇਨ / ਰਜਿਸਟਰ ਕਰੋ"
        else -> "लॉगिन / रजिस्टर करें"
    }

    val mobileText = when (selectedLanguage) {
        "English" -> "Continue with Mobile Number"
        "मराठी" -> "मोबाईल क्रमांकाने सुरू ठेवा"
        "ગુજરાતી" -> "મોબાઇલ નંબરથી આગળ વધો"
        "ਪੰਜਾਬੀ" -> "ਮੋਬਾਈਲ ਨੰਬਰ ਨਾਲ ਜਾਰੀ ਰੱਖੋ"
        else -> "मोबाइल नंबर से जारी रखें"
    }

    val selectLanguageLabel = when (selectedLanguage) {
        "English" -> "Select Language"
        "मराठी" -> "भाषा निवडा"
        "ગુજરાતી" -> "ભાષા પસંદ કરો"
        "ਪੰਜਾਬੀ" -> "ਭਾਸ਼ਾ ਚੁਣੋ"
        else -> "भाषा चुनें"
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Quick Language Selector Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GreenContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { langMenuExpanded = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = GreenDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedLanguage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = GreenDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = langMenuExpanded,
                        onDismissRequest = { langMenuExpanded = false }
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = lang,
                                            fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                                            color = if (lang == selectedLanguage) GreenDark else TextPrimary
                                        )
                                        if (lang == selectedLanguage) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = GreenDark,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    onLanguageChange(lang)
                                    langMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Brand & Emblem
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFF3E0))
                            )
                        )
                        .border(2.dp, Color(0xFFC8E6C9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CattleAvatar(animalType = "गाय", size = 64.dp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // App Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pashu",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GreenPrimary
                    )
                    Text(
                        text = "Setu",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AmberSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Dynamic Tagline
                AnimatedContent(
                    targetState = tagline,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TaglineTransition"
                ) { text ->
                    Text(
                        text = text,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Center Hero Illustration Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                PashuSetuHeroGraphic(modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Login / Register Button
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenDark,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = loginText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Continue with Mobile Button
                OutlinedButton(
                    onClick = onMobileLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "Phone icon",
                        modifier = Modifier.size(20.dp),
                        tint = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mobileText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Language Selector Chips Row at bottom
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectLanguageLabel,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    languages.forEach { lang ->
                        val isSelected = lang == selectedLanguage
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) GreenDark else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable { onLanguageChange(lang) }
                        ) {
                            Text(
                                text = lang,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
