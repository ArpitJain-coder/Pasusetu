package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusSick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineScheduleScreen(
    vaccinesList: List<VaccineRecord>,
    selectedLanguage: String = "हिंदी",
    onBackClick: () -> Unit,
    onMarkCompleted: (Long) -> Unit,
    onAddNewSchedule: (
        vaccineName: String,
        englishName: String,
        disease: String,
        targetAnimal: String,
        date: String,
        location: String,
        dosage: String
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilterIndex by remember { mutableStateOf(0) } // 0: All, 1: Due & Overdue, 2: Upcoming, 3: Completed
    var showAddDialog by remember { mutableStateOf(false) }

    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val dueCount = vaccinesList.count { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
    val upcomingCount = vaccinesList.count { it.status == VaccineStatus.UPCOMING }
    val completedCount = vaccinesList.count { it.status == VaccineStatus.COMPLETED }

    val filteredVaccines = when (selectedFilterIndex) {
        1 -> vaccinesList.filter { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
        2 -> vaccinesList.filter { it.status == VaccineStatus.UPCOMING }
        3 -> vaccinesList.filter { it.status == VaccineStatus.COMPLETED }
        else -> vaccinesList
    }

    val dueVaccinesWithAlerts = vaccinesList.filter {
        (it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE) && it.isAlertActive
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = tr("पशु टीकाकरण शेड्यूल", "Vaccination Schedule", "पशू लसीकरण वेळापत्रक", "પશુ રસીકરણ સમયપત્રક", "ਪਸ਼ੂ ਟੀਕਾਕਰਨ ਸ਼ਡਿਊਲ"),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = tr("वापस", "Back", "मागे", "પાછા", "ਵਾਪਸ"),
                            tint = Color(0xFF1B241C)
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tr("+ टीका जोड़ें", "+ Add Vaccine", "+ लस जोडा", "+ રસી ઉમેરો", "+ ਟੀਕਾ ਜੋੜੋ"), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Alert Banner for Due Vaccines
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (dueVaccinesWithAlerts.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            border = CardDefaults.outlinedCardBorder().copy(width = 1.5.dp, brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF9800)))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE65100)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = "Alert",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tr("🔔 टीकाकरण अलर्ट: $dueCount टीके बाकी हैं!", "🔔 Vaccination Alert: $dueCount Vaccines Due!", "🔔 लसीकरण अलर्ट: $dueCount लस बाकी!", "🔔 રસીકરણ એલર્ટ: $dueCount રસી બાકી!", "🔔 ਟੀਕਾਕਰਨ ਅਲਰਟ: $dueCount ਟੀਕੇ ਬਾਕੀ!"),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE65100)
                                        )
                                        Text(
                                            text = tr("संक्रमण से बचाव के लिए नियत समय पर टीकाकरण अवश्य करवाएं।", "Vaccinate on schedule to prevent infectious outbreaks.", "संसर्गापासून संरक्षणासाठी वेळेवर लसीकरण करून घ्या.", "ચેપથી રક્ષણ માટે સમયસર રસીકરણ કરાવો.", "ਲਾਗ ਤੋਂ ਬਚਾਅ ਲਈ ਸਮੇਂ ਸਿਰ ਟੀਕਾਕਰਨ ਕਰਵਾਓ।"),
                                            fontSize = 12.sp,
                                            color = Color(0xFF5D4037)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // List Due items in the alert
                                dueVaccinesWithAlerts.forEach { due ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = if (selectedLanguage == "English") due.englishName else due.vaccineName,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF1B241C)
                                                )
                                                Text(
                                                    text = "${tr("नियत तिथि", "Due Date")}: ${due.scheduledDate} • ${due.locationCenter}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFFD84315),
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    onMarkCompleted(due.id)
                                                    Toast.makeText(context, tr("टीकाकरण पूर्ण चिह्नित किया गया", "Marked as Completed"), Toast.LENGTH_SHORT).show()
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(tr("टीका लग गया ✓", "Done ✓", "लस टोचली ✓", "રસી મુકાઈ ગઈ ✓", "ਟੀਕਾ ਲੱਗ ਗਿਆ ✓"), fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // National Vaccination Schedule Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GreenDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Vaccines, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = tr("राष्ट्रीय पशु रोग नियंत्रण कार्यक्रम (NADCP)", "National Animal Disease Control Programme", "राष्ट्रीय पशुरोग नियंत्रण कार्यक्रम", "રાષ્ટ્રીય પશુ રોગ નિયંત્રણ કાર્યક્રમ", "ਰਾਸ਼ਟਰੀ ਪਸ਼ੂ ਰੋਗ ਨਿਯੰਤਰਣ ਪ੍ਰੋਗਰਾਮ"),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                                Text(
                                    text = tr("FMD व ब्रूसेलोसिस का सरकारी टीकाकरण 100% निःशुल्क है।", "100% Free Govt. vaccination for FMD & Brucellosis.", "FMD व ब्रुसेलोसिसचे शासकीय लसीकरण १००% मोफत आहे.", "FMD અને બ્રુસેલોસિસનું સરકારી રસીકરણ ૧૦૦% મફત છે.", "FMD ਅਤੇ ਬਰੂਸੇਲੋਸਿਸ ਦਾ ਸਰਕਾਰੀ ਟੀਕਾਕਰਨ 100% ਮੁਫ਼ਤ ਹੈ।"),
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }
                }

                // Filter Tabs (All / Due / Upcoming / Done)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedFilterIndex == 0,
                            onClick = { selectedFilterIndex = 0 },
                            label = { Text(tr("सभी (${vaccinesList.size})", "All (${vaccinesList.size})")) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenDark,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = selectedFilterIndex == 1,
                            onClick = { selectedFilterIndex = 1 },
                            label = { Text(tr("बाकी ($dueCount)", "Due ($dueCount)")) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE65100),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = selectedFilterIndex == 2,
                            onClick = { selectedFilterIndex = 2 },
                            label = { Text(tr("आगामी ($upcomingCount)", "Upcoming ($upcomingCount)")) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0277BD),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = selectedFilterIndex == 3,
                            onClick = { selectedFilterIndex = 3 },
                            label = { Text(tr("पूर्ण ($completedCount)", "Done ($completedCount)")) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2E7D32),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // List of Vaccines
                items(filteredVaccines) { vaccine ->
                    VaccineCardItem(
                        vaccine = vaccine,
                        selectedLanguage = selectedLanguage,
                        onMarkCompleted = { onMarkCompleted(vaccine.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // Add Custom Vaccine Dialog
        if (showAddDialog) {
            AddVaccineDialog(
                selectedLanguage = selectedLanguage,
                onDismiss = { showAddDialog = false },
                onAdd = { name, enName, disease, animal, date, loc, dosage ->
                    onAddNewSchedule(name, enName, disease, animal, date, loc, dosage)
                    showAddDialog = false
                    Toast.makeText(context, tr("टीकाकरण सफलतापूर्वक जोड़ा गया!", "Vaccine schedule added successfully!"), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun VaccineCardItem(
    vaccine: VaccineRecord,
    selectedLanguage: String,
    onMarkCompleted: () -> Unit
) {
    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val isDue = vaccine.status == VaccineStatus.DUE || vaccine.status == VaccineStatus.OVERDUE
    val isCompleted = vaccine.status == VaccineStatus.COMPLETED

    val (badgeBg, badgeTextColor, badgeLabel) = when (vaccine.status) {
        VaccineStatus.DUE -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), tr("टीकाकरण बाकी (Due)", "Due Now", "लसीकरण बाकी", "રસીકરણ બાકી", "ਟੀਕਾਕਰਨ ਬਾਕੀ"))
        VaccineStatus.OVERDUE -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), tr("अति-आवश्यक (Overdue)", "Overdue", "अति-तातडीचे", "અતિ-જરૂરી", "ਬਹੁਤ ਜ਼ਰੂਰੀ"))
        VaccineStatus.UPCOMING -> Triple(Color(0xFFE1F5FE), Color(0xFF0277BD), tr("आगामी (Upcoming)", "Upcoming", "आगामी", "આગામી", "ਅਗਲਾ"))
        VaccineStatus.COMPLETED -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), tr("सम्पन्न (Done)", "Completed", "पूर्ण", "પૂર્ણ", "ਪੂਰਾ ਹੋਇਆ"))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Name + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (selectedLanguage == "English") vaccine.englishName else vaccine.vaccineName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                    Text(
                        text = vaccine.targetDisease,
                        fontSize = 12.sp,
                        color = Color(0xFF616161)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GreenDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${tr("निर्धारित तिथि", "Scheduled Date")}: ${vaccine.scheduledDate}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDue) Color(0xFFD84315) else Color(0xFF37474F)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${tr("लक्षित पशु", "Target Stock")}: ${vaccine.targetAnimal} (${vaccine.batchOrCattleTag})",
                    fontSize = 12.sp,
                    color = Color(0xFF424242)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = vaccine.locationCenter,
                    fontSize = 12.sp,
                    color = Color(0xFF616161)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF757575), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${tr("मात्रा", "Dosage")}: ${vaccine.dosage} • ${vaccine.intervalOrFrequency}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }

            // Alert Message Box if active
            if (vaccine.isAlertActive && isDue) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF8E1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedLanguage == "English" && vaccine.alertMessageEnglish.isNotBlank()) vaccine.alertMessageEnglish else vaccine.alertMessageHindi,
                            fontSize = 11.sp,
                            color = Color(0xFF795548),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Action Button
            if (!isCompleted) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMarkCompleted,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr("टीका लग चुका है (मार्क करें)", "Mark Vaccine as Completed", "लस टोचून झाली (मार्क करा)", "રસી મુકાઈ ગઈ (ચિહ્નિત કરો)", "ਟੀਕਾ ਲੱਗ ਚੁੱਕਾ ਹੈ (ਮਾਰਕ ਕਰੋ)"),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AddVaccineDialog(
    selectedLanguage: String,
    onDismiss: () -> Unit,
    onAdd: (
        name: String,
        englishName: String,
        disease: String,
        targetAnimal: String,
        date: String,
        location: String,
        dosage: String
    ) -> Unit
) {
    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    var name by remember { mutableStateOf("एंथ्रेक्स (Anthrax) वैक्सीन") }
    var enName by remember { mutableStateOf("Anthrax Spore Vaccine") }
    var disease by remember { mutableStateOf("एंथ्रेक्स संक्रामक जीवाणु रोग") }
    var targetAnimal by remember { mutableStateOf("सभी वयस्क पशु") }
    var date by remember { mutableStateOf("15 जुलाई 2025") }
    var location by remember { mutableStateOf("गाँव भाटी पशु उपकेंद्र") }
    var dosage by remember { mutableStateOf("1 ml sub-cut") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = tr("नया टीकाकरण शेड्यूल जोड़ें", "Add Vaccine Schedule", "नवीन लसीकरण वेळापत्रक जोडा", "નવું રસીકરણ સમયપત્રક ઉમેરો", "ਨਵਾਂ ਟੀਕਾਕਰਨ ਸ਼ਡਿਊਲ ਜੋੜੋ"),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(tr("वैक्सीन का नाम (हिंदी)", "Vaccine Name")) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = enName,
                    onValueChange = { enName = it },
                    label = { Text(tr("वैक्सीन का नाम (English)", "English Name")) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = disease,
                    onValueChange = { disease = it },
                    label = { Text(tr("लक्षित रोग", "Target Disease")) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text(tr("निर्धारित तिथि", "Scheduled Date")) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(tr("टीकाकरण केंद्र", "Vaccine Center")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdd(name, enName, disease, targetAnimal, date, location, dosage)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
            ) {
                Text(tr("शेड्यूल जोड़ें", "Save Schedule", "जतन करा", "ઉમેરો", "ਸੰਭਾਲੋ"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"))
            }
        }
    )
}
