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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.model.DistrictSummary
import com.example.ui.components.DistrictHealthMap
import com.example.ui.components.RealMapView
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusHealthy
import com.example.ui.theme.StatusSick

import com.example.ui.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictOfficerDashboardScreen(
    summary: DistrictSummary,
    isHindi: Boolean = true,
    selectedLanguage: String = "हिंदी",
    onLanguageChange: (String) -> Unit = {},
    onToggleLanguage: () -> Unit = {},
    onOpenDrawer: () -> Unit,
    onNotificationsClick: () -> Unit,
    onDistrictChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var districtMenuExpanded by remember { mutableStateOf(false) }
    var langMenuExpanded by remember { mutableStateOf(false) }
    var showDispatchDialog by remember { mutableStateOf(false) }
    var dispatchedZone by remember { mutableStateOf<String?>(null) }
    var showSuccessToast by remember { mutableStateOf(false) }
    var selectedMapTab by remember { mutableStateOf(0) } // 0 = Live OpenStreetMap, 1 = Block Matrix

    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val districts = listOf("जयपुर", "जोधपुर", "उदयपुर", "अलवर")

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar matching Screen 9
            TopAppBar(
                title = {
                    Text(
                        text = tr("जिला पशु चिकित्सा डैशबोर्ड", "District Animal Health Dashboard", "जिल्हा पशू आरोग्य डॅशबोर्ड", "જિલ્લા પશુ આરોગ્ય ડેશબોર્ડ", "ਜ਼ਿਲ੍ਹਾ ਪਸ਼ੂ ਸਿਹਤ ਡੈਸ਼ਬੋਰਡ"),
                        fontSize = 18.sp,
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
                    // Language Switcher Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8F5E9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.5f)),
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
                                    contentDescription = null,
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
                // Officer Profile Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tr("नमस्ते अधिकारी महोदय 👋", "Hello Officer 👋", "नमस्कार अधिकारी महोदय 👋", "નમસ્તે અધિકારી સાહેબ 👋", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ ਅਧਿਕਾਰੀ ਸਾਹਿਬ 👋"),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B241C)
                            )
                            Text(
                                text = tr("पशुपालन एवं डेयरी विकास विभाग • जयपुर मंडल", "Animal Husbandry & Dairy Dept • Jaipur Div", "पशूसंवर्धन विभाग • जयपूर विभाग", "પશુપાલન વિભાગ • જયપુર", "ਪਸ਼ੂ ਪਾਲਣ ਵਿਭਾਗ • ਜੈਪੁਰ ਮੰਡਲ"),
                                fontSize = 12.sp,
                                color = Color(0xFF616161)
                            )
                        }
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_officer_avatar),
                            contentDescription = "Officer Avatar",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                        )
                    }
                }

                // Filter Controls Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // State Dropdown
                        FilterPill(text = summary.state, onClick = {})

                        // District Dropdown (Interactive)
                        Box {
                            FilterPill(
                                text = summary.district,
                                onClick = { districtMenuExpanded = true }
                            )
                            DropdownMenu(
                                expanded = districtMenuExpanded,
                                onDismissRequest = { districtMenuExpanded = false }
                            ) {
                                districts.forEach { d ->
                                    DropdownMenuItem(
                                        text = { Text(d) },
                                        onClick = {
                                            onDistrictChange(d)
                                            districtMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Date Range Filter
                        FilterPill(
                            text = summary.dateRange,
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Section Title: Outbreak Mapping
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tr("रोग प्रकोप मैपिंग व विश्लेषण", "Disease Outbreak Map & Spread", "रोग प्रादुर्भाव मॅपिंग व विश्लेषण", "રોગ ફેલાવો મેપિંગ અને વિશ્લેષણ", "ਰੋਗ ਫੈਲਾਅ ਮੈਪਿੰਗ ਅਤੇ ਵਿਸ਼ਲੇਸ਼ਣ"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B241C)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Map View Mode Tabs
                    TabRow(
                        selectedTabIndex = selectedMapTab,
                        containerColor = Color(0xFFE8F5E9),
                        contentColor = GreenDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Tab(
                            selected = selectedMapTab == 0,
                            onClick = { selectedMapTab = 0 },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        tr("लाइव GPS मैप", "Live GPS Map", "लाइव्ह GPS नकाशा", "લાઇવ GPS મેપ", "ਲਾਈਵ GPS ਨਕਸ਼ਾ"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                        Tab(
                            selected = selectedMapTab == 1,
                            onClick = { selectedMapTab = 1 },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ViewModule, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        tr("ब्लॉक मैट्रिक्स", "Block Matrix", "ब्लॉक मॅट्रिक्स", "બ્લોક મેટ્રિક્સ", "ਬਲਾਕ ਮੈਟ੍ਰਿਕਸ"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Interactive Map Component or Block Matrix Visualizer
                item {
                    if (selectedMapTab == 0) {
                        // Real OpenStreetMap View
                        RealMapView(
                            isHindi = isHindi,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            isOfficerMode = true,
                            onDispatchUnit = { pin ->
                                dispatchedZone = if (isHindi) pin.titleHindi else pin.titleEnglish
                                showDispatchDialog = true
                            }
                        )
                    } else {
                        // District Block Matrix View
                        DistrictHealthMap(
                            summary = summary,
                            onZoneClick = { zoneName, cases ->
                                dispatchedZone = zoneName
                                showDispatchDialog = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Outbreak Alert Notification Action Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Emergency,
                                    contentDescription = "Alert",
                                    tint = StatusSick,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = tr("FMD आउटब्रेक अलर्ट (शाहपुरा क्लस्टर)", "FMD Outbreak Alert (Shahpura Cluster)", "FMD उद्रेक इशारा (शहापुरा क्लस्टर)", "FMD આઉટબ્રેક ચેતવણી (શાહપુરા ક્લસ્ટર)", "FMD ਆਊਟਬ੍ਰੇਕ ਅਲਰਟ (ਸ਼ਾਹਪੁਰਾ ਕਲੱਸਟਰ)"),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusSick
                                    )
                                    Text(
                                        text = tr("32 पुष्ट मामले • त्वरित टीकाकरण दल आवश्यक", "32 confirmed cases • Rapid vaccine unit needed", "३२ पुष्टी झालेली प्रकरणे • तातडीचे लसीकरण पथक आवश्यक", "32 પુષ્ટિ કેસ • તાત્કાલિક રસીકરણ ટીમ જરૂરી", "32 ਪੁਸ਼ਟੀ ਕੀਤੇ ਕੇਸ • ਤੁਰੰਤ ਟੀਕਾਕਰਨ ਟੀਮ ਲੋੜੀਂਦੀ"),
                                        fontSize = 11.sp,
                                        color = Color(0xFF555555)
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    dispatchedZone = tr("शाहपुरा / कोटपूतली क्लस्टर", "Shahpura / Kotputli Cluster", "शहापुरा / कोटपुतली क्लस्टर", "શાહપુરા / કોટપૂતલી ક્લસ્ટર", "ਸ਼ਾਹਪੁਰਾ / ਕੋਟਪੂਤਲੀ ਕਲੱਸਟਰ")
                                    showDispatchDialog = true
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = StatusSick),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(tr("दल भेजें", "Dispatch", "पथक पाठवा", "ટીમ મોકલો", "ਟੀਮ ਭੇਜੋ"), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Summary Section
                item {
                    Text(
                        text = "${tr("सारांश", "Summary", "सारांश", "સારાંશ", "ਸਾਰਾਂਸ਼")} (${summary.district})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Summary Stat Cards Row matching Screen 9
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OfficerStatCard(
                            count = "${summary.sickAnimals}",
                            label = tr("बीमार पशु (सक्रिय)", "Active Cases", "आजारी पशू (सक्रिय)", "બીમાર પશુ (સક્રિય)", "ਬਿਮਾਰ ਪਸ਼ੂ (ਐਕਟਿਵ)"),
                            countColor = StatusSick,
                            bgColor = Color(0xFFFFEBEE),
                            modifier = Modifier.weight(1f)
                        )
                        OfficerStatCard(
                            count = "${((summary.vaccinatedAnimals.toDouble() / (summary.totalAnimals.coerceAtLeast(1))) * 100).toInt()}%",
                            label = tr("टीकाकरण कवरेज", "Vaccination %", "लसीकरण कव्हरेज", "રસીકરણ કવરેજ", "ਟੀਕਾਕਰਨ ਕਵਰੇਜ"),
                            countColor = StatusHealthy,
                            bgColor = Color(0xFFE8F5E9),
                            modifier = Modifier.weight(1f)
                        )
                        OfficerStatCard(
                            count = "${summary.zones.size}",
                            label = tr("निगरानी क्षेत्र", "High-Risk Villages", "निगरानी क्षेत्रे", "દેખરેખ વિસ્તારો", "ਨਿਗਰਾਨੀ ਖੇਤਰ"),
                            countColor = Color(0xFFE65100),
                            bgColor = Color(0xFFFFF3E0),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Quarantine Zones Section
                item {
                    Text(
                        text = tr("सक्रिय निगरानी क्षेत्र", "Active Containment Zones", "सक्रिय नियंत्रण क्षेत्रे", "સક્રિય દેખરેખ વિસ્તારો", "ਐਕਟਿਵ ਕੰਟੇਨਮੈਂਟ ਜ਼ੋਨ"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Zones list
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            ZoneRowItem(
                                tr("शाहपुरा (कोटपूतली)", "Shahpura (Kotputli)", "शहापुरा (कोटपुतली)", "શાહપુરા (કોટપૂતલી)", "ਸ਼ਾਹਪੁਰਾ (ਕੋਟਪੂਤਲੀ)"),
                                tr("32 मामले (FMD)", "32 Cases (FMD)", "३२ प्रकरणे (FMD)", "32 કેસ (FMD)", "32 ਕੇਸ (FMD)"),
                                tr("प्रतिबंधित आवागमन", "Movement Restricted", "हालचालीस बंदी", "પ્રતિબંધિત હિલચાલ", "ਪ੍ਰਤੀਬੰਧਿਤ ਆਵਾਜਾਈ"),
                                StatusSick
                            )
                            ZoneRowItem(
                                tr("बस्सी (जयपुर ग्रामीण)", "Bassi (Jaipur Rural)", "बस्सी (जयपूर ग्रामीण)", "બસ્સી (જયપુર ગ્રામીણ)", "ਬੱਸੀ (ਜੈਪੁਰ ਪੇਂਡੂ)"),
                                tr("14 मामले (HS)", "14 Cases (HS)", "१४ प्रकरणे (HS)", "14 કેસ (HS)", "14 ਕੇਸ (HS)"),
                                tr("सघन निगरानी", "Intensive Watch", "तीव्र पाळत", "સઘન દેખરેખ", "ਸਖ਼ਤ ਨਿਗਰਾਨੀ"),
                                Color(0xFFFFA000)
                            )
                            ZoneRowItem(
                                tr("चाकसू (जयपुर)", "Chaksu (Jaipur)", "चाकसू (जयपूर)", "ચાકસૂ (જયપુર)", "ਚਾਕਸੂ (ਜੈਪੁਰ)"),
                                tr("3 मामले (LSD)", "3 Cases (LSD)", "३ प्रकरणे (LSD)", "3 કેસ (LSD)", "3 ਕੇਸ (LSD)"),
                                tr("नियंत्रणाधीन", "Under Control", "नियंत्रणाखाली", "નિયંત્રણ હેઠળ", "ਨਿਯੰਤਰਣ ਅਧੀਨ"),
                                StatusHealthy
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Dispatch Team Dialog
        if (showDispatchDialog) {
            AlertDialog(
                onDismissRequest = { showDispatchDialog = false },
                title = {
                    Text(
                        text = tr("त्वरित सहायता दल रवाना करें", "Dispatch Rapid Response Team", "तातडीचे मदत पथक रवाना करा", "તાત્કાલિક સહાય ટીમ રવાના કરો", "ਤੁਰੰਤ ਸਹਾਇਤਾ ਟੀਮ ਰਵਾਨਾ ਕਰੋ"),
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "${tr("क्षेत्र", "Target Zone", "भाग", "વિસ્તાર", "ਖੇਤਰ")}: ${dispatchedZone ?: tr("चयनित ब्लॉक", "Selected Block", "निवडलेला ब्लॉक", "પસંદ કરેલ બ્લોક", "ਚੁਣਿਆ ਗਿਆ ਬਲਾਕ")}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "क्या आप इस क्षेत्र में पशु चिकित्सा दल, मोबाइल एम्बुलेंस और 200 FMD वैक्सीन डोज रवाना करना चाहते हैं?",
                                "Do you want to deploy a veterinary team, mobile ambulance, and 200 vaccine doses to this area?",
                                "तुम्हाला या भागात पशुवैद्यकीय पथक, मोबाईल रुग्णवाहिका आणि २०० लस डोस पाठवायचे आहेत का?",
                                "શું તમે આ વિસ્તારમાં પશુ ચિકિત્સા ટીમ, મોબાઈલ એમ્બ્યુલન્સ અને 200 રસી ડોઝ મોકલવા માંગો છો?",
                                "ਕੀ ਤੁਸੀਂ ਇਸ ਖੇਤਰ ਵਿੱਚ ਵੈਟਰਨਰੀ ਟੀਮ, ਮੋਬਾਈਲ ਐਂਬੂਲੈਂਸ ਅਤੇ 200 ਵੈਕਸੀਨ ਡੋਜ਼ ਭੇਜਣਾ ਚਾਹੁੰਦੇ ਹੋ?"
                            ),
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDispatchDialog = false
                            showSuccessToast = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusSick)
                    ) {
                        Text(tr("हाँ, रवाना करें", "Confirm Dispatch", "होय, रवाना करा", "હા, રવાના કરો", "ਹਾਂ, ਰਵਾਨਾ ਕਰੋ"), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDispatchDialog = false }) {
                        Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"))
                    }
                }
            )
        }

        // Success Confirmation Dialog
        if (showSuccessToast) {
            AlertDialog(
                onDismissRequest = { showSuccessToast = false },
                title = {
                    Text(
                        text = tr("दल सफलतापूर्वक रवाना ✓", "Unit Dispatched Successfully ✓", "पथक यशस्वीरीत्या रवाना ✓", "ટીમ સફળતાપૂર્વક રવાના ✓", "ਟੀਮ ਸਫ਼ਲਤਾਪੂਰਵਕ ਰਵਾਨਾ ✓"),
                        fontWeight = FontWeight.Bold,
                        color = StatusHealthy
                    )
                },
                text = {
                    Text(
                        text = tr(
                            "त्वरित प्रतिक्रिया दल ${dispatchedZone ?: ""} के लिए रवाना हो चुका है। संबंधित ग्राम प्रधान को SMS सूचना भेज दी गई है।",
                            "Rapid response team is en route to ${dispatchedZone ?: ""}. SMS alerts dispatched to local livestock farmers.",
                            "तातडीचे प्रतिसाद पथक ${dispatchedZone ?: ""} साठी रवाना झाले आहे. संबंधित सरपंचांना SMS पाठवला आहे.",
                            "તાત્કાલિક સહાય ટીમ ${dispatchedZone ?: ""} માટે રવાના થઈ ચૂકી છે. સંબંધિત સરપંચને SMS મોકલી દેવાયો છે.",
                            "ਤੁਰੰਤ ਸਹਾਇਤਾ ਟੀਮ ${dispatchedZone ?: ""} ਲਈ ਰਵਾਨਾ ਹੋ ਚੁੱਕੀ ਹੈ। ਸੰਬੰਧਿਤ ਸਰਪੰਚ ਨੂੰ SMS ਸੂਚਨਾ ਭੇਜ ਦਿੱਤੀ ਗਈ ਹੈ।"
                        ),
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showSuccessToast = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(tr("ठीक है", "OK", "ठीक आहे", "બરાબર", "ਠੀਕ ਹੈ"))
                    }
                }
            )
        }
    }
}

@Composable
private fun FilterPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF666666),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun OfficerStatCard(
    count: String,
    label: String,
    countColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = countColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF424242),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ZoneRowItem(name: String, cases: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = cases, fontSize = 11.sp, color = Color.Gray)
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = statusColor.copy(alpha = 0.15f)
        ) {
            Text(
                text = status,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}
