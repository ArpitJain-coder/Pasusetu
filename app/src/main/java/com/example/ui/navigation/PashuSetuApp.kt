package com.example.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.PashuSetuBottomBar
import androidx.compose.material.icons.filled.Vaccines
import com.example.ui.components.RealMapView
import com.example.ui.components.RoleAvatar
import com.example.ui.screens.AlertsScreen
import com.example.ui.screens.CaseDetailScreen
import com.example.ui.screens.CattleDiagnosisScreen
import com.example.ui.screens.DiagnosisResultScreen
import com.example.ui.screens.DistrictOfficerDashboardScreen
import com.example.ui.screens.KisanHomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MedicinesScreen
import com.example.ui.screens.MyCattleScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RoleSelectionScreen
import com.example.ui.screens.VaccineScheduleScreen
import com.example.ui.screens.VetDoctorHomeScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.viewmodel.PashuSetuViewModel
import kotlinx.coroutines.launch

sealed class Screen {
    data object Welcome : Screen()
    data object RoleSelection : Screen()
    data class Login(val initialRole: UserRole = UserRole.FARMER) : Screen()
    data object MainApp : Screen()
    data object Diagnosis : Screen()
    data object DiagnosisResult : Screen()
    data object MyCattleDetail : Screen()
    data object CaseDetail : Screen()
    data object LiveMap : Screen()
    data object VaccineSchedule : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PashuSetuApp(
    viewModel: PashuSetuViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    var currentTab by remember { mutableStateOf("home") }

    val currentRole by viewModel.currentRole.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val cattleList by viewModel.cattleList.collectAsState()
    val appointmentList by viewModel.appointmentList.collectAsState()
    val caseList by viewModel.caseList.collectAsState()
    val selectedCattleForDiag by viewModel.selectedCattleForDiagnosis.collectAsState()
    val selectedSymptoms by viewModel.selectedSymptoms.collectAsState()
    val capturedPhoto by viewModel.capturedPhoto.collectAsState()
    val spokenText by viewModel.spokenText.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val diagnosisResult by viewModel.diagnosisResult.collectAsState()
    val isAnalyzingDiagnosis by viewModel.isAnalyzingDiagnosis.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()
    val districtSummary by viewModel.districtSummary.collectAsState()
    val vaccineList by viewModel.vaccineList.collectAsState()
    val alertList by viewModel.alertList.collectAsState()
    val medicineList by viewModel.medicineList.collectAsState()

    val isHindi = viewModel.isHindi()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val localizedRoleTitle = when (currentRole) {
        UserRole.FARMER -> tr("किसान", "Farmer", "शेतकरी", "ખેડૂત", "ਕਿਸਾਨ")
        UserRole.VET -> tr("पशु चिकित्सक", "Veterinary Doctor", "पशुवैद्यक", "પશુ ચિકિત્સક", "ਪਸ਼ੂ ਡਾਕਟਰ")
        UserRole.OFFICER -> tr("जिला अधिकारी", "District Officer", "जिल्हा अधिकारी", "જિલ્લા અધિકારી", "ਜ਼ਿਲ੍ਹਾ ਅਧਿਕਾਰੀ")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentScreen is Screen.MainApp,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                // Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GreenDark)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoleAvatar(
                            roleTitle = localizedRoleTitle,
                            size = 56.dp
                        )
                        IconButton(onClick = { scope.launch { drawerState.close() } }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = tr("बंद करें", "Close", "बंद करा", "બંધ કરો", "ਬੰਦ ਕਰੋ"),
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userProfile.name.ifBlank { tr("पशुसेतु उपयोगकर्ता", "PashuSetu User", "पशुसेतू वापरकर्ता", "પશુસેતુ વપરાશકર્તા", "ਪਸ਼ੂਸੇਤੂ ਉਪਭੋਗਤਾ") },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "$localizedRoleTitle • ${userProfile.district}",
                        fontSize = 13.sp,
                        color = Color(0xFFC8E6C9)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Items in Drawer
                NavigationDrawerItem(
                    label = { Text(tr("भूमिका बदलें", "Switch Role", "भूमिका बदला", "ભૂમિકા બદલો", "ਭੂਮਿਕਾ ਬਦਲੋ")) },
                    icon = { Icon(Icons.Default.People, contentDescription = null, tint = GreenDark) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentScreen = Screen.RoleSelection
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(tr("💉 टीकाकरण शेड्यूल व अलर्ट", "💉 Vaccine Schedule & Alerts", "💉 लसीकरण वेळापत्रक व अलर्ट", "💉 રસીકરણ સમયપત્રક અને એલર્ટ", "💉 ਟੀਕਾਕਰਨ ਸਮਾਸੂਚੀ ਅਤੇ ਅਲਰਟ")) },
                    icon = { Icon(Icons.Default.Vaccines, contentDescription = null, tint = GreenDark) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentScreen = Screen.VaccineSchedule
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(tr("🗺️ लाइव जीपीएस मैप", "🗺️ Live GPS Map", "🗺️ थेट जीपीएस नकाशा", "🗺️ લાઇવ જીપીએસ નકશો", "🗺️ ਲਾਈਵ ਜੀਪੀਐਸ ਨਕਸ਼ਾ")) },
                    icon = { Icon(Icons.Default.Map, contentDescription = null, tint = GreenDark) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentScreen = Screen.LiveMap
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(tr("पशु हेल्पलाइन 1962", "Animal Helpline (1962)", "पशू हेल्पलाइन 1962", "પશુ હેલ્પલાઇન 1962", "ਪਸ਼ੂ ਹੈਲਪਲਾਈਨ 1962")) },
                    icon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GreenDark) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentTab = "alerts"
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // Language Selection in Drawer with all 5 languages
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = GreenDark, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = when (selectedLanguage) {
                                "English" -> "Language: $selectedLanguage"
                                "मराठी" -> "भाषा: $selectedLanguage"
                                "ગુજરાતી" -> "ભાષા: $selectedLanguage"
                                "ਪੰਜਾਬੀ" -> "ਭਾਸ਼ਾ: $selectedLanguage"
                                else -> "भाषा: $selectedLanguage"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (lang in listOf("हिंदी", "English", "मराठी", "ગુજરાતી", "ਪੰਜਾਬੀ")) {
                            val isSel = lang == selectedLanguage
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) GreenDark else Color(0xFFF1F8E9),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.setLanguage(lang)
                                        scope.launch { drawerState.close() }
                                    }
                            ) {
                                Text(
                                    text = lang,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else GreenDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                NavigationDrawerItem(
                    label = { Text(tr("सहायता व अक्सर पूछे जाने वाले प्रश्न", "Help & FAQs", "मदत व वारंवार विचारले जाणारे प्रश्न", "મદદ અને વારંવાર પૂછાતા પ્રશ્નો", "ਮਦਦ ਅਤੇ ਅਕਸਰ ਪੁੱਛੇ ਜਾਂਦੇ ਸਵਾਲ")) },
                    icon = { Icon(Icons.Default.Help, contentDescription = null, tint = GreenDark) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(color = Color(0xFFEEEEEE))

                NavigationDrawerItem(
                    label = { Text(tr("लॉगआउट", "Logout", "लॉगआउट", "લૉગ આઉટ", "ਲੌਗਆਉਟ"), color = Color(0xFFD32F2F)) },
                    icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFD32F2F)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentScreen = Screen.Welcome
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                is Screen.Welcome -> {
                    WelcomeScreen(
                        onLoginClick = { currentScreen = Screen.RoleSelection },
                        onMobileLoginClick = { currentScreen = Screen.Login(UserRole.FARMER) },
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { viewModel.setLanguage(it) }
                    )
                }

                is Screen.RoleSelection -> {
                    RoleSelectionScreen(
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        isHindi = isHindi,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onBackClick = { currentScreen = Screen.Welcome },
                        onRoleSelected = { role ->
                            currentScreen = Screen.Login(role)
                        }
                    )
                }

                is Screen.Login -> {
                    LoginScreen(
                        initialRole = screen.initialRole,
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        isHindi = isHindi,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onBackClick = { currentScreen = Screen.RoleSelection },
                        onFarmerLogin = { name, mobile, village, district, pincode ->
                            viewModel.updateFarmerProfile(name, mobile, village, district, pincode)
                            currentTab = "home"
                            currentScreen = Screen.MainApp
                        },
                        onVetLogin = { email, password, regNo ->
                            viewModel.updateVetProfile(email = email, regNo = regNo)
                            currentTab = "home"
                            currentScreen = Screen.MainApp
                        },
                        onOfficerLogin = { email, password, district ->
                            viewModel.updateOfficerProfile(email = email, district = district)
                            currentTab = "dashboard"
                            currentScreen = Screen.MainApp
                        }
                    )
                }

                is Screen.Diagnosis -> {
                    CattleDiagnosisScreen(
                        cattleList = cattleList,
                        selectedCattle = selectedCattleForDiag,
                        selectedSymptoms = selectedSymptoms,
                        capturedPhoto = capturedPhoto,
                        spokenText = spokenText,
                        isHindi = isHindi,
                        selectedLanguage = selectedLanguage,
                        onBackClick = { currentScreen = Screen.MainApp },
                        onSelectCattle = { viewModel.selectCattleForDiagnosis(it) },
                        onToggleSymptom = { viewModel.toggleSymptom(it) },
                        onPhotoCaptured = { viewModel.setCapturedPhoto(it) },
                        onVoiceInputProcessed = { viewModel.processSpokenText(it) },
                        onStartDiagnosis = {
                            viewModel.performDiagnosis()
                            currentScreen = Screen.DiagnosisResult
                        }
                    )
                }

                is Screen.DiagnosisResult -> {
                    DiagnosisResultScreen(
                        result = diagnosisResult,
                        capturedPhoto = capturedPhoto,
                        isHindi = isHindi,
                        selectedLanguage = selectedLanguage,
                        isAnalyzing = isAnalyzingDiagnosis,
                        isSpeaking = isSpeaking,
                        onSpeakClick = { viewModel.speakDiagnosisAdvice() },
                        onStopSpeakingClick = { viewModel.stopSpeaking() },
                        onBackClick = { currentScreen = Screen.Diagnosis },
                        onSaveCaseClick = { viewModel.saveDiagnosisAsMedicalCase() },
                        onContactVetClick = {
                            viewModel.scheduleVetAppointment(
                                cattleTag = selectedCattleForDiag?.tagNumber ?: "G001",
                                animalType = selectedCattleForDiag?.animalType ?: (if (isHindi) "गाय" else "Cow"),
                                farmerName = if (isHindi) "राम किसान" else "Ram Kisan",
                                timeSlot = if (isHindi) "तत्काल (आपातकालीन)" else "Immediate (Emergency)",
                                reason = diagnosisResult.diseaseName
                            )
                            currentScreen = Screen.MainApp
                            currentTab = "home"
                        }
                    )
                }

                is Screen.LiveMap -> {
                    // Fullscreen Dedicated Live Interactive Map
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = tr("लाइव जीपीएस एवं पशु चिकित्सालय मैप", "Live GPS & Vet Clinics Map", "थेट जीपीएस व पशू रुग्णालय नकाशा", "લાઇવ જીપીએસ અને પશુ દવાખાના નકશો", "ਲਾਈਵ ਜੀਪੀਐਸ ਅਤੇ ਵੈਟਰਨਰੀ ਕਲੀਨਿਕ ਨਕਸ਼ਾ"),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B241C)
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = { currentScreen = Screen.MainApp }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = tr("वापस", "Back", "मागे", "પાછા", "ਵਾਪਸ"),
                                            tint = Color(0xFF1B241C)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                            )
                        }
                    ) { mapPadding ->
                        RealMapView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(mapPadding),
                            isHindi = isHindi
                        )
                    }
                }

