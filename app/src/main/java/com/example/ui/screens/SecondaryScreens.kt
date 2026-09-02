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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.data.model.UserRole
import com.example.ui.components.RoleAvatar
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusSick

data class AlertItem(
    val title: String,
    val description: String,
    val time: String,
    val isUrgent: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    selectedLanguage: String = "हिंदी",
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val alerts = listOf(
        AlertItem(
            title = tr("FMD (खुरपका मुंहपका) अलर्ट", "FMD (Foot & Mouth) Alert", "FMD (लाळ्या खुरकूत) अलर्ट", "FMD (ખરવા-મોવાસા) ચેતવણી", "FMD (ਮੂੰਹ-ਖੁਰ) ਅਲਰਟ"),
            description = tr(
                "कोटपूतली व शाहपुरा क्षेत्र में 32 पशुओं में लक्षण पाए गए हैं। अपने पशुओं को स्वच्छ पानी दें और लक्षण दिखते ही अलग रखें।",
                "Symptoms detected in 32 animals in nearby area. Provide clean water and isolate symptomatic cattle immediately.",
                "जवळपासच्या भागात ३२ जनावरांमध्ये लक्षणे आढळली आहेत. स्वच्छ पाणी द्या व लक्षणे दिसताच वेगळे ठेवा.",
                "નજીકના વિસ્તારમાં 32 પશુઓમાં લક્ષણો જોવા મળ્યા છે. સ્વચ્છ પાણી આપો અને લક્ષણો દેખાતા જ અલગ રાખો.",
                "ਨੇੜਲੇ ਇਲਾਕੇ ਵਿੱਚ 32 ਪਸ਼ੂਆਂ ਵਿੱਚ ਲੱਛਣ ਮਿਲੇ ਹਨ। ਸਾਫ਼ ਪਾਣੀ ਦਿਓ ਅਤੇ ਲੱਛਣ ਦਿੱਸਦੇ ਹੀ ਵੱਖਰਾ ਕਰੋ।"
            ),
            time = tr("आज, 10:30 AM", "Today, 10:30 AM", "आज, 10:30 AM", "આજે, 10:30 AM", "ਅੱਜ, 10:30 AM"),
            isUrgent = true
        ),
        AlertItem(
            title = tr("गर्मियों में लू व निर्जलीकरण की चेतावनी", "Summer Heatwave & Dehydration Warning", "उन्हाळ्यात उष्माघात व डिहायड्रेशन इशारा", "ઉનાળામાં લૂ અને ડીહાઈડ્રેશન ચેતવણી", "ਗਰਮੀਆਂ ਵਿੱਚ ਲੂ ਅਤੇ ਡੀਹਾਈਡ੍ਰੇਸ਼ਨ ਚੇਤਾਵਨੀ"),
            description = tr(
                "मौसम विभाग के अनुसार तापमान 44°C तक पहुँच सकता है। पशुओं को दोपहर में छाया में रखें और ओआरएस युक्त पानी दें।",
                "Temperature may reach 44°C. Keep cattle in shade during afternoons and provide electrolyte-enriched water.",
                "तापमान ४४°C पर्यंत पोहोचू शकते. दुपारी जनावरांना सावलीत ठेवा आणि ओआरएसयुक्त पाणी द्या.",
                "તાપમાન 44°C સુધી પહોંચી શકે છે. બપોરે પશુઓને છાંયડામાં રાખો અને ઓઆરએસ વાળું પાણી આપો.",
                "ਤਾਪਮਾਨ 44°C ਤੱਕ ਪਹੁੰਚ ਸਕਦਾ ਹੈ। ਦੁਪਹਿਰ ਵੇਲੇ ਪਸ਼ੂਆਂ ਨੂੰ ਛਾਂ ਵਿੱਚ ਰੱਖੋ ਅਤੇ ਓਆਰਐਸ ਵਾਲਾ ਪਾਣੀ ਦਿਓ।"
            ),
            time = tr("कल, 04:15 PM", "Yesterday, 04:15 PM", "काल, 04:15 PM", "ગઈકાલે, 04:15 PM", "ਕੱਲ੍ਹ, 04:15 PM")
        ),
        AlertItem(
            title = tr("मुफ्त राष्ट्रीय पशु रोग नियंत्रण टीकाकरण शिविर", "Free National Animal Disease Vaccination Camp", "मोफत राष्ट्रीय पशुरोग प्रतिबंधक लसीकरण शिबीर", "મફત રાષ્ટ્રીય પશુ રોગ નિયંત્રણ રસીકરણ કેમ્પ", "ਮੁਫ਼ਤ ਰਾਸ਼ਟਰੀ ਪਸ਼ੂ ਰੋਗ ਨਿਯੰਤਰਣ ਟੀਕਾਕਰਨ ਕੈਂਪ"),
            description = tr(
                "गाँव भाटी प्राथमिक पशु केंद्र पर 20 मई को ब्रूसेलोसिस व FMD का निःशुल्क टीकाकरण किया जाएगा।",
                "Free vaccination against Brucellosis & FMD on May 20 at primary veterinary center.",
                "प्राथमिक पशू केंद्रावर २० मे रोजी ब्रुसेलोसिस व FMD चे मोफत लसीकरण केले जाईल.",
                "પ્રાથમિક પશુ કેન્દ્ર પર 20 મે ના રોજ બ્રુસેલોસિસ અને FMD નું મફત રસીકરણ કરવામાં આવશે.",
                "ਪ੍ਰਾਇਮਰੀ ਵੈਟਰਨਰੀ ਸੈਂਟਰ ਵਿਖੇ 20 ਮਈ ਨੂੰ ਬਰੂਸੇਲੋਸਿਸ ਅਤੇ FMD ਦਾ ਮੁਫ਼ਤ ਟੀਕਾਕਰਨ ਕੀਤਾ ਜਾਵੇਗਾ।"
            ),
            time = tr("14 मई 2025", "14 May 2025", "14 मे 2025", "14 મે 2025", "14 ਮਈ 2025")
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = tr("अलर्ट एवं सूचनाएं", "Alerts & Notifications", "सूचना आणि अलर्ट", "ચેતવણીઓ અને સૂચનાઓ", "ਅਲਰਟ ਅਤੇ ਸੂਚਨਾਵਾਂ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(alerts) { alert ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (alert.isUrgent) Color(0xFFFFEBEE) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
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
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = alert.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B241C)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = alert.description,
                                    fontSize = 13.sp,
                                    color = Color(0xFF424242),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = alert.time,
                                    fontSize = 11.sp,
                                    color = Color(0xFF757575)
                                )
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
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = tr("प्रोफ़ाइल", "Profile", "प्रोफाइल", "પ્રોફાઇલ", "ਪ੍ਰੋਫਾਈਲ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                RoleAvatar(
                    roleTitle = currentRole.titleHindi,
                    size = 90.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B241C)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF616161)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Switch Role Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                    color = Color(0xFF1B241C)
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
                                    color = Color(0xFF757575)
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
                                Text(tr("भूमिका बदलें", "Switch Role", "भूमिका बदला", "ભૂમિકા બદલો", "ਭੂਮਿਕਾ ਬਦਲੋ"), fontSize = 13.sp)
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
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GreenDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(tr("PashuSetu v1.0 • डिजिटल भारत मिशन", "PashuSetu v1.0 • Digital India Mission", "PashuSetu v1.0 • डिजिटल भारत अभियान", "PashuSetu v1.0 • ડિજિટલ ભારત મિશન", "PashuSetu v1.0 • ਡਿਜੀਟਲ ਭਾਰਤ ਮਿਸ਼ਨ"), fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = GreenDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("${tr("चयनित भाषा", "Selected Language", "निवडलेली भाषा", "પસંદ કરેલ ભાષા", "ਚੁਣੀ ਹੋਈ ਭਾਸ਼ਾ")}: $selectedLanguage", fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = GreenDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(tr("हेल्पलाइन: 1962 (टोल फ्री 24x7)", "Helpline: 1962 (Toll Free 24x7)", "हेल्पलाइन: 1962 (टोल फ्री 24x7)", "હેલ્પલાઇન: 1962 (ટોલ ફ્રી 24x7)", "ਹੈਲਪਲਾਈਨ: 1962 (ਟੋਲ ਫ੍ਰੀ 24x7)"), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
    selectedLanguage: String = "हिंदी",
    modifier: Modifier = Modifier
) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val medicines = listOf(
        Pair(
            "Melonex ORS",
            tr("दर्द व सूजन निवारक (Non-steroidal Anti-inflammatory)", "Pain & Anti-inflammatory relief", "वेदना व सूज कमी करणारे", "દર્દ અને સોજા નિવારક", "ਦਰਦ ਅਤੇ ਸੋਜ ਨਿਵਾਰਕ")
        ),
        Pair(
            tr("टेट्रासाइक्लिन (Tetracycline)", "Tetracycline", "टेट्रासायक्लिन", "ટેટ્રાસાઇક્લિન", "ਟੈਟਰਾਸਾਈਕਲਿਨ"),
            tr("ब्रॉड स्पेक्ट्रम एंटीबायोटिक (500mg)", "Broad spectrum antibiotic (500mg)", "ब्रॉड स्पेक्ट्रम अँटिबायोटिक (500mg)", "બ્રોડ સ્પેક્ટ્રમ એન્ટીબાયોટીક (500mg)", "ਬ੍ਰਾਡ ਸਪੈਕਟ੍ਰਮ ਐਂਟੀਬਾਇਓਟਿਕ (500mg)")
        ),
        Pair(
            tr("विटामिन बी-कॉम्प्लेक्स सिरप", "Vitamin B-Complex Syrup", "व्हिटॅमिन बी-कॉम्प्लेक्स सिरप", "વિટામિન બી-કોમ્પ્લેક્સ સીરપ", "ਵਿਟਾਮਿਨ ਬੀ-ਕੰਪਲੈਕਸ ਸ਼ਰਬਤ"),
            tr("ऊर्जा व भूख वर्धक टॉनिक", "Energy & appetite tonic", "ऊर्जा व भूक वाढवणारे टॉनिक", "ઊર્જા અને ભૂખ વર્ધક ટોનિક", "ਊਰਜਾ ਅਤੇ ਭੁੱਖ ਵਧਾਉਣ ਵਾਲਾ ਟਾਨਿਕ")
        ),
        Pair(
            tr("पोटैशियम परमैंगनेट (लाल दवा)", "Potassium Permanganate (Lal Dawa)", "पोटॅशियम परमँगनेट (लाल औषध)", "પોટેશિયમ પરમેંગેનેટ (લાલ દવા)", "ਪੋਟਾਸ਼ੀਅਮ ਪਰਮੈਂਗਨੇਟ (ਲਾਲ ਦਵਾਈ)"),
            tr("खुर व घाव धोने हेतु एंटीसेप्टिक घोल", "Antiseptic wash for hooves & wounds", "खुर व जखमा धुण्यासाठी अँटिसेप्टिक द्रावण", "ખૂર અને ઘા ધોવા માટે એન્ટિસેપ્ટિક દ્રાવણ", "ਖੁਰਾਂ ਅਤੇ ਜ਼ਖਮਾਂ ਲਈ ਐਂਟੀਸੈਪਟਿਕ ਘੋਲ")
        ),
        Pair(
            tr("हिमालय बतीसा", "Himalaya Batisa", "हिमालय बतीसा", "હિમાલય બતીસા", "ਹਿਮਾਲਿਆ ਬਤੀਸਾ"),
            tr("पाचन व अपच निवारक आयुर्वेदिक चूर्ण", "Digestive & appetite stimulant ayurvedic powder", "पचन व अपचन निवारक आयुर्वेदिक चूर्ण", "પાચન અને અપચો નિવારક આયુર્વેદિક ચૂર્ણ", "ਪਾਚਨ ਅਤੇ ਅਪਚ ਨਿਵਾਰਕ ਆਯੁਰਵੈਦਿਕ ਚੂਰਨ")
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF9FAF8)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = tr("दवाइयाँ व औषधालय", "Medicines & Pharmacy", "औषधे आणि औषधालय", "દવાઓ અને ઔષધાલય", "ਦਵਾਈਆਂ ਅਤੇ ਫਾਰਮੇਸੀ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(medicines) { (name, desc) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = GreenDark
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(desc, fontSize = 12.sp, color = Color(0xFF616161))
                            }
                        }
                    }
                }
            }
        }
    }
}
