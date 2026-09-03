package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.Warning
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Cattle
import com.example.ui.components.CattleAvatar
import androidx.compose.material3.MaterialTheme
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusPregnant
import com.example.ui.theme.StatusSick
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KisanHomeScreen(
    cattleList: List<Cattle>,
    vaccineList: List<VaccineRecord> = emptyList(),
    isHindi: Boolean = true,
    selectedLanguage: String = "हिंदी",
    onLanguageChange: (String) -> Unit = {},
    onToggleLanguage: () -> Unit = {},
    onOpenDrawer: () -> Unit,
    onNotificationsClick: () -> Unit,
    onMyCattleClick: () -> Unit,
    onStartDiagnosisClick: () -> Unit,
    onMedicinesClick: () -> Unit,
    onVaccineScheduleClick: () -> Unit,
    onOpenMapClick: () -> Unit,
    onEmergencyCallClick: () -> Unit,
    farmerName: String = "राम किसान",
    farmerVillage: String = "गाँव भाटी, जयपुर",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showTipDialog by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }
    var langMenuExpanded by remember { mutableStateOf(false) }
    val languages = listOf("हिंदी", "English", "मराठी", "ગુજરાતી", "ਪੰਜਾਬੀ")

    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String {
        return when (selectedLanguage) {
            "English" -> en
            "मराठी" -> mr
            "ગુજરાતી" -> gu
            "ਪੰਜਾਬੀ" -> pa
            else -> hi
        }
    }

    val totalCattle = if (cattleList.isEmpty()) 12 else cattleList.size
    val sickCattle = cattleList.count { it.status == "बीमार" || it.status == "Sick" }.let { if (it == 0 && cattleList.isEmpty()) 2 else it }
    val pregnantCattle = cattleList.count { it.status == "गर्भवती" || it.status == "Pregnant" }.let { if (it == 0 && cattleList.isEmpty()) 1 else it }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top App Bar with Language Switcher and Notifications
            TopAppBar(
                title = {
                    Text(
                        text = tr("किसान होम", "Farmer Home", "शेतकरी होम", "ખેડૂત હોમ", "ਕਿਸਾਨ ਹੋਮ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Quick 5-Language Switcher Dropdown Button
                    Box {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier
                                .clickable { langMenuExpanded = true }
                                .padding(end = 4.dp)
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
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = GreenDark,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
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
                                                color = if (lang == selectedLanguage) GreenDark else Color(0xFF1B241C)
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

                    // Notification Bell with dynamic Due Vaccine Alert badge
                    val dueVaccineCount = vaccineList.count { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
                    IconButton(onClick = onNotificationsClick) {
                        Box {
                            Icon(
                                imageVector = if (dueVaccineCount > 0) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = if (dueVaccineCount > 0) Color(0xFFE65100) else Color(0xFF1B241C)
                            )
                            if (dueVaccineCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFC62828)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$dueVaccineCount",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                val isWide = maxWidth > 680.dp
                Column(
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxWidth()
                        .padding(horizontal = if (isWide) 24.dp else 16.dp, vertical = 12.dp)
                ) {
                // Greeting & Location with Farmer Avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tr("नमस्ते, ${farmerName.take(15)} जी 👋", "Hello, ${farmerName.take(15)} 👋", "नमस्कार, ${farmerName.take(15)} जी 👋", "નમસ્તે, ${farmerName.take(15)} ભાઈ 👋", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ, ${farmerName.take(15)} ਜੀ 👋"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tr("स्थान: $farmerVillage", "Location: $farmerVillage", "स्थान: $farmerVillage", "સ્થળ: $farmerVillage", "ਸਥਾਨ: $farmerVillage"),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.img_farmer_avatar),
                        contentDescription = "Farmer Avatar",
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, GreenPrimary, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Farm Hero Landscape Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_farm_landscape),
                            contentDescription = "Rural Dairy Farm",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Gradient Overlay for text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = tr("पशुसेतु डिजिटल डेयरी प्रबंधन", "PashuSetu Smart Herd Care", "पशूसेतू डिजिटल डेअरी व्यवस्थापन", "પશુસેતુ ડિજિટલ ડેરી સંચાલન", "ਪਸ਼ੂਸੇਤੂ ਡਿਜੀਟਲ ਡੇਅਰੀ ਪ੍ਰਬੰਧਨ"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = tr(
                                    "कैमरा, वॉयस एवं लाइव जीपीएस मैप सहायता युक्त",
                                    "Equipped with Live Camera, Voice AI & GPS Map",
                                    "कॅमेरा, व्हॉइस आणि थेट जीपीएस नकाशा सहाय्य",
                                    "કૅમેરા, વૉઇસ અને લાઇવ જીપીએસ મેપ સહાયતા",
                                    "ਕੈਮਰਾ, ਵੌਇਸ ਅਤੇ ਲਾਈਵ ਜੀਪੀਐਸ ਮੈਪ ਸਹਾਇਤਾ ਯੁਕਤ"
                                ),
                                fontSize = 11.sp,
                                color = Color(0xFFE0E0E0)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "मेरे पशु" Overview Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMyCattleClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tr("मेरे पशुधन की स्थिति", "My Herd Health Status", "माझ्या पशुधनाची स्थिती", "મારા પશુધનની સ્થિતિ", "ਮੇਰੇ ਪਸ਼ੂਧਨ ਦੀ ਸਥਿਤੀ"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = tr("सभी देखें →", "View all →", "सर्व पहा →", "બધા જુઓ →", "ਸਾਰੇ ਵੇਖੋ →"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Stats Pill 1: कुल पशु
                            StatBadge(
                                count = "$totalCattle",
                                label = tr("कुल पशु", "Total", "एकूण पशू", "કુલ પશુ", "ਕੁੱਲ ਪਸ਼ੂ"),
                                countColor = Color(0xFF1B5E20),
                                bgColor = Color(0xFFE8F5E9)
                            )

                            // Stats Pill 2: बीमार
                            StatBadge(
                                count = "$sickCattle",
                                label = tr("बीमार", "Sick", "आजारी", "બીમાર", "ਬਿਮਾਰ"),
                                countColor = StatusSick,
                                bgColor = Color(0xFFFFEBEE)
                            )

                            // Stats Pill 3: गर्भवती
                            StatBadge(
                                count = "$pregnantCattle",
                                label = tr("गर्भवती", "Pregnant", "गाभण", "સગર્ભા", "ਗਰਭਵਤੀ"),
                                countColor = StatusPregnant,
                                bgColor = Color(0xFFFFF3E0)
                            )

                            // Cow Portrait Avatar
                            Image(
                                painter = painterResource(id = R.drawable.img_gir_cow),
                                contentDescription = "Gir Cow",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NEW: Real Interactive Map Feature Banner Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenMapClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = CardDefaults.outlinedCardBorder().copy(width = 1.5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GreenDark,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = "Map",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = tr("पशु चिकित्सालय व प्रकोप मैप", "Vet Clinics & Outbreak Live Map", "पशू रुग्णालय व उद्रेक थेट नकाशा", "પશુ દવાખાનું અને રોગચાળો મેપ", "ਪਸ਼ੂ ਹਸਪਤਾਲ ਅਤੇ ਬਿਮਾਰੀ ਲਾਈਵ ਨਕਸ਼ਾ"),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B241C)
                                )
                                Text(
                                    text = tr(
                                        "नजदीकी अस्पताल, मोबाइल वैन और हॉटस्पॉट देखें",
                                        "View nearby clinics, mobile vans & hotspots",
                                        "जवळची रुग्णालये, फिरती व्हॅन आणि हॉटस्पॉट पहा",
                                        "નજીકના દવાખાના, મોબાઇલ વાન અને હોટસ્પોટ જુઓ",
                                        "ਨੇੜਲੇ ਹਸਪਤਾਲ, ਮੋਬਾਈਲ ਵੈਨ ਅਤੇ ਹੌਟਸਪੌਟ ਵੇਖੋ"
                                    ),
                                    fontSize = 12.sp,
                                    color = Color(0xFF424242)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GreenDark
                        ) {
                            Text(
                                text = tr("मैप खोलें", "Open Map", "नकाशा उघडा", "મેપ ખોલો", "ਨਕਸ਼ਾ ਖੋਲ੍ਹੋ"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dynamic Next Scheduled Vaccine Card with Due Alert Banner
                val nextVaccine = vaccineList.firstOrNull { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
                    ?: vaccineList.firstOrNull { it.status == VaccineStatus.UPCOMING }
                    ?: vaccineList.firstOrNull()

                // Urgent Alert Banner if any vaccine is due/overdue
                val hasDueVaccines = vaccineList.any { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
                if (hasDueVaccines) {
                    val dueItem = vaccineList.firstOrNull { it.status == VaccineStatus.DUE || it.status == VaccineStatus.OVERDUE }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVaccineScheduleClick() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF9800)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tr("⚠️ टीकाकरण अलर्ट: टीका बाकी है!", "⚠️ Vaccination Alert: Vaccine Due!", "⚠️ लसीकरण अलर्ट: लस बाकी आहे!", "⚠️ રસીકરણ એલર્ટ: રસી બાકી છે!", "⚠️ ਟੀਕਾਕਰਨ ਅਲਰਟ: ਟੀਕਾ ਬਾਕੀ ਹੈ!"),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD84315)
                                )
                                Text(
                                    text = if (dueItem != null) {
                                        "${if (selectedLanguage == "English") dueItem.englishName else dueItem.vaccineName} • ${dueItem.scheduledDate}"
                                    } else {
                                        tr("नजदीकी केंद्र पर टीकाकरण कराएं", "Visit nearest center for vaccination")
                                    },
                                    fontSize = 11.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE65100)
                            ) {
                                Text(
                                    text = tr("देखें →", "View →", "पहा →", "જુઓ →", "ਵੇਖੋ →"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // "अगला टीकाकरण" Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVaccineScheduleClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = tr("अगला टीकाकरण (Vaccine Schedule)", "Next Scheduled Vaccine", "पुढील नियोजित लसीकरण", "આગામી રસીકરણ", "ਅਗਲਾ ਟੀਕਾਕਰਨ"),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                if (nextVaccine?.status == VaccineStatus.DUE || nextVaccine?.status == VaccineStatus.OVERDUE) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFFFEBEE)
                                    ) {
                                        Text(
                                            text = tr("बाकी (Due)", "Due"),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC62828),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (nextVaccine != null) {
                                    if (selectedLanguage == "English") nextVaccine.englishName else nextVaccine.vaccineName
                                } else {
                                    tr("FMD बूस्टर वैक्सीन (गौशाला बैच)", "FMD Booster Vaccine (Dairy Batch)")
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (nextVaccine != null) {
                                    "${tr("दिनांक", "Date")}: ${nextVaccine.scheduledDate} • ${nextVaccine.locationCenter}"
                                } else {
                                    tr("दिनांक: 20 मई 2025 • गाँव भाटी केंद्र", "Date: 20 May 2025 • Bhati Center")
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = GreenDark
                            )
                        }

                        // Calendar Icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = GreenDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // "त्वरित सेवाएं" Quick Actions Grid
                Text(
                    text = tr("त्वरित सेवाएं", "Quick Actions", "जलद सेवा", "ઝડપી સેવાઓ", "ਤੁਰੰਤ ਸੇਵਾਵਾਂ"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = tr("पशु की जाँच\n(AI कैमरा)", "Check Cattle\n(AI Camera)", "पशू तपासणी\n(AI कॅमेरा)", "પશુ તપાસ\n(AI કૅમેરા)", "ਪਸ਼ੂ ਜਾਂਚ\n(AI ਕੈਮਰਾ)"),
                        icon = Icons.Default.LocalHospital,
                        iconTint = Color(0xFF0277BD),
                        bgColor = Color(0xFFE1F5FE),
                        modifier = Modifier.weight(1f),
                        onClick = onStartDiagnosisClick
                    )
                    QuickActionCard(
                        title = tr("दवा / उपचार\nसूची", "Medicines &\nDosage", "औषध / उपचार\nयादी", "દવા / સારવાર\nયાદી", "ਦਵਾਈ / ਇਲਾਜ\nਸੂਚੀ"),
                        icon = Icons.Default.Medication,
                        iconTint = Color(0xFF283593),
                        bgColor = Color(0xFFEDE7F6),
                        modifier = Modifier.weight(1f),
                        onClick = onMedicinesClick
                    )
                    QuickActionCard(
                        title = tr("टीकाकरण\nशेड्यूल", "Vaccination\nSchedule", "लसीकरण\nवेळापत्रक", "રસીકરણ\nસમયપત્રક", "ਟੀਕਾਕਰਨ\nਸ਼ਡਿਊਲ"),
                        icon = Icons.Default.Vaccines,
                        iconTint = Color(0xFF6A1B9A),
                        bgColor = Color(0xFFF3E5F5),
                        modifier = Modifier.weight(1f),
                        onClick = onVaccineScheduleClick
                    )
                    QuickActionCard(
                        title = tr("पशु एम्बुलेंस\n1962 कॉल", "Animal\nAmbulance", "पशू रुग्णवाहिका\n1962 कॉल", "પશુ એમ્બ્યુલન્સ\n1962 કૉલ", "ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ\n1962 ਕਾਲ"),
                        icon = Icons.Default.Call,
                        iconTint = Color(0xFFC62828),
                        bgColor = Color(0xFFFFEBEE),
                        modifier = Modifier.weight(1f),
                        onClick = { showEmergencyDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // "आज की सलाह" Daily Tip Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFFDCEDC8), Color(0xFFC5E1A5))))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Tip",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("पशु विशेषज्ञ सलाह", "Veterinary Expert Tip", "पशू तज्ज्ञ सल्ला", "પશુ નિષ્ણાત સલાહ", "ਪਸ਼ੂ ਮਾਹਿਰ ਸਲਾਹ"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tr(
                                    "गर्मियों में दुधारू पशुओं को दिन में 4 बार ताजा पानी और हरा चारा दें।",
                                    "Provide dairy cattle with fresh clean water 4 times daily in summer.",
                                    "उन्हाळ्यात दुभत्या जनावरांना दिवसातून ४ वेळा ताजे पाणी व हिरवा चारा द्या.",
                                    "ઉનાળામાં દૂધાળા પશુઓને દિવસમાં 4 વાર તાજું પાણી અને લીલો ચારો આપો.",
                                    "ਗਰਮੀਆਂ ਵਿੱਚ ਦੁਧਾਰੂ ਪਸ਼ੂਆਂ ਨੂੰ ਦਿਨ ਵਿੱਚ 4 ਵਾਰ ਤਾਜ਼ਾ ਪਾਣੀ ਅਤੇ ਹਰਾ ਚਾਰਾ ਦਿਓ।"
                                ),
                                fontSize = 13.sp,
                                color = Color(0xFF333333),
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = tr("और जानें", "Read more", "अधिक जाणून घ्या", "વધુ જાણો", "ਹੋਰ ਜਾਣੋ"),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark,
                                modifier = Modifier
                                    .clickable { showTipDialog = true }
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
            }
        }

        // Emergency Dialog (Triggers real dialer to 1962)
        if (showEmergencyDialog) {
            AlertDialog(
                onDismissRequest = { showEmergencyDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Emergency",
                            tint = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tr("पशु आपातकालीन सेवा (1962)", "Animal Emergency Helpline (1962)", "पशू आणीबाणी सेवा (1962)", "પશુ કટોકટી સેવા (1962)", "ਪਸ਼ੂ ਐਮਰਜੈਂਸੀ ਸੇਵਾ (1962)"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = tr(
                                "सरकारी टोल फ्री पशु एम्बुलेंस और डॉक्टर सहायता केंद्र:\n",
                                "Government toll-free animal ambulance and veterinary support:\n",
                                "शासकीय टोल फ्री पशू रुग्णवाहिका आणि डॉक्टर मदत केंद्र:\n",
                                "સરકારી ટોલ ફ્રી પશુ એમ્બ્યુલન્સ અને ડૉક્ટર સહાય કેન્દ્ર:\n",
                                "ਸਰਕਾਰੀ ਟੋਲ ਫ੍ਰੀ ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ ਅਤੇ ਡਾਕਟਰ ਸਹਾਇਤਾ ਕੇਂਦਰ:\n"
                            ),
                            fontSize = 14.sp
                        )
                        Text(
                            text = tr(
                                "📞 टोल-फ्री: 1962 (24x7 उपलब्ध)\n🏥 नजदीकी पशु चिकित्सालय: गाँव भाटी केंद्र\n👨‍⚕️ ऑन-ड्यूटी डॉक्टर: डॉ. महेश (+91 98765 00112)",
                                "📞 Toll-Free: 1962 (24x7)\n🏥 Nearest Clinic: Bhati Center\n👨‍⚕️ On-Duty Vet: Dr. Mahesh (+91 98765 00112)",
                                "📞 टोल-फ्री: 1962 (24x7 उपलब्ध)\n🏥 जवळचे पशू रुग्णालय: गाव भाटी केंद्र\n👨‍⚕️ ऑन-ड्युटी डॉक्टर: डॉ. महेश (+91 98765 00112)",
                                "📞 ટોલ-ફ્રી: 1962 (24x7 ઉપલબ્ધ)\n🏥 નજીકનું પશુ દવાખાનું: ગામ ભાટી કેન્દ્ર\n👨‍⚕️ ઓન-ડ્યુટી ડૉક્ટર: ડૉ. મહેશ (+91 98765 00112)",
                                "📞 ਟੋਲ-ਫ੍ਰੀ: 1962 (24x7 ਉਪਲਬਧ)\n🏥 ਨੇੜਲਾ ਪਸ਼ੂ ਹਸਪਤਾਲ: ਪਿੰਡ ਭਾਟੀ ਕੇਂਦਰ\n👨‍⚕️ ਆਨ-ਡਿਊਟੀ ਡਾਕਟਰ: ਡਾ. ਮਹੇਸ਼ (+91 98765 00112)"
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E3A2F)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showEmergencyDialog = false
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1962"))
                        context.startActivity(intent)
                    }) {
                        Text(
                            tr("1962 डायल करें", "Dial 1962", "1962 डायल करा", "1962 ડાયલ કરો", "1962 ਡਾਇਲ ਕਰੋ"),
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEmergencyDialog = false }) {
                        Text(tr("बंद करें", "Close", "बंद करा", "બંધ કરો", "ਬੰਦ ਕਰੋ"))
                    }
                }
            )
        }

        // Tip Dialog
        if (showTipDialog) {
            AlertDialog(
                onDismissRequest = { showTipDialog = false },
                title = {
                    Text(
                        text = tr("गर्मी में पशु देखभाल के सुझाव", "Summer Livestock Care Tips", "उन्हाळ्यात पशू संगोपन सल्ला", "ઉનાળામાં પશુ સંભાળ ટિપ્સ", "ਗਰਮੀਆਂ ਵਿੱਚ ਪਸ਼ੂ ਸੰਭਾਲ ਸੁਝਾਅ"),
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = tr(
                            "1. पशुओं को दिन में कम से कम 4-5 बार ताजा व ठंडा पानी पिलाएं।\n" +
                                "2. दोपहर के समय पशुओं को सीधे धूप में न बांधें, हवादार छप्पर या पेड़ की छाया में रखें।\n" +
                                "3. चारे में हरा चारा व मिनरल मिक्सचर (खनिज लवण) अवश्य शामिल करें।\n" +
                                "4. डिहाइड्रेशन से बचाने के लिए पानी में थोड़ा गुड़ और नमक मिलाकर दें।",
                            "1. Provide fresh and cool drinking water 4-5 times a day.\n" +
                                "2. Keep animals under airy shaded shed away from direct midday sun.\n" +
                                "3. Add green fodder and mineral mixtures in daily feed.\n" +
                                "4. Mix a pinch of jaggery and salt in water to prevent dehydration.",
                            "1. जनावरांना दिवसातून किमान ४-५ वेळा ताजे व थंड पाणी द्या.\n" +
                                "2. दुपारच्या वेळी जनावरांना थेट उन्हात बांधू नका, सावलीत ठेवा.\n" +
                                "3. चाऱ्यामध्ये हिरवा चारा व खनिज मिश्रण आवर्जून द्या.\n" +
                                "4. डिहायड्रेशन टाळण्यासाठी पाण्यात थोडे गूळ व मीठ मिसळा.",
                            "1. પશુઓને દિવસમાં ઓછામાં ઓછું 4-5 વાર તાજું અને ઠંડુ પાણી પીવડાવો.\n" +
                                "2. બપોરે પશુઓને સીધા તડકામાં ન બાંધો, છાંયડામાં રાખો.\n" +
                                "3. ચારામાં લીલો ચારો અને મિનરલ મિક્સચર જરૂર સામેલ કરો.\n" +
                                "4. ડીહાઇડ્રેશનથી બચાવવા માટે પાણીમાં થોડો ગોળ અને મીઠું ઉમેરો.",
                            "1. ਪਸ਼ੂਆਂ ਨੂੰ ਦਿਨ ਵਿੱਚ ਘੱਟੋ-ਘੱਟ 4-5 ਵਾਰ ਤਾਜ਼ਾ ਅਤੇ ਠੰਡਾ ਪਾਣੀ ਪਿਲਾਓ।\n" +
                                "2. ਦੁਪਹਿਰ ਵੇਲੇ ਪਸ਼ੂਆਂ ਨੂੰ ਸਿੱਧੀ ਧੁੱਪ ਵਿੱਚ ਨਾ ਬੰਨ੍ਹੋ, ਛਾਂ ਵਿੱਚ ਰੱਖੋ।\n" +
                                "3. ਚਾਰੇ ਵਿੱਚ ਹਰਾ ਚਾਰਾ ਅਤੇ ਖਣਿਜ ਮਿਸ਼ਰਣ ਜ਼ਰੂਰ ਸ਼ਾਮਲ ਕਰੋ।\n" +
                                "4. ਡੀਹਾਈਡ੍ਰੇਸ਼ਨ ਤੋਂ ਬਚਾਉਣ ਲਈ ਪਾਣੀ ਵਿੱਚ ਥੋੜ੍ਹਾ ਗੁੜ ਅਤੇ ਲੂਣ ਮਿਲਾਓ।"
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showTipDialog = false }) {
                        Text(tr("समझ गया", "Understood", "समजले", "સમજાઈ ગયું", "ਸਮਝ ਆ ਗਿਆ"), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun StatBadge(
    count: String,
    label: String,
    countColor: Color,
    bgColor: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = countColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(112.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