                is Screen.VaccineSchedule -> {
                    VaccineScheduleScreen(
                        vaccinesList = vaccineList,
                        selectedLanguage = selectedLanguage,
                        onBackClick = { currentScreen = Screen.MainApp },
                        onMarkCompleted = { vaccineId ->
                            viewModel.markVaccineCompleted(vaccineId)
                        },
                        onAddNewSchedule = { vName, enName, dis, target, date, loc, dose ->
                            viewModel.addNewVaccineSchedule(vName, enName, dis, target, date, loc, dose)
                        }
                    )
                }

                is Screen.MyCattleDetail -> {
                    MyCattleScreen(
                        cattleList = cattleList,
                        selectedLanguage = selectedLanguage,
                        onBackClick = { currentScreen = Screen.MainApp },
                        onCattleClick = { cattle ->
                            viewModel.selectCattleForDiagnosis(cattle)
                            currentScreen = Screen.Diagnosis
                        },
                        onAddCattle = { tag, type, age, status, breed, notes ->
                            viewModel.addNewCattle(tag, type, age, status, breed, notes)
                        }
                    )
                }

                is Screen.CaseDetail -> {
                    val activeCase = selectedCase ?: caseList.firstOrNull() ?: com.example.data.model.MedicalCase(
                        cattleTag = "गाय – G001",
                        animalType = "गाय",
                        farmerName = "राम किसान",
                        village = "गाँव भाटी",
                        date = "15 मई 2025",
                        symptoms = "मुंह में छाले, लार आना, बुखार, खाने में कमी",
                        diagnosis = "FMD (खुरपका मुंहपका)",
                        treatment = "1. Melonex ORS\n2. टेट्रासाइक्लिन (Tetracycline)\n3. विटामिन बी-कॉम्प्लेक्स",
                        nextVisit = "18 मई 2025"
                    )
                    CaseDetailScreen(
                        medicalCase = activeCase,
                        selectedLanguage = selectedLanguage,
                        onBackClick = { currentScreen = Screen.MainApp },
                        onCallFarmerClick = {},
                        onUpdateCase = { newTreatment, nextVisit ->
                            viewModel.updateCaseTreatment(activeCase.id, newTreatment, nextVisit)
                        }
                    )
                }

