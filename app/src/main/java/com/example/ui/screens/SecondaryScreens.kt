package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Vaccines
import com.example.data.model.AlertRecord
import com.example.data.model.MedicineRecord
import com.example.data.model.UserRole
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import com.example.ui.components.RoleAvatar
import com.example.ui.theme.BorderLight
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusSick
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.appTextFieldColors

data class AlertItem(
    val title: String,
    val description: String,
    val time: String,
    val isUrgent: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    alertList: List<AlertRecord> = emptyList(),
    vaccineList: List<VaccineRecord> = emptyList(),
    selectedLanguage: String = "हिंदी",
    onOpenVaccineSchedule: () -> Unit = {},
    onMarkRead: (Long) -> Unit = {},
    onDeleteAlert: (AlertRecord) -> Unit = {},
    onBroadcastAlert: (String, String, String, String, Boolean) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    var showBroadcastDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newEngTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newEngDesc by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val contentModifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .align(Alignment.TopCenter)

            Column(modifier = contentModifier) {
                TopAppBar(
                    title = {
                        Text(
                            text = tr("अलर्ट एवं आधिकारिक सूचनाएं", "Alerts & Official Notices", "सूचना आणि अलर्ट", "ચેતવણીઓ અને સૂચનાઓ", "ਅਲਰਟ ਅਤੇ ਸੂਚਨਾਵਾਂ"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    actions = {
                        IconButton(onClick = { showBroadcastDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.AddAlert,
                                contentDescription = tr("अलर्ट जारी करें", "Broadcast Alert", "अलर्ट जारी करा", "ચેતવણી જારી કરો", "ਅਲਰਟ ਜਾਰੀ ਕਰੋ"),
                                tint = GreenDark
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )

                val dueVaccines = vaccineList.filter { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Dynamic Due Vaccination Alert Cards if due
                    if (dueVaccines.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenVaccineSchedule() },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    width = 1.5.dp,
                                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE65100))
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE65100)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Vaccines,
                                                contentDescription = "Vaccine Alert",
                                                tint = Color.White,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tr("🔔 अति-आवश्यक: टीकाकरण बाकी है!", "🔔 Urgent: Vaccination Due!", "🔔 अति-तातडीचे: लसीकरण बाकी!", "🔔 અતિ-જરૂરી: રસીકરણ બાકી!", "🔔 ਬਹੁਤ ਜ਼ਰੂਰੀ: ਟੀਕਾਕਰਨ ਬਾਕੀ!"),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFD84315)
                                            )
                                            Text(
                                                text = tr("${dueVaccines.size} टीके आपके पशुओं के लिए निर्धारित हैं।", "${dueVaccines.size} vaccines are due for your herd.", "तुमच्या पशूंसाठी ${dueVaccines.size} लस बाकी आहेत.", "તમારા પશુઓ માટે ${dueVaccines.size} રસી બાકી છે.", "ਤੁਹਾਡੇ ਪਸ਼ੂਆਂ ਲਈ ${dueVaccines.size} ਟੀਕੇ ਬਾਕੀ ਹਨ।"),
                                                fontSize = 12.sp,
                                                color = Color(0xFF5D4037)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    dueVaccines.forEach { vaccine ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = if (selectedLanguage == "English") vaccine.englishName else vaccine.vaccineName,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextPrimary
                                                    )
                                                    Text(
                                                        text = "${tr("नियत तिथि", "Due")}: ${vaccine.scheduledDate} • ${vaccine.targetAnimal}",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFFD84315)
                                                    )
                                                }

                                                Button(
                                                    onClick = onOpenVaccineSchedule,
                                                    shape = RoundedCornerShape(6.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(tr("शेड्यूल देखें", "Schedule"), fontSize = 11.sp, color = Color.White)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Alerts from Room Database
                    items(alertList) { alert ->
                        val isEng = selectedLanguage == "English"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (alert.isUrgent) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(if (alert.isUrgent) StatusSick else GreenDark),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (alert.isUrgent) Icons.Default.Warning else Icons.Default.NotificationsActive,
                                            contentDescription = "Alert",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isEng && alert.englishTitle.isNotBlank()) alert.englishTitle else alert.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = if (isEng && alert.englishDescription.isNotBlank()) alert.englishDescription else alert.description,
                                            fontSize = 13.sp,
                                            color = TextSecondary,
                                            lineHeight = 18.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${alert.timestamp} • ${alert.source}",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                if (!alert.isRead) {
                                                    IconButton(
                                                        onClick = { onMarkRead(alert.id) },
                                                        modifier = Modifier.size(28.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = tr("पढ़ा हुआ चिह्नित करें", "Mark read", "वाचले म्हणून चिन्हांकित करा", "વાંચેલ તરીકે ચિહ્નિત કરો", "ਪੜ੍ਹਿਆ ਗਿਆ ਨਿਸ਼ਾਨਬੱਧ ਕਰੋ"),
                                                            tint = GreenDark,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                                IconButton(
                                                    onClick = { onDeleteAlert(alert) },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.DeleteOutline,
                                                        contentDescription = tr("हटाएं", "Delete", "हटवा", "કાઢી નાખો", "ਮਿਟਾਓ"),
                                                        tint = TextSecondary,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }

        // Broadcast Alert Dialog
        if (showBroadcastDialog) {
            AlertDialog(
                onDismissRequest = { showBroadcastDialog = false },
                title = {
                    Text(
                        text = tr("आपातकालीन चेतावनी प्रसारित करें", "Broadcast Emergency Alert", "तातडीचा अलर्ट जारी करा", "કટોકટી ચેતવણી પ્રસારિત કરો", "ਐਮਰਜੈਂਸੀ ਅਲਰਟ ਜਾਰੀ ਕਰੋ"),
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text(tr("शीर्षक (हिंदी)", "Title (Hindi)")) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = appTextFieldColors()
                        )
                        OutlinedTextField(
                            value = newEngTitle,
                            onValueChange = { newEngTitle = it },
                            label = { Text(tr("शीर्षक (English)", "Title (English)")) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = appTextFieldColors()
                        )
                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text(tr("विवरण (हिंदी)", "Description (Hindi)")) },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth(),
                            colors = appTextFieldColors()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilterChip(
                                selected = isUrgent,
                                onClick = { isUrgent = !isUrgent },
                                label = { Text(tr("अति-आवश्यक (रेड अलर्ट)", "High Priority (Red Alert)")) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFFFCDD2),
                                    selectedLabelColor = Color(0xFFC62828)
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                onBroadcastAlert(
                                    newTitle,
                                    newEngTitle.ifBlank { newTitle },
                                    newDesc.ifBlank { "सावधानी बरतें और नजदीकी पशु केंद्र से संपर्क करें।" },
                                    newEngDesc.ifBlank { newDesc },
                                    isUrgent
                                )
                                showBroadcastDialog = false
                                newTitle = ""
                                newEngTitle = ""
                                newDesc = ""
                                newEngDesc = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(tr("प्रसारित करें", "Broadcast", "प्रसारित करा", "પ્રસારિત કરો", "ਪ੍ਰਸਾਰਿਤ ਕਰੋ"), color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBroadcastDialog = false }) {
                        Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"), color = TextSecondary)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentRole: UserRole,
    selectedLanguage: String,
    onSwitchRoleClick: () -> Unit,
    userProfile: com.example.ui.viewmodel.UserProfile? = null,
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val name = userProfile?.name?.takeIf { it.isNotBlank() } ?: when (currentRole) {
        UserRole.FARMER -> tr("राम किसान", "Ram Kisan", "राम शेतकरी", "રામ કિસાન", "ਰਾਮ ਕਿਸਾਨ")
        UserRole.VET -> tr("डॉ. महेश शर्मा (B.V.Sc & A.H.)", "Dr. Mahesh Sharma (B.V.Sc & A.H.)", "डॉ. महेश शर्मा (B.V.Sc & A.H.)", "ડૉ. મહેશ શર્મા (B.V.Sc & A.H.)", "ਡਾ. ਮਹੇਸ਼ ਸ਼ਰਮਾ (B.V.Sc & A.H.)")
        UserRole.OFFICER -> tr("संजय वर्मा (मुख्य पशु चिकित्सा अधिकारी)", "Sanjay Verma (Chief Veterinary Officer)", "संजय वर्मा (मुख्य पशुवैद्यकीय अधिकारी)", "સંજય વર્મા (મુખ્ય પશુ ચિકિત્સા અધિકારી)", "ਸੰਜੇ ਵਰਮਾ (ਮੁੱਖ ਵੈਟਰਨਰੀ ਅਫ਼ਸਰ)")
    }
    val subtitle = when (currentRole) {
        UserRole.FARMER -> "${userProfile?.address ?: tr("गाँव भाटी", "Bhati Village", "गाव भाटी", "ગામ ભાટી", "ਪਿੰਡ ਭਾਟੀ")}, ${userProfile?.district ?: tr("जयपुर", "Jaipur", "जयपूर", "જયપુર", "ਜੈਪੁਰ")} | ${tr("पंजीकृत डेयरी किसान", "Registered Dairy Farmer", "नोंदणीकृत दुग्ध उत्पादक", "નોંધાયેલ ડેરી ખેડૂત", "ਰਜਿਸਟਰਡ ਡੇਅਰੀ ਕਿਸਾਨ")}"
        UserRole.VET -> "${userProfile?.address ?: tr("राजकीय पशु चिकित्सालय", "Govt. Veterinary Hospital", "शासकीय पशू रुग्णालय", "સરકારી પશુ દવાખાનું", "ਸਰਕਾਰੀ ਪਸ਼ੂ ਹਸਪਤਾਲ")} | Reg: ${userProfile?.regOrDeptId ?: "RVC-2022-4102"}"
        UserRole.OFFICER -> "${userProfile?.address ?: tr("पशुपालन विभाग", "Dept. of Animal Husbandry", "पशुसंवर्धन विभाग", "પશુપાલન વિભાગ", "ਪਸ਼ੂ ਪਾਲਣ ਵਿਭਾਗ")}, ${userProfile?.district ?: tr("जयपुर", "Jaipur", "जयपूर", "જયપુર", "ਜੈਪੁਰ")}"
    }

    val roleTitle = when (currentRole) {
        UserRole.FARMER -> tr("किसान", "Farmer", "शेतकरी", "ખેડૂત", "ਕਿਸਾਨ")
        UserRole.VET -> tr("पशु चिकित्सक", "Veterinary Doctor", "पशुवैद्यक", "પશુ ચિકિત્સક", "ਪਸ਼ੂ ਡਾਕਟਰ")
        UserRole.OFFICER -> tr("जिला अधिकारी", "District Officer", "जिल्हा अधिकारी", "જિલ્લા અધિકારી", "ਜ਼ਿਲ੍ਹਾ ਅਧਿਕਾਰੀ")
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .align(Alignment.TopCenter)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = tr("प्रोफ़ाइल", "Profile", "प्रोफाइल", "પ્રોફાઇલ", "ਪ੍ਰੋਫਾਈਲ"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoleAvatar(
                        roleTitle = currentRole.titleHindi,
                        size = 90.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Switch Role Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = "Role",
                                    tint = GreenDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${tr("वर्तमान भूमिका", "Current Role", "सध्याची भूमिका", "વર્તમાન ભૂમિકા", "ਮੌਜੂਦਾ ਭੂਮਿਕਾ")}: $roleTitle",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = tr(
                                            "किसान, डॉक्टर या अधिकारी के रूप में देखें",
                                            "Switch between Farmer, Vet or Officer",
                                            "शेतकरी, डॉक्टर किंवा अधिकारी म्हणून पहा",
                                            "ખેડૂત, ડૉક્ટર અથવા અધિકારી તરીકે જુઓ",
                                            "ਕਿਸਾਨ, ਡਾਕਟਰ ਜਾਂ ਅਧਿਕਾਰੀ ਵਜੋਂ ਵੇਖੋ"
                                        ),
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = onSwitchRoleClick,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                                ) {
                                    Text(tr("भूमिका बदलें", "Switch Role", "भूमिका बदला", "ભૂમિકા બદલો", "ਭੂਮਿਕਾ ਬਦਲੋ"), fontSize = 13.sp, color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = onLogoutClick,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(tr("लॉगआउट", "Logout", "लॉगआउट", "લૉગઆઉટ", "ਲਾਗਆਉਟ"), fontSize = 13.sp, color = Color(0xFFD32F2F))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // App Info Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GreenDark)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(tr("PashuSetu v1.0 • डिजिटल भारत मिशन", "PashuSetu v1.0 • Digital India Mission", "PashuSetu v1.0 • डिजिटल भारत अभियान", "PashuSetu v1.0 • ડિજિટલ ભારત મિશન", "PashuSetu v1.0 • ਡਿਜੀਟਲ ਭਾਰਤ ਮਿਸ਼ਨ"), fontSize = 14.sp, color = TextPrimary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = GreenDark)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("${tr("चयनित भाषा", "Selected Language", "निवडलेली भाषा", "પસંદ કરેલ ભાષા", "ਚੁਣੀ ਹੋਈ ਭਾਸ਼ਾ")}: $selectedLanguage", fontSize = 14.sp, color = TextPrimary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = GreenDark)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(tr("हेल्पलाइन: 1962 (टोल फ्री 24x7)", "Helpline: 1962 (Toll Free 24x7)", "हेल्पलाइन: 1962 (टोल फ्री 24x7)", "હેલ્પલાઇન: 1962 (ટોલ ફ્રી 24x7)", "ਹੈਲਪਲਾਈਨ: 1962 (ਟੋਲ ਫ੍ਰੀ 24x7)"), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinesScreen(
    medicineList: List<MedicineRecord> = emptyList(),
    selectedLanguage: String = "हिंदी",
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String = en, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("सभी") }

    val categories = listOf("सभी", "एंटीबायोटिक", "दर्द निवारक", "टॉनिक", "एंटीसेप्टिक", "पाचक")

    // Filter list
    val filteredList = medicineList.filter { med ->
        val matchesCategory = selectedCategory == "सभी" || med.category.contains(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
            med.name.contains(searchQuery, ignoreCase = true) ||
            med.genericName.contains(searchQuery, ignoreCase = true) ||
            med.descriptionHindi.contains(searchQuery, ignoreCase = true) ||
            med.descriptionEnglish.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 720.dp)
                    .align(Alignment.TopCenter)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = tr("दवाइयाँ व सरकारी औषधालय", "Medicines & Pharmacy", "औषधे आणि औषधालय", "દવાઓ અને ઔષધાલય", "ਦਵਾਈਆਂ ਅਤੇ ਫਾਰਮੇਸੀ"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text(tr("दवा या लक्षण खोजें...", "Search medicine or symptom...", "औषध किंवा लक्षण शोधा...", "દવા અથવા લક્ષણ શોધો...", "ਦਵਾਈ ਜਾਂ ਲੱਛਣ ਖੋਜੋ...")) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = GreenDark) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = appTextFieldColors()
                )

                // Category Chips Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = GreenDark
                            )
                        )
                    }
                }

                // Medicines List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { med ->
                        val isEng = selectedLanguage == "English"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GreenContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Medication,
                                            contentDescription = null,
                                            tint = GreenDark
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(med.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(med.genericName, fontSize = 12.sp, color = TextSecondary)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (med.inStock) GreenContainer else Color(0xFFFFEBEE)
                                    ) {
                                        Text(
                                            text = if (med.inStock) tr("उपलब्ध", "In Stock", "उपलब्ध", "ઉપલબ્ધ", "ਉਪਲਬਧ") else tr("समाप्त", "Out of Stock", "संपले", "સમાપ્ત", "ਖ਼ਤਮ"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (med.inStock) GreenDark else Color(0xFFD32F2F),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (isEng && med.descriptionEnglish.isNotBlank()) med.descriptionEnglish else med.descriptionHindi,
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${tr("मात्रा", "Dosage")}: ${med.dosageInfo}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = med.price,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenDark
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
