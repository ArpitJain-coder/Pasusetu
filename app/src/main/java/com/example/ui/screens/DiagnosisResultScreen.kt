package com.example.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CattleAvatar
import com.example.ui.theme.GreenDark
import com.example.ui.theme.StatusMediumRisk
import com.example.ui.theme.StatusMediumRiskBg
import com.example.ui.theme.StatusSick
import com.example.ui.viewmodel.DiagnosisResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultScreen(
    result: DiagnosisResult,
    capturedPhoto: Bitmap? = null,
    isHindi: Boolean = true,
    selectedLanguage: String = "हिंदी",
    isSpeaking: Boolean = false,
    onSpeakClick: () -> Unit = {},
    onStopSpeakingClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onContactVetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showMoreInfoDialog by remember { mutableStateOf(false) }

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
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = tr("जाँच का परिणाम", "Diagnosis Result", "तपासणीचा निकाल", "તપાસ પરિણામ", "ਜਾਂਚ ਦਾ ਨਤੀਜਾ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1B241C)
                        )
                    }
                },
                actions = {
                    // Voice read-aloud button
                    IconButton(
                        onClick = {
                            if (isSpeaking) onStopSpeakingClick() else onSpeakClick()
                        }
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = "Read Aloud",
                            tint = if (isSpeaking) Color(0xFFD32F2F) else GreenDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cattle Image / Photo with Warning Badge Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8ECE9)),
                    contentAlignment = Alignment.Center
                ) {
                    if (capturedPhoto != null) {
                        Image(
                            bitmap = capturedPhoto.asImageBitmap(),
                            contentDescription = "Live Cattle Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        CattleAvatar(
                            animalType = "गाय",
                            size = 140.dp,
                            status = "बीमार"
                        )
                    }

                    // Warning Badge on top right of the photo
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(14.dp)
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFA000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Disease Header
                Text(
                    text = tr("संभावित बीमारी", "Detected Condition", "संभाव्य आजार", "સંભવિત રોગ", "ਸੰਭਾਵੀ ਬਿਮਾਰੀ"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (selectedLanguage == "English") result.englishName else result.diseaseName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusSick,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Risk Level Badge & Listen Aloud Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(StatusMediumRiskBg)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${tr("जोखिम स्तर", "Risk Level", "धोक्याची पातळी", "જોખમ સ્તર", "ਜੋਖਮ ਪੱਧਰ")}: ",
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                        Text(
                            text = result.riskLevel,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusMediumRisk
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSpeaking) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                        modifier = Modifier.clickable {
                            if (isSpeaking) onStopSpeakingClick() else onSpeakClick()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = if (isSpeaking) Color(0xFFD32F2F) else GreenDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSpeaking) tr("आवाज रोकें", "Stop Audio", "आवाज थांबवा", "અવાજ રોકો", "ਆਵਾਜ਼ ਰੋਕੋ")
                                else tr("सलाह सुनें", "Listen Audio", "सल्ला ऐका", "સલાહ સાંભળો", "ਸਲਾਹ ਸੁਣੋ"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSpeaking) Color(0xFFD32F2F) else GreenDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Alert Box: Quarantine & Call 1962
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
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
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = StatusSick,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tr(
                                    "तत्काल पशु को अन्य पशुओं से अलग रखें",
                                    "Isolate this animal immediately",
                                    "तात्काळ जनावराला इतर जनावरांपासून वेगळे ठेवा",
                                    "તાત્કાલિક પશુને અન્ય પશુઓથી અલગ રાખો",
                                    "ਤੁਰੰਤ ਪਸ਼ੂ ਨੂੰ ਦੂਜੇ ਪਸ਼ੂਆਂ ਤੋਂ ਵੱਖ ਰੱਖੋ"
                                ),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusSick
                            )
                            Text(
                                text = tr(
                                    "संक्रमण फैलने का खतरा अधिक है। बाड़े को चूने से कीटाणुरहित करें।",
                                    "High infection risk. Sanitize shed with lime/phenyle.",
                                    "संसर्ग पसरण्याचा धोका जास्त आहे. गोठा चुन्याने निर्जंतुक करा.",
                                    "ચેપ ફેલાવવાનું જોખમ વધુ છે. વાડાને ચૂનાથી જંતુમુક્ત કરો.",
                                    "ਲਾਗ ਫੈਲਣ ਦਾ ਖ਼ਤਰਾ ਵੱਧ ਹੈ। ਵਾੜੇ ਨੂੰ ਚੂਨੇ ਨਾਲ ਕੀਟਾਣੂ-ਰਹਿਤ ਕਰੋ।"
                                ),
                                fontSize = 12.sp,
                                color = Color(0xFF555555)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Recommended Actions Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = tr("प्राथमिक सावधानियां एवं देखभाल", "Primary Precautions & Care", "प्राथमिक खबरदारी आणि काळजी", "પ્રાથમિક સાવચેતી અને સંભાળ", "ਮੁੱਢਲੀਆਂ ਸਾਵਧਾਨੀਆਂ ਅਤੇ ਦੇਖਭਾਲ"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B241C)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        result.precautions.forEach { precaution ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = GreenDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = precaution,
                                    fontSize = 13.sp,
                                    color = Color(0xFF333333),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recommended Primary Medicines Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = tr("अनुशंसित प्राथमिक दवाइयां", "Recommended Supportive Medicines", "शिफारस केलेली प्राथमिक औषधे", "ભલામણ કરેલ પ્રાથમિક દવાઓ", "ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀਆਂ ਮੁੱਢਲੀਆਂ ਦਵਾਈਆਂ"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B241C)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        result.recommendedMedicines.forEach { medicine ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F8E9),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "• $medicine",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GreenDark,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Emergency 1962 Direct Call Button
                Button(
                    onClick = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1962"))
                        context.startActivity(callIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr("पशु एम्बुलेंस 1962 पर कॉल करें", "Call Animal Ambulance (1962)", "पशू रुग्णवाहिका 1962 ला कॉल करा", "પશુ એમ્બ્યુલન્સ 1962 પર કૉલ કરો", "ਪਸ਼ੂ ਐਂਬੂਲੈਂਸ 1962 'ਤੇ ਕਾਲ ਕਰੋ"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Book Vet Consultation Button
                Button(
                    onClick = onContactVetClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                ) {
                    Text(
                        text = tr("डॉक्टर से परामर्श / अपॉइंटमेंट बुक करें", "Book Vet Consultation", "डॉक्टरांशी सल्लामसलत / अपॉइंटमेंट बुक करा", "ડૉક્ટર સાથે પરામર્શ / એપોઇન્ટમેન્ટ બુક કરો", "ਡਾਕਟਰ ਨਾਲ ਸਲਾਹ / ਅਪੌਇੰਟਮੈਂਟ ਬੁੱਕ ਕਰੋ"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // More Information Text Button
                TextButton(onClick = { showMoreInfoDialog = true }) {
                    Text(
                        text = tr("इस बीमारी के बारे में और जानें", "Learn more about this condition", "या आजाराबद्दल अधिक जाणून घ्या", "આ રોગ વિશે વધુ જાણો", "ਇਸ ਬਿਮਾਰੀ ਬਾਰੇ ਹੋਰ ਜਾਣੋ"),
                        fontSize = 14.sp,
                        color = GreenDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // More Info Dialog
        if (showMoreInfoDialog) {
            AlertDialog(
                onDismissRequest = { showMoreInfoDialog = false },
                title = {
                    Text(
                        text = if (selectedLanguage == "English") result.englishName else result.diseaseName,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = tr(
                                "खुरपका-मुंहपका (FMD) एक अत्यधिक संक्रामक विषाणु जनित रोग है जो गाय, भैंस, भेड़ और बकरियों को प्रभावित करता है।",
                                "Foot and Mouth Disease is a highly contagious viral illness affecting cattle, buffaloes, sheep and goats.",
                                "लाळ्या-खुरकूत (FMD) हा एक अत्यंत संसर्गजन्य विषाणूजन्य आजार आहे जो गायी, म्हशी, मेंढ्या आणि शेळ्यांना प्रभावित करतो.",
                                "ખુરપકા-મોંપકા (FMD) એક અત્યંત ચેપી વાયરલ રોગ છે જે ગાય, ભેંસ, ઘેટાં અને બકરાંને અસર કરે છે.",
                                "ਮੂੰਹ-ਖੁਰ (FMD) ਇੱਕ ਬਹੁਤ ਹੀ ਛੂਤ ਵਾਲੀ ਵਿਸ਼ਾਣੂ ਰੋਗ ਹੈ ਜੋ ਗਾਵਾਂ, ਮੱਝਾਂ, ਭੇਡਾਂ ਅਤੇ ਬੱਕਰੀਆਂ ਨੂੰ ਪ੍ਰਭਾਵਿਤ ਕਰਦਾ ਹੈ।"
                            ),
                            fontSize = 14.sp
                        )
                        Text(
                            text = tr(
                                "निवारण: हर 6 माह में FMD टीका अवश्य लगवाएं।",
                                "Prevention: Ensure regular vaccination every 6 months.",
                                "प्रतिबंध: दर 6 महिन्यांनी FMD लस नक्की टोचा.",
                                "નિવારણ: દર 6 મહિને FMD રસી અચૂક અપાવો.",
                                "ਬਚਾਅ: ਹਰ 6 ਮਹੀਨੇ ਬਾਅਦ FMD ਟੀਕਾ ਜ਼ਰੂਰ ਲਗਵਾਓ।"
                            ),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showMoreInfoDialog = false }) {
                        Text(tr("ठीक है", "OK", "ठीक आहे", "બરાબર", "ਠੀਕ ਹੈ"))
                    }
                }
            )
        }
    }
}
