package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.BlueVet
import com.example.ui.theme.BlueVetContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.PurpleOfficer
import com.example.ui.theme.PurpleOfficerContainer
import com.example.ui.util.AppStrings

data class BottomNavItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val isCenterAction: Boolean = false
)

@Composable
fun PashuSetuBottomBar(
    role: UserRole,
    currentTab: String,
    onTabSelected: (String) -> Unit,
    onCenterActionClick: () -> Unit,
    selectedLanguage: String = "हिंदी",
    unreadAlertCount: Int = 0,
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val items = when (role) {
        UserRole.FARMER -> listOf(
            BottomNavItem("home", AppStrings.t("nav_home", selectedLanguage), Icons.Default.Home),
            BottomNavItem("cattle", AppStrings.t("nav_cattle", selectedLanguage), Icons.Default.Pets),
            BottomNavItem("action", tr("AI जाँच", "AI Scan", "AI तपासणी", "AI તપાસ", "AI ਜਾਂਚ"), Icons.Default.LocalHospital, isCenterAction = true),
            BottomNavItem("alerts", AppStrings.t("nav_alerts", selectedLanguage), Icons.Default.Notifications),
            BottomNavItem("profile", AppStrings.t("nav_profile", selectedLanguage), Icons.Default.Person)
        )
        UserRole.VET -> listOf(
            BottomNavItem("home", AppStrings.t("nav_home", selectedLanguage), Icons.Default.Home),
            BottomNavItem("cases", AppStrings.t("nav_cases", selectedLanguage), Icons.Default.MedicalServices),
            BottomNavItem("action", tr("नया केस", "New Case", "नवीन केस", "નવો કેસ", "ਨਵਾਂ ਕੇਸ"), Icons.Default.Add, isCenterAction = true),
            BottomNavItem("medicines", AppStrings.t("nav_medicines", selectedLanguage), Icons.Default.Medication),
            BottomNavItem("profile", AppStrings.t("nav_profile", selectedLanguage), Icons.Default.Person)
        )
        UserRole.OFFICER -> listOf(
            BottomNavItem("dashboard", AppStrings.t("nav_dashboard", selectedLanguage), Icons.Default.Dashboard),
            BottomNavItem("reports", AppStrings.t("nav_reports", selectedLanguage), Icons.Default.Description),
            BottomNavItem("alerts", AppStrings.t("nav_alerts", selectedLanguage), Icons.Default.Notifications),
            BottomNavItem("more", AppStrings.t("nav_more", selectedLanguage), Icons.Default.MoreHoriz)
        )
    }

    val indicatorColor = when (role) {
        UserRole.FARMER -> GreenContainer
        UserRole.VET -> BlueVetContainer
        UserRole.OFFICER -> PurpleOfficerContainer
    }

    val activeColor = when (role) {
        UserRole.FARMER -> GreenDark
        UserRole.VET -> BlueVet
        UserRole.OFFICER -> PurpleOfficer
    }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            if (item.isCenterAction) {
                NavigationBarItem(
                    selected = false,
                    onClick = onCenterActionClick,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(activeColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = activeColor,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = indicatorColor
                    )
                )
            } else {
                val isSelected = currentTab == item.id
                val isAlertTab = item.id == "alerts"

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(item.id) },
                    icon = {
                        if (isAlertTab && unreadAlertCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Color(0xFFD32F2F),
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = if (unreadAlertCount > 9) "9+" else "$unreadAlertCount",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeColor,
                        selectedTextColor = activeColor,
                        unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = indicatorColor
                    )
                )
            }
        }
    }
}
