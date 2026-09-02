package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.components.RoleAvatar
import com.example.ui.theme.BlueVet
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.PurpleOfficer

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.example.ui.util.AppStrings

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    selectedLanguage: String = "हिंदी",
    onLanguageChange: (String) -> Unit = {},
    isHindi: Boolean = selectedLanguage == "हिंदी",
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFAFBF9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language Selector Pill in header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE8F5E9),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable { langMenuExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = GreenDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedLanguage,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = langMenuExpanded,
                        onDismissRequest = { langMenuExpanded = false }
                    ) {
                        AppStrings.SUPPORTED_LANGUAGES.forEach { lang ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = lang,
                                        fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                                        color = if (lang == selectedLanguage) GreenDark else Color.Black
                                    )
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AppStrings.t("role_select_title", selectedLanguage),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2721),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppStrings.t("role_select_subtitle", selectedLanguage),
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role Card 1: किसान (Farmer)
            RoleSelectionCard(
                roleTitle = AppStrings.t("role_farmer_full", selectedLanguage),
                description = AppStrings.t("farmer_desc", selectedLanguage),
                accentColor = GreenDark,
                onClick = { onRoleSelected(UserRole.FARMER) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Role Card 2: पशु चिकित्सक (Vet)
            RoleSelectionCard(
                roleTitle = AppStrings.t("role_vet_full", selectedLanguage),
                description = AppStrings.t("vet_desc", selectedLanguage),
                accentColor = BlueVet,
                onClick = { onRoleSelected(UserRole.VET) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Role Card 3: जिला अधिकारी (District Officer)
            RoleSelectionCard(
                roleTitle = AppStrings.t("role_officer_full", selectedLanguage),
                description = AppStrings.t("officer_desc", selectedLanguage),
                accentColor = PurpleOfficer,
                onClick = { onRoleSelected(UserRole.OFFICER) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RoleSelectionCard(
    roleTitle: String,
    description: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            RoleAvatar(
                roleTitle = roleTitle,
                size = 76.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texts
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = roleTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E2721)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF616161)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Circular Forward Arrow Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Select role",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
