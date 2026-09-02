package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Appointment
import com.example.ui.components.CattleAvatar
import com.example.ui.theme.BlueVet
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusSick

import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ui.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetDoctorHomeScreen(
    appointments: List<Appointment>,
    onOpenDrawer: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAppointmentClick: (Appointment) -> Unit,
    onViewAllAppointments: () -> Unit,
    selectedLanguage: String = "हिंदी",
    onLanguageChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }

    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar matching Screen 7
            TopAppBar(
                title = {
                    Text(
                        text = tr("पशु चिकित्सक होम", "Veterinary Doctor Home", "पशुवैद्यक मुख्यपृष्ठ", "પશુ ચિકિત્સક હોમ", "ਪਸ਼ੂ ਡਾਕਟਰ ਹੋਮ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFF1B241C)
                        )
                    }
                },
                actions = {
                    Box {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8F5E9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { langMenuExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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

                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = Color(0xFF1B241C)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Greeting
                item {
                    Text(
                        text = tr("नमस्ते डॉ. महेश 👋", "Hello Dr. Mahesh 👋", "नमस्कार डॉ. महेश 👋", "નમસ્તે ડૉ. મહેશ 👋", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ ਡਾ. ਮਹੇਸ਼ 👋"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Doctor KPI Metric Cards (3 items in row)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: 8 आज के अपॉइंटमेंट
                        VetKpiCard(
                            count = "8",
                            label = tr("आज के अपॉइंटमेंट", "Today's Appointments", "आजच्या भेटी", "આજના એપોઇન્ટમેન્ટ", "ਅੱਜ ਦੇ ਅਪੌਇੰਟਮੈਂਟ"),
                            countColor = BlueVet,
                            bgColor = Color(0xFFE1F5FE),
                            modifier = Modifier.weight(1f)
                        )

                        // Card 2: 28 कुल पशु विजिट
                        VetKpiCard(
                            count = "28",
                            label = tr("कुल पशु विजिट", "Total Visits", "एकूण पशू भेटी", "કુલ પશુ મુલાકાત", "ਕੁੱਲ ਪਸ਼ੂ ਵਿਜ਼ਿਟ"),
                            countColor = Color(0xFF2E3A2F),
                            bgColor = Color(0xFFEFF3EE),
                            modifier = Modifier.weight(1f)
                        )

                        // Card 3: 2 आपातकालीन
                        VetKpiCard(
                            count = "2",
                            label = tr("आपातकालीन", "Emergency", "तातडीचे / आणीबाणी", "કટોકટી / ઇમરજન્સી", "ਐਮਰਜੈਂਸੀ"),
                            countColor = StatusSick,
                            bgColor = Color(0xFFFFEBEE),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // "आज के अपॉइंटमेंट" Header
                item {
                    Text(
                        text = tr("आज के अपॉइंटमेंट", "Today's Appointments", "आजच्या भेटी", "આજના એપોઇન્ટમેન્ટ", "ਅੱਜ ਦੇ ਅਪੌਇੰਟਮੈਂਟ"),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Appointments list matching the screenshot
                items(appointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onClick = { onAppointmentClick(appointment) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // "सभी अपॉइंटमेंट देखें" Outlined Button
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = onViewAllAppointments,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark)
                    ) {
                        Text(
                            text = tr("सभी अपॉइंटमेंट देखें", "View All Appointments", "सर्व भेटी पहा", "બધા એપોઇન્ટમેન્ટ જુઓ", "ਸਾਰੇ ਅਪੌਇੰਟਮੈਂਟ ਵੇਖੋ"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun VetKpiCard(
    count: String,
    label: String,
    countColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = countColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )
        }
    }
}

@Composable
private fun AppointmentCard(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CattleAvatar(
                animalType = appointment.animalType,
                size = 56.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.cattleTag,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B241C)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = appointment.farmerName,
                    fontSize = 13.sp,
                    color = Color(0xFF616161)
                )
            }

            Text(
                text = appointment.timeSlot,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
        }
    }
}
