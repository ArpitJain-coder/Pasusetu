package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import androidx.compose.material3.MaterialTheme
import com.example.ui.theme.BlueVet
import com.example.ui.theme.BlueVetContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.PurpleOfficer
import com.example.ui.theme.PurpleOfficerContainer
import com.example.ui.theme.StatusHealthy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.appTextFieldColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    initialRole: UserRole = UserRole.FARMER,
    selectedLanguage: String = "हिंदी",
    onLanguageChange: (String) -> Unit = {},
    isHindi: Boolean = selectedLanguage == "हिंदी",
    onToggleLanguage: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onFarmerLogin: (name: String, mobile: String, village: String, district: String, pincode: String) -> Unit,
    onVetLogin: (email: String, password: String, regNo: String) -> Unit,
    onOfficerLogin: (email: String, password: String, district: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf(initialRole) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var langMenuExpanded by remember { mutableStateOf(false) }

    fun tr(hi: String, en: String, mr: String = hi, gu: String = hi, pa: String = hi): String =
        when (selectedLanguage) {
            "English" -> en
            "मराठी" -> mr
            "ગુજરાતી" -> gu
            "ਪੰਜਾਬੀ" -> pa
            else -> hi
        }

    // --- Farmer Form State ---
    var farmerMobile by remember { mutableStateOf("9876543210") }
    var farmerName by remember { mutableStateOf("राम किसान") }
    var farmerVillage by remember { mutableStateOf("गाँव भाटी, कोटपूतली") }
    var farmerDistrict by remember { mutableStateOf("जयपुर") }
    var farmerPincode by remember { mutableStateOf("303108") }
    var farmerOtp by remember { mutableStateOf("1962") }
    var isOtpSent by remember { mutableStateOf(true) }
    var otpCooldown by remember { mutableIntStateOf(45) }
    var districtMenuExpanded by remember { mutableStateOf(false) }
    var farmerErrorMessage by remember { mutableStateOf<String?>(null) }

    // --- Vet Form State ---
    var vetEmail by remember { mutableStateOf("dr.rajesh.vet@rajasthan.gov.in") }
    var vetPassword by remember { mutableStateOf("PashuVet@2025") }
    var vetRegNo by remember { mutableStateOf("RVC-2022-4102") }
    var vetPasswordVisible by remember { mutableStateOf(false) }
    var vetErrorMessage by remember { mutableStateOf<String?>(null) }

    // --- District Officer Form State ---
    var officerEmail by remember { mutableStateOf("officer.ahd.jaipur@rajasthan.gov.in") }
    var officerPassword by remember { mutableStateOf("RajGovOfficer@2025") }
    var officerDistrict by remember { mutableStateOf("जयपुर") }
    var officerPasswordVisible by remember { mutableStateOf(false) }
    var officerDistrictMenuExpanded by remember { mutableStateOf(false) }
    var officerErrorMessage by remember { mutableStateOf<String?>(null) }

    val districts = listOf("जयपुर", "जोधपुर", "उदयपुर", "अलवर", "अजमेर", "बीकानेर", "कोटा", "सीकर")

    // Theme color based on role
    val activeColor = when (selectedRole) {
        UserRole.FARMER -> GreenDark
        UserRole.VET -> BlueVet
        UserRole.OFFICER -> PurpleOfficer
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            TopAppBar(
                title = {
                    Text(
                        text = tr("पोर्टल लॉगिन", "Portal Login", "पोर्टल लॉगिन", "પોર્ટલ લૉગિન", "ਪੋਰਟਲ ਲਾਗਇਨ"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Box {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier
                                .clickable { langMenuExpanded = true }
                                .padding(end = 8.dp)
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
                            com.example.ui.util.AppStrings.SUPPORTED_LANGUAGES.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = lang,
                                            fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                                            color = if (lang == selectedLanguage) GreenDark else TextPrimary
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
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Role Selector Segmented Tabs
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Farmer Tab
                        RoleTabButton(
                            title = tr("🌾 किसान", "🌾 Farmer", "🌾 शेतकरी", "🌾 ખેડૂત", "🌾 ਕਿਸਾਨ"),
                            isSelected = selectedRole == UserRole.FARMER,
                            selectedColor = GreenDark,
                            onClick = { selectedRole = UserRole.FARMER },
                            modifier = Modifier.weight(1f)
                        )

                        // Vet Tab
                        RoleTabButton(
                            title = tr("🩺 डॉक्टर", "🩺 Vet", "🩺 डॉक्टर", "🩺 ડૉક્ટર", "🩺 ਡਾਕਟਰ"),
                            isSelected = selectedRole == UserRole.VET,
                            selectedColor = BlueVet,
                            onClick = { selectedRole = UserRole.VET },
                            modifier = Modifier.weight(1f)
                        )

                        // Officer Tab
                        RoleTabButton(
                            title = tr("🏛️ अधिकारी", "🏛️ Officer", "🏛️ अधिकारी", "🏛️ અધિકારી", "🏛️ ਅਧਿਕਾਰੀ"),
                            isSelected = selectedRole == UserRole.OFFICER,
                            selectedColor = PurpleOfficer,
                            onClick = { selectedRole = UserRole.OFFICER },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animated Form Container based on selected role
                AnimatedContent(
                    targetState = selectedRole,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "RoleFormTransition"
                ) { role ->
                    when (role) {
                        // ==========================================
                        // 1. FARMER LOGIN: Mobile + OTP + Address
                        // ==========================================
                        UserRole.FARMER -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tr("किसान लॉगिन एवं सत्यापन", "Farmer Login & Verification", "शेतकरी लॉगिन व पडताळणी", "ખેડૂત લૉગિન અને ચકાસણી", "ਕਿਸਾਨ ਲਾਗਇਨ ਅਤੇ ਤਸਦੀਕ"),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GreenDark
                                            )
                                            Text(
                                                text = tr("मोबाइल नंबर, OTP एवं पते के विवरण से प्रवेश करें", "Sign in with mobile number, OTP and village address", "मोबाईल क्रमांक, OTP आणि पत्त्याच्या तपशिलासह प्रवेश करा", "મોબાઇલ નંબર, OTP અને સરનામાની વિગતો સાથે પ્રવેશ કરો", "ਮੋਬਾਈਲ ਨੰਬਰ, OTP ਅਤੇ ਪਤੇ ਦੇ ਵੇਰਵਿਆਂ ਨਾਲ ਦਾਖਲ ਹੋਵੋ"),
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        // Quick demo autofill chip
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFE8F5E9),
                                            border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f)),
                                            modifier = Modifier.clickable {
                                                farmerMobile = "9876543210"
                                                farmerName = "राम किसान"
                                                farmerVillage = "गाँव भाटी, कोटपूतली"
                                                farmerDistrict = "जयपुर"
                                                farmerPincode = "303108"
                                                farmerOtp = "1962"
                                                isOtpSent = true
                                                farmerErrorMessage = null
                                            }
                                        ) {
                                            Text(
                                                text = tr("⚡ डेमो भरें", "⚡ Demo", "⚡ डेमो भरा", "⚡ ડેમો ભરો", "⚡ ਡੈਮੋ ਭਰੋ"),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GreenDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Error Message if any
                                    if (farmerErrorMessage != null) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFEBEE),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = farmerErrorMessage ?: "",
                                                color = Color(0xFFD32F2F),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }

                                    // 1. Mobile Number Field
                                    Column {
                                        Text(
                                            text = tr("मोबाइल नंबर *", "Mobile Number *", "मोबाईल क्रमांक *", "મોબાઇલ નંબર *", "ਮੋਬਾਈਲ ਨੰਬਰ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = farmerMobile,
                                            onValueChange = {
                                                if (it.length <= 10) farmerMobile = it.filter { char -> char.isDigit() }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                                                ) {
                                                    Icon(Icons.Default.Phone, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("+91", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                                }
                                            },
                                            trailingIcon = {
                                                Button(
                                                    onClick = {
                                                        if (farmerMobile.length == 10) {
                                                            isOtpSent = true
                                                            farmerOtp = "1962"
                                                            farmerErrorMessage = null
                                                        } else {
                                                            farmerErrorMessage = tr(
                                                                "कृपया 10 अंकों का वैध मोबाइल नंबर दर्ज करें",
                                                                "Please enter a valid 10-digit mobile number",
                                                                "कृपया 10 अंकी वैध मोबाईल क्रमांक प्रविष्ट करा",
                                                                "કૃપા કરીને 10 અંકનો માન્ય મોબાઇલ નંબર દાખલ કરો",
                                                                "ਕਿਰਪਾ ਕਰਕੇ 10 ਅੰਕਾਂ ਦਾ ਵੈਧ ਮੋਬਾਈਲ ਨੰਬਰ ਦਰਜ ਕਰੋ"
                                                            )
                                                        }
                                                    },
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                    modifier = Modifier.padding(end = 6.dp)
                                                ) {
                                                    Text(
                                                        text = if (isOtpSent)
                                                            tr("पुनः भेजें", "Resend", "पुन्हा पाठवा", "ફરી મોકલો", "ਦੁਬਾਰਾ ਭੇਜੋ")
                                                        else
                                                            tr("OTP भेजें", "Get OTP", "OTP पाठवा", "OTP મોકલો", "OTP ਭੇਜੋ"),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            },
                                            placeholder = { Text(tr("10 अंकों का मोबाइल नंबर", "10-digit mobile number", "10 अंकी मोबाईल क्रमांक", "10 અંકનો મોબાઇલ નંબર", "10 ਅੰਕਾਂ ਦਾ ਮੋਬਾਈਲ ਨੰਬਰ")) },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = GreenDark)
                                        )
                                    }

                                    // 2. OTP Field (One Time Password)
                                    AnimatedVisibility(visible = isOtpSent) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF1F8E9), RoundedCornerShape(10.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusHealthy, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = tr("OTP भेजा गया: 1962", "OTP Sent: 1962", "OTP पाठवला: 1962", "OTP મોકલ્યો: 1962", "OTP ਭੇਜਿਆ: 1962"),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = StatusHealthy
                                                    )
                                                }
                                                Text(
                                                    text = tr("मान्य: ${otpCooldown}s", "Expires: ${otpCooldown}s", "वैध: ${otpCooldown}s", "માન્ય: ${otpCooldown}s", "ਮਿਆਦ: ${otpCooldown}s"),
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            OutlinedTextField(
                                                value = farmerOtp,
                                                onValueChange = { if (it.length <= 6) farmerOtp = it },
                                                modifier = Modifier.fillMaxWidth(),
                                                leadingIcon = {
                                                    Icon(Icons.Default.Key, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                                },
                                                trailingIcon = {
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = Color(0xFFDCEDC8),
                                                        modifier = Modifier
                                                            .clickable { farmerOtp = "1962" }
                                                            .padding(end = 8.dp)
                                                     ) {
                                                        Text(
                                                            text = tr("ऑटो-फिल 1962", "Auto-fill 1962", "ऑटो-फिल 1962", "ઓટો-ફિલ 1962", "ਆਟੋ-ਫਿਲ 1962"),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = GreenDark,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                },
                                                placeholder = { Text(tr("4 या 6 अंकों का OTP", "Enter 4 or 6-digit OTP", "4 किंवा 6 अंकी OTP", "4 અથવા 6 અંકનો OTP", "4 ਜਾਂ 6 ਅੰਕਾਂ ਦਾ OTP")) },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                                                shape = RoundedCornerShape(8.dp),
                                                singleLine = true,
                                                colors = appTextFieldColors(focusedBorder = GreenDark)
                                            )
                                        }
                                    }

                                    // 3. Farmer Name
                                    Column {
                                        Text(
                                            text = tr("किसान का पूरा नाम *", "Farmer's Full Name *", "शेतकऱ्याचे पूर्ण नाव *", "ખેડૂતનું પૂરું નામ *", "ਕਿਸਾਨ ਦਾ ਪੂਰਾ ਨਾਮ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = farmerName,
                                            onValueChange = { farmerName = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Person, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                            },
                                            placeholder = { Text(tr("जैसे: राम किसान / रमेश यादव", "e.g. Ramesh Kumar", "उदा. राम शेतकरी", "દા.ત. રમેશ પટેલ", "ਜਿਵੇਂ: ਰਾਮ ਸਿੰਘ")) },
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = GreenDark)
                                        )
                                    }

                                    // 4. Address Details (Village & Locality)
                                    Column {
                                        Text(
                                            text = tr("गाँव / ढाणी / पता (Address) *", "Village / Street Address *", "गाव / पत्ता *", "ગામ / સરનામું *", "ਪਿੰਡ / ਪਤਾ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = farmerVillage,
                                            onValueChange = { farmerVillage = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Home, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                            },
                                            placeholder = { Text(tr("गाँव भाटी, शाहपुरा, कोटपूतली", "Village & Postal Address", "गाव आणि पत्ता", "ગામ અને સરનામું", "ਪਿੰਡ ਅਤੇ ਪਤਾ")) },
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = GreenDark)
                                        )
                                    }

                                    // District & Pincode Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // District Dropdown
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tr("जिला *", "District *", "जिल्हा *", "જિલ્લો *", "ਜ਼ਿਲ੍ਹਾ *"),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box {
                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    border = BorderStroke(1.dp, BorderLight),
                                                    color = MaterialTheme.colorScheme.surface,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(54.dp)
                                                        .clickable { districtMenuExpanded = true }
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(horizontal = 12.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.LocationCity, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(farmerDistrict, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                                        }
                                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                                                    }
                                                }

                                                DropdownMenu(
                                                    expanded = districtMenuExpanded,
                                                    onDismissRequest = { districtMenuExpanded = false }
                                                ) {
                                                    districts.forEach { dist ->
                                                        DropdownMenuItem(
                                                            text = { Text(dist, color = TextPrimary) },
                                                            onClick = {
                                                                farmerDistrict = dist
                                                                districtMenuExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Pincode
                                        Column(modifier = Modifier.weight(0.9f)) {
                                            Text(
                                                text = tr("पिन कोड", "Pincode", "पिन कोड", "પિન કોડ", "ਪਿੰਨ ਕੋਡ"),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            OutlinedTextField(
                                                value = farmerPincode,
                                                onValueChange = { if (it.length <= 6) farmerPincode = it.filter { c -> c.isDigit() } },
                                                modifier = Modifier.fillMaxWidth(),
                                                leadingIcon = {
                                                    Icon(Icons.Default.PinDrop, contentDescription = null, tint = GreenDark, modifier = Modifier.size(18.dp))
                                                },
                                                placeholder = { Text("303108") },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                                shape = RoundedCornerShape(10.dp),
                                                singleLine = true,
                                                colors = appTextFieldColors(focusedBorder = GreenDark)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Submit Button
                                    Button(
                                        onClick = {
                                            if (farmerMobile.length < 10) {
                                                farmerErrorMessage = tr(
                                                    "कृपया वैध 10 अंकों का मोबाइल नंबर दर्ज करें",
                                                    "Enter valid 10-digit mobile number",
                                                    "कृपया 10 अंकी वैध मोबाईल क्रमांक प्रविष्ट करा",
                                                    "કૃપા કરીને 10 અંકનો માન્ય મોબાઇલ નંબર દાખલ કરો",
                                                    "ਕਿਰਪਾ ਕਰਕੇ 10 ਅੰਕਾਂ ਦਾ ਵੈਧ ਮੋਬਾਈਲ ਨੰਬਰ ਦਰਜ ਕਰੋ"
                                                )
                                                return@Button
                                            }
                                            if (farmerOtp.isBlank()) {
                                                farmerErrorMessage = tr(
                                                    "कृपया OTP दर्ज करें",
                                                    "Please enter OTP",
                                                    "कृपया OTP प्रविष्ट करा",
                                                    "કૃપા કરીને OTP દાખલ કરો",
                                                    "ਕਿਰਪਾ ਕਰਕੇ OTP ਦਰਜ ਕਰੋ"
                                                )
                                                return@Button
                                            }
                                            if (farmerVillage.isBlank()) {
                                                farmerErrorMessage = tr(
                                                    "कृपया गाँव व पता दर्ज करें",
                                                    "Please enter village and address",
                                                    "कृपया गाव आणि पत्ता प्रविष्ट करा",
                                                    "કૃપા કરીને ગામ અને સરનામું દાખલ કરો",
                                                    "ਕਿਰਪਾ ਕਰਕੇ ਪਿੰਡ ਅਤੇ ਪਤਾ ਦਰਜ ਕਰੋ"
                                                )
                                                return@Button
                                            }
                                            isLoading = true
                                            coroutineScope.launch {
                                                delay(400)
                                                isLoading = false
                                                onFarmerLogin(farmerName, farmerMobile, farmerVillage, farmerDistrict, farmerPincode)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                                        enabled = !isLoading
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = tr("सत्यापित करें और प्रवेश करें", "Verify OTP & Sign In", "पडताळणी करा आणि प्रवेश करा", "ચકાસો અને પ્રવેશ કરો", "ਤਸਦੀਕ ਕਰੋ ਅਤੇ ਦਾਖਲ ਹੋਵੋ"),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==========================================
                        // 2. VET LOGIN: Email + Password + Reg No.
                        // ==========================================
                        UserRole.VET -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tr("पशु चिकित्सक पोर्टल लॉगिन", "Veterinary Doctor Portal Login", "पशुवैद्यकीय डॉक्टर पोर्टल लॉगिन", "પશુ ચિકિત્સક પોર્ટલ લૉગિન", "ਪਸ਼ੂ ਡਾਕਟਰ ਪੋਰਟਲ ਲਾਗਇਨ"),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BlueVet
                                            )
                                            Text(
                                                text = tr("पंजीकृत ईमेल व पासवर्ड से क्लिनिकल पोर्टल खोलें", "Sign in with institutional email & password", "नोंदणीकृत ईमेल व पासवर्डने क्लिनिकल पोर्टल उघडा", "નોંધાયેલ ઇમેઇલ અને પાસવર્ડ વડે ક્લિનિકલ પોર્ટલ ખોલો", "ਰਜਿਸਟਰਡ ਈਮੇਲ ਅਤੇ ਪਾਸਵਰਡ ਨਾਲ ਕਲੀਨਿਕਲ ਪੋਰਟਲ ਖੋਲ੍ਹੋ"),
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        // Quick demo autofill chip
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = BlueVetContainer,
                                            border = BorderStroke(1.dp, BlueVet.copy(alpha = 0.4f)),
                                            modifier = Modifier.clickable {
                                                vetEmail = "dr.rajesh.vet@rajasthan.gov.in"
                                                vetPassword = "PashuVet@2025"
                                                vetRegNo = "RVC-2022-4102"
                                                vetErrorMessage = null
                                            }
                                        ) {
                                            Text(
                                                text = tr("⚡ डेमो डॉक्टर", "⚡ Demo Vet", "⚡ डेमो डॉक्टर", "⚡ ડેમો ડૉક્ટર", "⚡ ਡੈਮੋ ਡਾਕਟਰ"),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BlueVet,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Error Message if any
                                    if (vetErrorMessage != null) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFEBEE),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = vetErrorMessage ?: "",
                                                color = Color(0xFFD32F2F),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }

                                    // 1. Email Field
                                    Column {
                                        Text(
                                            text = tr("चिकित्सक ईमेल (Veterinary Email) *", "Doctor's Email *", "डॉक्टर ईमेल *", "ડૉક્ટર ઇમેઇલ *", "ਡਾਕਟਰ ਈਮੇਲ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = vetEmail,
                                            onValueChange = { vetEmail = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Email, contentDescription = null, tint = BlueVet, modifier = Modifier.size(18.dp))
                                            },
                                            placeholder = { Text("dr.rajesh.vet@rajasthan.gov.in") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = BlueVet)
                                        )
                                    }

                                    // 2. Password Field
                                    Column {
                                        Text(
                                            text = tr("पासवर्ड (Password) *", "Password *", "पासवर्ड *", "પાસવર્ડ *", "ਪਾਸਵਰਡ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = vetPassword,
                                            onValueChange = { vetPassword = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Lock, contentDescription = null, tint = BlueVet, modifier = Modifier.size(18.dp))
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = { vetPasswordVisible = !vetPasswordVisible }) {
                                                    Icon(
                                                        imageVector = if (vetPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = "Toggle password visibility",
                                                        tint = TextSecondary
                                                    )
                                                }
                                            },
                                            placeholder = { Text("••••••••") },
                                            visualTransformation = if (vetPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = BlueVet)
                                        )
                                    }

                                    // 3. Veterinary Registration / Council Number
                                    Column {
                                        Text(
                                            text = tr("पशु चिकित्सा परिषद पंजीयन संख्या (VCI / RVC Reg No.)", "Veterinary Council Reg No.", "पशुवैद्यकीय परिषद नोंदणी क्रमांक", "પશુ ચિકિત્સા પરિષદ નોંધણી નંબર", "ਪਸ਼ੂ ਚਿਕਿਤਸਾ ਕੌਂਸਲ ਰਜਿਸਟ੍ਰੇਸ਼ਨ ਨੰਬਰ"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = vetRegNo,
                                            onValueChange = { vetRegNo = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Badge, contentDescription = null, tint = BlueVet, modifier = Modifier.size(18.dp))
                                            },
                                            placeholder = { Text("RVC-2022-4102") },
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = BlueVet)
                                        )
                                    }

                                    // Forgot password text button
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { /* Simulated helper */ }) {
                                            Text(
                                                text = tr("पासवर्ड भूल गए?", "Forgot Password?", "पासवर्ड विसरलात?", "પાસવર્ડ ભૂલી ગયા?", "ਪਾਸਵਰਡ ਭੁੱਲ ਗਏ?"),
                                                fontSize = 12.sp,
                                                color = BlueVet
                                            )
                                        }
                                    }

                                    // Submit Button
                                    Button(
                                        onClick = {
                                            if (vetEmail.isBlank()) {
                                                vetErrorMessage = tr("कृपया ईमेल दर्ज करें", "Please enter email", "कृपया ईमेल प्रविष्ट करा", "કૃપા કરીને ઇમેઇલ દાખલ કરો", "ਕਿਰਪਾ ਕਰਕੇ ਈਮੇਲ ਦਰਜ ਕਰੋ")
                                                return@Button
                                            }
                                            if (vetPassword.isBlank()) {
                                                vetErrorMessage = tr("कृपया पासवर्ड दर्ज करें", "Please enter password", "कृपया पासवर्ड प्रविष्ट करा", "કૃપા કરીને પાસવર્ડ દાખલ કરો", "ਕਿਰਪਾ ਕਰਕੇ ਪਾਸਵਰਡ ਦਰਜ ਕਰੋ")
                                                return@Button
                                            }
                                            isLoading = true
                                            coroutineScope.launch {
                                                delay(400)
                                                isLoading = false
                                                onVetLogin(vetEmail, vetPassword, vetRegNo)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BlueVet),
                                        enabled = !isLoading
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = tr("डॉक्टर पोर्टल में प्रवेश करें", "Login to Vet Portal", "डॉक्टर पोर्टलमध्ये प्रवेश करा", "ડૉક્ટર પોર્ટલમાં પ્રવેશ કરો", "ਡਾਕਟਰ ਪੋਰਟਲ ਵਿੱਚ ਦਾਖਲ ਹੋਵੋ"),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==========================================
                        // 3. DISTRICT OFFICER LOGIN: Email + Password
                        // ==========================================
                        UserRole.OFFICER -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tr("जिला पशुपालन प्रशासनिक पोर्टल", "District Animal Health Portal", "जिल्हा पशुसंवर्धन प्रशासकीय पोर्टल", "જિલ્લા પશુપાલન વહીવટી પોર્ટલ", "ਜ਼ਿਲ੍ਹਾ ਪਸ਼ੂ ਪਾਲਣ ਪ੍ਰਸ਼ਾਸਕੀ ਪੋਰਟਲ"),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PurpleOfficer
                                            )
                                            Text(
                                                text = tr("विभागीय सरकारी ईमेल व पासवर्ड द्वारा प्रवेश करें", "Sign in with department govt email & password", "विभागीय शासकीय ईमेल व पासवर्डने प्रवेश करा", "વિભાગીય સરકારી ઇમેઇલ અને પાસવર્ડ દ્વારા પ્રવેશ કરો", "ਵਿਭਾਗੀ ਸਰਕਾਰੀ ਈਮੇਲ ਅਤੇ ਪਾਸਵਰਡ ਰਾਹੀਂ ਦਾਖਲ ਹੋਵੋ"),
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        // Quick demo autofill chip
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = PurpleOfficerContainer,
                                            border = BorderStroke(1.dp, PurpleOfficer.copy(alpha = 0.4f)),
                                            modifier = Modifier.clickable {
                                                officerEmail = "officer.ahd.jaipur@rajasthan.gov.in"
                                                officerPassword = "RajGovOfficer@2025"
                                                officerDistrict = "जयपुर"
                                                officerErrorMessage = null
                                            }
                                        ) {
                                            Text(
                                                text = tr("⚡ डेमो अधिकारी", "⚡ Demo Officer", "⚡ डेमो अधिकारी", "⚡ ડેમો અધિકારી", "⚡ ਡੈਮੋ ਅਧਿਕਾਰੀ"),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PurpleOfficer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Error Message if any
                                    if (officerErrorMessage != null) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFFEBEE),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = officerErrorMessage ?: "",
                                                color = Color(0xFFD32F2F),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }

                                    // 1. Govt Email Field
                                    Column {
                                        Text(
                                            text = tr("विभागीय सरकारी ईमेल (Govt Email) *", "Department Govt Email *", "विभागीय शासकीय ईमेल *", "વિભાગીય સરકારી ઇમેઇલ *", "ਵਿਭਾਗੀ ਸਰਕਾਰੀ ਈਮੇਲ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = officerEmail,
                                            onValueChange = { officerEmail = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Email, contentDescription = null, tint = PurpleOfficer, modifier = Modifier.size(18.dp))
                                            },
                                            placeholder = { Text("officer.ahd.jaipur@rajasthan.gov.in") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = PurpleOfficer)
                                        )
                                    }

                                    // 2. Password Field
                                    Column {
                                        Text(
                                            text = tr("पासवर्ड (Password) *", "Password *", "पासवर्ड *", "પાસવર્ડ *", "ਪਾਸਵਰਡ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        OutlinedTextField(
                                            value = officerPassword,
                                            onValueChange = { officerPassword = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            leadingIcon = {
                                                Icon(Icons.Default.Lock, contentDescription = null, tint = PurpleOfficer, modifier = Modifier.size(18.dp))
                                            },
                                            trailingIcon = {
                                                IconButton(onClick = { officerPasswordVisible = !officerPasswordVisible }) {
                                                    Icon(
                                                        imageVector = if (officerPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = "Toggle password visibility",
                                                        tint = TextSecondary
                                                    )
                                                }
                                            },
                                            placeholder = { Text("••••••••") },
                                            visualTransformation = if (officerPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                                            shape = RoundedCornerShape(10.dp),
                                            singleLine = true,
                                            colors = appTextFieldColors(focusedBorder = PurpleOfficer)
                                        )
                                    }

                                    // 3. District Jurisdiction Selection
                                    Column {
                                        Text(
                                            text = tr("कार्यक्षेत्र जिला / संभाग *", "Assigned District / Division *", "कार्यक्षेत्र जिल्हा / विभाग *", "કાર્યક્ષેત્ર જિલ્લો / વિભાગ *", "ਕਾਰਜ ਖੇਤਰ ਜ਼ਿਲ੍ਹਾ / ਮੰਡਲ *"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box {
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                border = BorderStroke(1.dp, BorderLight),
                                                color = MaterialTheme.colorScheme.surface,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(54.dp)
                                                    .clickable { officerDistrictMenuExpanded = true }
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(horizontal = 12.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.LocationCity, contentDescription = null, tint = PurpleOfficer, modifier = Modifier.size(18.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(officerDistrict, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                                    }
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                                                }
                                            }

                                            DropdownMenu(
                                                expanded = officerDistrictMenuExpanded,
                                                onDismissRequest = { officerDistrictMenuExpanded = false }
                                            ) {
                                                districts.forEach { dist ->
                                                    DropdownMenuItem(
                                                        text = { Text(dist, color = TextPrimary) },
                                                        onClick = {
                                                            officerDistrict = dist
                                                            officerDistrictMenuExpanded = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Security Assurance badge
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PurpleOfficerContainer.copy(alpha = 0.6f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Security, contentDescription = null, tint = PurpleOfficer, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = tr("सरकारी SSO प्रमाणीकरण व डेटा सुरक्षा एन्क्रिप्टेड", "Govt SSO & 256-bit Encrypted Animal Health Registry", "शासकीय SSO प्रमाणीकरण व डेटा सुरक्षा एन्क्रिप्टेड", "સરકારી SSO પ્રમાણીકરણ અને ડેટા સુરક્ષા એન્ક્રિપ્ટેડ", "ਸਰਕਾਰੀ SSO ਪ੍ਰਮਾਣੀਕਰਨ ਅਤੇ ਡਾਟਾ ਸੁਰੱਖਿਆ ਇਨਕ੍ਰਿਪਟਡ"),
                                                fontSize = 11.sp,
                                                color = PurpleOfficer,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    // Submit Button
                                    Button(
                                        onClick = {
                                            if (officerEmail.isBlank()) {
                                                officerErrorMessage = tr("कृपया सरकारी ईमेल दर्ज करें", "Please enter govt email", "कृपया शासकीय ईमेल प्रविष्ट करा", "કૃપા કરીને સરકારી ઇમેઇલ દાખલ કરો", "ਕਿਰਪਾ ਕਰਕੇ ਸਰਕਾਰੀ ਈਮੇਲ ਦਰਜ ਕਰੋ")
                                                return@Button
                                            }
                                            if (officerPassword.isBlank()) {
                                                officerErrorMessage = tr("कृपया पासवर्ड दर्ज करें", "Please enter password", "कृपया पासवर्ड प्रविष्ट करा", "કૃપા કરીને પાસવર્ડ દાખલ કરો", "ਕਿਰਪਾ ਕਰਕੇ ਪਾਸਵਰਡ ਦਰਜ ਕਰੋ")
                                                return@Button
                                            }
                                            isLoading = true
                                            coroutineScope.launch {
                                                delay(400)
                                                isLoading = false
                                                onOfficerLogin(officerEmail, officerPassword, officerDistrict)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PurpleOfficer),
                                        enabled = !isLoading
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = tr("प्रशासनिक डैशबोर्ड खोलें", "Access Officer Dashboard", "प्रशासकीय डॅशबोर्ड उघडा", "વહીવટી ડેશબોર્ડ ખોલો", "ਪ੍ਰਸ਼ਾਸਕੀ ਡੈਸ਼ਬੋਰਡ ਖੋਲ੍ਹੋ"),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Help & Toll-free Helpline
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = GreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr("सहायता हेतु कॉल करें: 1962 (पशु चिकित्सा एम्बुलेंस)", "Helpdesk: 1962 (Livestock Ambulance)", "मदतीसाठी कॉल करा: 1962 (पशुवैद्यकीय रुग्णवाहिका)", "મદદ માટે કૉલ કરો: 1962 (પશુ ચિકિત્સા એમ્બ્યુલન્સ)", "ਮਦਦ ਲਈ ਕਾਲ ਕਰੋ: 1962 (ਪਸ਼ੂ ਚਿਕਿਤਸਾ ਐਂਬੂਲੈਂਸ)"),
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleTabButton(
    title: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) selectedColor else Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextPrimary
            )
        }
    }
}
