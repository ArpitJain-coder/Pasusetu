package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary

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
    modifier: Modifier = Modifier
) {
    val items = when (role) {
        UserRole.FARMER -> listOf(
            BottomNavItem("home", AppStrings.t("nav_home", selectedLanguage), Icons.Default.Home),
            BottomNavItem("cattle", AppStrings.t("nav_cattle", selectedLanguage), Icons.Default.Pets),
            BottomNavItem("action", "", Icons.Default.Add, isCenterAction = true),
            BottomNavItem("alerts", AppStrings.t("nav_alerts", selectedLanguage), Icons.Default.Notifications),
            BottomNavItem("profile", AppStrings.t("nav_profile", selectedLanguage), Icons.Default.Person)
        )
        UserRole.VET -> listOf(
            BottomNavItem("home", AppStrings.t("nav_home", selectedLanguage), Icons.Default.Home),
            BottomNavItem("cases", AppStrings.t("nav_cases", selectedLanguage), Icons.Default.MedicalServices),
            BottomNavItem("action", "", Icons.Default.Add, isCenterAction = true),
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

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                if (item.isCenterAction) {
                    // Elevated green circular FAB
                    Box(
                        modifier = Modifier
                            .offset(y = (-8).dp)
                            .size(52.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(GreenDark)
                            .clickable { onCenterActionClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "Action",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    val isSelected = currentTab == item.id
                    Column(
                        modifier = Modifier
                            .clickable { onTabSelected(item.id) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) GreenDark else Color(0xFF757575),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) GreenDark else Color(0xFF757575)
                        )
                    }
                }
            }
        }
    }
}