                is Screen.MainApp -> {
                    val unreadAlerts = alertList.count { !it.isRead } + vaccineList.count { it.status == com.example.data.model.VaccineStatus.DUE || it.status == com.example.data.model.VaccineStatus.OVERDUE }
                    Scaffold(
                        bottomBar = {
                            PashuSetuBottomBar(
                                role = currentRole,
                                currentTab = currentTab,
                                onTabSelected = { currentTab = it },
                                selectedLanguage = selectedLanguage,
                                unreadAlertCount = unreadAlerts,
                                onCenterActionClick = {
                                    when (currentRole) {
                                        UserRole.FARMER -> currentScreen = Screen.Diagnosis
                                        UserRole.VET -> {
                                            selectedCase?.let { currentScreen = Screen.CaseDetail }
                                                ?: run {
                                                    if (caseList.isNotEmpty()) {
                                                        viewModel.selectCase(caseList.first())
                                                        currentScreen = Screen.CaseDetail
                                                    }
                                                }
                                        }
                                        UserRole.OFFICER -> currentTab = "dashboard"
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentRole) {
                                UserRole.FARMER -> {
                                    when (currentTab) {
                                        "home" -> KisanHomeScreen(
                                            cattleList = cattleList,
                                            vaccineList = vaccineList,
                                            isHindi = isHindi,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onToggleLanguage = {
                                                viewModel.setLanguage(if (isHindi) "English" else "हिंदी")
                                            },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onMyCattleClick = { currentScreen = Screen.MyCattleDetail },
                                            onStartDiagnosisClick = { currentScreen = Screen.Diagnosis },
                                            onMedicinesClick = { currentTab = "alerts" },
                                            onVaccineScheduleClick = { currentScreen = Screen.VaccineSchedule },
                                            onOpenMapClick = { currentScreen = Screen.LiveMap },
                                            onEmergencyCallClick = { currentTab = "alerts" }
                                        )
                                        "cattle" -> MyCattleScreen(
                                            cattleList = cattleList,
                                            selectedLanguage = selectedLanguage,
                                            onBackClick = { currentTab = "home" },
                                            onCattleClick = { cattle ->
                                                viewModel.selectCattleForDiagnosis(cattle)
                                                currentScreen = Screen.Diagnosis
                                            },
                                            onAddCattle = { tag, type, age, status, breed, notes ->
                                                viewModel.addNewCattle(tag, type, age, status, breed, notes)
                                            }
                                        )
                                        "alerts" -> AlertsScreen(
                                            alertList = alertList,
                                            vaccineList = vaccineList,
                                            selectedLanguage = selectedLanguage,
                                            onOpenVaccineSchedule = { currentScreen = Screen.VaccineSchedule },
                                            onMarkRead = { viewModel.markAlertAsRead(it) },
                                            onDeleteAlert = { viewModel.deleteAlert(it) },
                                            onBroadcastAlert = { t, et, d, ed, u -> viewModel.broadcastAlert(t, et, d, ed, u) }
                                        )
                                        "profile" -> ProfileScreen(
                                            currentRole = currentRole,
                                            selectedLanguage = selectedLanguage,
                                            userProfile = userProfile,
                                            onSwitchRoleClick = { currentScreen = Screen.RoleSelection },
                                            onLogoutClick = { currentScreen = Screen.Login(UserRole.FARMER) }
                                        )
                                        else -> KisanHomeScreen(
                                            cattleList = cattleList,
                                            vaccineList = vaccineList,
                                            isHindi = isHindi,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            farmerName = userProfile.name,
                                            farmerVillage = "${userProfile.address}, ${userProfile.district}",
                                            onToggleLanguage = {
                                                viewModel.setLanguage(if (isHindi) "English" else "हिंदी")
                                            },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onMyCattleClick = { currentScreen = Screen.MyCattleDetail },
                                            onStartDiagnosisClick = { currentScreen = Screen.Diagnosis },
                                            onMedicinesClick = { currentTab = "alerts" },
                                            onVaccineScheduleClick = { currentScreen = Screen.VaccineSchedule },
                                            onOpenMapClick = { currentScreen = Screen.LiveMap },
                                            onEmergencyCallClick = { currentTab = "alerts" }
                                        )
                                    }
                                }

                                UserRole.VET -> {
                                    when (currentTab) {
                                        "home" -> VetDoctorHomeScreen(
                                            appointments = appointmentList,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onAppointmentClick = { appt ->
                                                val matched = caseList.firstOrNull { it.cattleTag.contains(appt.cattleTag) }
                                                    ?: caseList.firstOrNull()
                                                if (matched != null) {
                                                    viewModel.selectCase(matched)
                                                    currentScreen = Screen.CaseDetail
                                                }
                                            },
                                            onViewAllAppointments = {
                                                if (caseList.isNotEmpty()) {
                                                    viewModel.selectCase(caseList.first())
                                                    currentScreen = Screen.CaseDetail
                                                }
                                            }
                                        )
                                        "cases" -> {
                                            if (caseList.isNotEmpty()) {
                                                CaseDetailScreen(
                                                    medicalCase = selectedCase ?: caseList.first(),
                                                    selectedLanguage = selectedLanguage,
                                                    onBackClick = { currentTab = "home" },
                                                    onCallFarmerClick = {},
                                                    onUpdateCase = { newTreatment, nextVisit ->
                                                        selectedCase?.let {
                                                             viewModel.updateCaseTreatment(it.id, newTreatment, nextVisit)
                                                        }
                                                    }
                                                )
                                            } else {
                                                VetDoctorHomeScreen(
                                                    appointments = appointmentList,
                                                    selectedLanguage = selectedLanguage,
                                                    onLanguageChange = { viewModel.setLanguage(it) },
                                                    onOpenDrawer = { scope.launch { drawerState.open() } },
                                                    onNotificationsClick = { currentTab = "alerts" },
                                                    onAppointmentClick = {},
                                                    onViewAllAppointments = {}
                                                )
                                            }
                                        }
                                        "medicines" -> MedicinesScreen(
                                            medicineList = medicineList,
                                            selectedLanguage = selectedLanguage
                                        )
                                        "profile" -> ProfileScreen(
                                            currentRole = currentRole,
                                            selectedLanguage = selectedLanguage,
                                            userProfile = userProfile,
                                            onSwitchRoleClick = { currentScreen = Screen.RoleSelection },
                                            onLogoutClick = { currentScreen = Screen.Login(UserRole.VET) }
                                        )
                                        else -> VetDoctorHomeScreen(
                                            appointments = appointmentList,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onAppointmentClick = {},
                                            onViewAllAppointments = {}
                                        )
                                    }
                                }

                                UserRole.OFFICER -> {
                                    when (currentTab) {
                                        "dashboard" -> DistrictOfficerDashboardScreen(
                                            summary = districtSummary,
                                            isHindi = isHindi,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onToggleLanguage = {
                                                viewModel.setLanguage(if (isHindi) "English" else "हिंदी")
                                            },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onDistrictChange = { viewModel.selectDistrict(it) }
                                        )
                                        "reports" -> DistrictOfficerDashboardScreen(
                                            summary = districtSummary,
                                            isHindi = isHindi,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onToggleLanguage = {
                                                viewModel.setLanguage(if (isHindi) "English" else "हिंदी")
                                            },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onDistrictChange = { viewModel.selectDistrict(it) }
                                        )
                                        "alerts" -> AlertsScreen(
                                            alertList = alertList,
                                            vaccineList = vaccineList,
                                            selectedLanguage = selectedLanguage,
                                            onOpenVaccineSchedule = { currentScreen = Screen.VaccineSchedule },
                                            onMarkRead = { viewModel.markAlertAsRead(it) },
                                            onDeleteAlert = { viewModel.deleteAlert(it) },
                                            onBroadcastAlert = { t, et, d, ed, u -> viewModel.broadcastAlert(t, et, d, ed, u) }
                                        )
                                        "more" -> ProfileScreen(
                                            currentRole = currentRole,
                                            selectedLanguage = selectedLanguage,
                                            userProfile = userProfile,
                                            onSwitchRoleClick = { currentScreen = Screen.RoleSelection },
                                            onLogoutClick = { currentScreen = Screen.Login(UserRole.OFFICER) }
                                        )
                                        else -> DistrictOfficerDashboardScreen(
                                            summary = districtSummary,
                                            isHindi = isHindi,
                                            selectedLanguage = selectedLanguage,
                                            onLanguageChange = { viewModel.setLanguage(it) },
                                            onToggleLanguage = {
                                                viewModel.setLanguage(if (isHindi) "English" else "हिंदी")
                                            },
                                            onOpenDrawer = { scope.launch { drawerState.open() } },
                                            onNotificationsClick = { currentTab = "alerts" },
                                            onDistrictChange = { viewModel.selectDistrict(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
