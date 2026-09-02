package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.data.model.Cattle
import com.example.ui.components.CattleAvatar
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.util.AppStrings
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CattleDiagnosisScreen(
    cattleList: List<Cattle>,
    selectedCattle: Cattle?,
    selectedSymptoms: Set<String>,
    capturedPhoto: Bitmap?,
    spokenText: String?,
    isHindi: Boolean = true,
    selectedLanguage: String = "हिंदी",
    onBackClick: () -> Unit,
    onSelectCattle: (Cattle) -> Unit,
    onToggleSymptom: (String) -> Unit,
    onPhotoCaptured: (Bitmap?) -> Unit,
    onVoiceInputProcessed: (String) -> Unit,
    onStartDiagnosis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showChangeCattleDialog by remember { mutableStateOf(false) }
    var showSymptomSheet by remember { mutableStateOf(false) }
    var showPhotoOptionsDialog by remember { mutableStateOf(false) }
    var showSpecimenDialog by remember { mutableStateOf(false) }

    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val activeCattle = selectedCattle ?: cattleList.firstOrNull() ?: Cattle(
        tagNumber = "G001",
        animalType = tr("गाय", "Cow", "गाय", "ગાય", "ਗਾਂ"),
        ageYears = 4,
        status = tr("स्वस्थ", "Healthy", "निरोगी", "સ્વસ્થ", "ਤੰਦਰੁਸਤ")
    )

    val commonSymptoms = listOf(
        tr("मुंह में छाले (Mouth blisters)", "Mouth blisters / ulcers", "तोंडात फोड / व्रण (Mouth blisters)", "મોંમાં ચાંદા (Mouth blisters)", "ਮੂੰਹ ਵਿੱਚ ਛਾਲੇ (Mouth blisters)"),
        tr("लार गिरना (Excessive salivation)", "Excessive drooling / salivation", "लाळ गळणे (Excessive salivation)", "લાળ ટપકવી (Excessive salivation)", "ਲਾਰ ਡਿੱਗਣਾ (Excessive salivation)"),
        tr("तेज बुखार (High fever)", "High fever", "तीव्र ताप (High fever)", "તીવ્ર તાવ (High fever)", "ਤੇਜ਼ ਬੁਖ਼ਾਰ (High fever)"),
        tr("लंगड़ाना / खुरों में घाव (Hoof lesions)", "Limping / Hoof lesions", "लंगडणे / खुरांमध्ये जखमा (Hoof lesions)", "લંગડાવું / ખરીમાં ઘા (Hoof lesions)", "ਲੰਗੜਾਉਣਾ / ਖੁਰਾਂ ਵਿੱਚ ਜ਼ਖ਼ਮ (Hoof lesions)"),
        tr("खाने में कमी / सुस्ती (Loss of appetite)", "Loss of appetite / dullness", "कमी खाणे / सुस्ती (Loss of appetite)", "ભૂખ ન લાગવી / સુસ્તી (Loss of appetite)", "ਖਾਣਾ ਘੱਟ / ਸੁਸਤੀ (Loss of appetite)"),
        tr("दूध उत्पादन में अचानक गिरावट (Drop in milk)", "Sudden drop in milk yield", "दूध उत्पादनात अचानक घट (Drop in milk)", "દૂધ ઉત્પાદનમાં અચાનક ઘટાડો (Drop in milk)", "ਦੁੱਧ ਵਿੱਚ ਅਚਾਨਕ ਗਿਰਾਵਟ (Drop in milk)"),
        tr("सांस लेने में कठिनाई (Breathing difficulty)", "Breathing difficulty", "श्वास घेण्यास त्रास (Breathing difficulty)", "શ્વાસ લેવામાં તકલીફ (Breathing difficulty)", "ਸਾਹ ਲੈਣ ਵਿੱਚ ਔਖ (Breathing difficulty)")
    )

    val speechLocale = when (selectedLanguage) {
        "English" -> "en-IN"
        "मराठी" -> "mr-IN"
        "ગુજરાતી" -> "gu-IN"
        "ਪੰਜਾਬੀ" -> "pa-IN"
        else -> "hi-IN"
    }

    // Camera Launcher (returns live Bitmap from actual camera)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            onPhotoCaptured(bitmap)
            Toast.makeText(
                context,
                tr("कैमरा से फोटो सफलतापूर्वक ली गई ✓", "Photo captured from camera ✓", "कॅमेरामधून फोटो यशस्वीरित्या काढला ✓", "કેમેરાથી ફોટો સફળતાપૂર્વક લેવામાં આવ્યો ✓", "ਕੈਮਰੇ ਤੋਂ ਫੋਟੋ ਸਫ਼ਲਤਾਪੂਰਵਕ ਲਈ ਗਈ ✓"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                tr("फोटो लेने के लिए कैमरा अनुमति आवश्यक है", "Camera permission required to capture photo", "फोटो घेण्यासाठी कॅमेरा परवानगी आवश्यक आहे", "ફોટો લેવા માટે કેમેરા પરવાનગી જરૂરી છે", "ਫੋਟੋ ਲੈਣ ਲਈ ਕੈਮਰਾ ਆਗਿਆ ਲੋੜੀਂਦੀ ਹੈ"),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Speech-To-Text Recognition Launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedSentence = spoken?.firstOrNull()
            if (!recognizedSentence.isNullOrBlank()) {
                onVoiceInputProcessed(recognizedSentence)
                Toast.makeText(
                    context,
                    "${tr("आवाज दर्ज हुई", "Recorded", "आवाज नोंदवला", "અવાજ નોંધાયો", "ਆਵਾਜ਼ ਦਰਜ ਹੋਈ")}: $recognizedSentence",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Audio Permission Launcher
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    speechLocale
                )
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    tr("पशु के लक्षण बोलकर बताएं...", "Speak symptoms of your cattle...", "जनावराची लक्षणे बोलून सांगा...", "પશુના લક્ષણો બોલીને જણાવો...", "ਪਸ਼ੂ ਦੇ ਲੱਛਣ ਬੋਲ ਕੇ ਦੱਸੋ...")
                )
            }
            try {
                speechLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    tr("वॉयस रिकॉग्निशन उपलब्ध नहीं है", "Voice recognition not available", "व्हॉइस रेकग्निशन उपलब्ध नाही", "વોઇસ રેકગ્નિશન ઉપલબ્ધ નથી", "ਵੌਇਸ ਪਛਾਣ ਉਪਲਬਧ ਨਹੀਂ ਹੈ"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                tr("आवाज से लक्षण बताने के लिए माइक अनुमति आवश्यक है", "Microphone permission required for voice notes", "आवाजाद्वारे लक्षणे सांगण्यासाठी माइक परवानगी आवश्यक आहे", "અવાજથી લક્ષણો જણાવવા માઈક પરવાનગી જરૂરી છે", "ਆਵਾਜ਼ ਨਾਲ ਲੱਛਣ ਦੱਸਣ ਲਈ ਮਾਈਕ ਆਗਿਆ ਲੋੜੀਂਦੀ ਹੈ"),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun startLiveCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(null)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLocale)
                putExtra(RecognizerIntent.EXTRA_PROMPT, tr("पशु के लक्षण बोलकर बताएं...", "Speak symptoms of your cattle...", "जनावराची लक्षणे बोलून सांगा...", "પશુના લક્ષણો બોલીને જણાવો...", "ਪਸ਼ੂ ਦੇ ਲੱਛਣ ਬੋਲ ਕੇ ਦੱਸੋ..."))
            }
            try {
                speechLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(context, tr("वॉयस सेवा डिवाइस पर सक्रिय नहीं है", "Voice service unavailable", "व्हॉइस सेवा उपलब्ध नाही", "વોઇસ સેવા ઉપલબ્ધ નથી", "ਵੌਇਸ ਸੇਵਾ ਉਪਲਬਧ ਨਹੀਂ ਹੈ"), Toast.LENGTH_SHORT).show()
            }
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
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
                        text = tr("पशु स्वास्थ्य जाँच (AI)", "Cattle Health Diagnosis (AI)", "पशू आरोग्य तपासणी (AI)", "પશુ આરોગ્ય તપાસ (AI)", "ਪਸ਼ੂ ਸਿਹਤ ਜਾਂਚ (AI)"),
                        fontSize = 19.sp,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Step Indicator Bar
                    DiagnosisStepBar(currentStep = 2, selectedLanguage = selectedLanguage)

                    Spacer(modifier = Modifier.height(18.dp))

                    // Selected Animal Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CattleAvatar(
                                animalType = activeCattle.animalType,
                                status = activeCattle.status,
                                size = 56.dp
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${activeCattle.animalType} – ${activeCattle.tagNumber}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B241C)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${tr("उम्र", "Age", "वय", "ઉંમર", "ਉਮਰ")}: ${activeCattle.ageYears} ${tr("वर्ष", "yrs", "वर्षे", "વર્ષ", "ਸਾਲ")} • ${activeCattle.status}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF616161)
                                )
                            }

                            TextButton(onClick = { showChangeCattleDialog = true }) {
                                Text(
                                    text = tr("बदलें", "Change", "बदला", "બદલો", "ਬਦਲੋ"),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // "समस्या बताएं" Header
                    Text(
                        text = tr("लक्षण एवं समस्या दर्ज करें", "Enter Symptoms & Problem", "लक्षणे व समस्या नोंदवा", "લક્ષણો અને સમસ્યા દાખલ કરો", "ਲੱਛਣ ਅਤੇ ਸਮੱਸਿਆ ਦਰਜ ਕਰੋ"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241C)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input Modes: 2 cards in Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1: लाइव कैमरा
                        InputModeCard(
                            title = tr("लाइव कैमरा", "Live Camera", "थेट कॅमेरा", "લાઇવ કેમેરા", "ਲਾਈਵ ਕੈਮਰਾ"),
                            subtitle = if (capturedPhoto != null) tr("फोटो ली गई ✓", "Photo captured ✓", "फोटो काढला ✓", "ફોટો લેવાયો ✓", "ਫੋਟੋ ਲਈ ਗਈ ✓")
                            else tr("फोटो खींचें", "Take Photo", "फोटो काढा", "ફોટો ખેંચો", "ਫੋਟੋ ਖਿੱਚੋ"),
                            icon = Icons.Default.CameraAlt,
                            isActive = capturedPhoto != null,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showPhotoOptionsDialog = true
                            }
                        )

                        // Card 2: लक्षण चुनें
                        InputModeCard(
                            title = tr("लक्षण सूची", "Symptoms List", "लक्षण यादी", "લક્ષણ યાદી", "ਲੱਛਣ ਸੂਚੀ"),
                            subtitle = if (selectedSymptoms.isNotEmpty()) "${selectedSymptoms.size} ${tr("चुने गए", "selected", "निवडले", "પસંદ કરેલ", "ਚੁਣੇ ਗਏ")}"
                            else tr("चेकलिस्ट", "Checklist", "चेकलिस्ट", "ચેકલિસ્ટ", "ਚੈੱਕਲਿਸਟ"),
                            icon = Icons.Default.FormatListBulleted,
                            isActive = selectedSymptoms.isNotEmpty(),
                            modifier = Modifier.weight(1f),
                            onClick = { showSymptomSheet = !showSymptomSheet }
                        )
                    }

                    // Display Live Captured Photo if available
                    if (capturedPhoto != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = GreenDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = tr("संलग्न लाइव कैमरा फोटो", "Attached Live Camera Photo", "जोडलेला थेट कॅमेरा फोटो", "જોડાયેલ લાઇવ કેમેરા ફોટો", "ਨੱਥੀ ਲਾਈਵ ਕੈਮਰਾ ਫੋਟੋ"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GreenDark
                                        )
                                    }

                                    Row {
                                        IconButton(onClick = { startLiveCamera() }) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Retake", tint = GreenDark)
                                        }
                                        IconButton(onClick = { onPhotoCaptured(null) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFD32F2F))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Image(
                                    bitmap = capturedPhoto.asImageBitmap(),
                                    contentDescription = "Captured Cattle Photo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Card 3: आवाज से बताएं (Full width Speech recognition)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { startVoiceRecognition() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!spokenText.isNullOrBlank()) Color(0xFFEDE7F6) else Color(0xFFF3E5F5)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "Voice",
                                            tint = Color(0xFF6A1B9A),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tr("आवाज से लक्षण बताएं (माइक टैप करें)", "Speak Symptoms (Tap to Talk)", "आवाजाद्वारे लक्षणे सांगा (माइक टॅप करा)", "અવાજથી લક્ષણો જણાવો (માઈક ટેપ કરો)", "ਆਵਾਜ਼ ਨਾਲ ਲੱਛਣ ਦੱਸੋ (ਮਾਈਕ ਟੈਪ ਕਰੋ)"),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A148C)
                                    )
                                    Text(
                                        text = tr("मातृभाषा में बोलें, AI स्वयं लक्षण पहचानेगा", "Speak naturally, AI automatically detects symptoms", "आपल्या भाषेत बोला, AI स्वतः लक्षणे ओळखेल", "તમારી ભાષામાં બોલો, AI આપોઆપ લક્ષણો ઓળખશે", "ਆਪਣੀ ਭਾਸ਼ਾ ਵਿੱਚ ਬੋਲੋ, AI ਖ਼ੁਦ ਲੱਛਣ ਪਛਾਣੇਗਾ"),
                                        fontSize = 12.sp,
                                        color = Color(0xFF7B1FA2)
                                    )
                                }
                            }

                            if (!spokenText.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.VolumeUp,
                                            contentDescription = null,
                                            tint = Color(0xFF6A1B9A),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${tr("सुना गया", "Transcribed", "ऐकले", "સાંભળ્યું", "ਸੁਣਿਆ")}: \"$spokenText\"",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF333333)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Expandable Symptom Selector Checklist
                    if (showSymptomSheet || selectedSymptoms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tr("लक्षणों का चयन करें:", "Select Observed Symptoms:", "लक्षणे निवडा:", "લક્ષણો પસંદ કરો:", "ਲੱਛਣ ਚੁਣੋ:"),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B241C)
                                    )

                                    // View specimen illustration button
                                    TextButton(onClick = { showSpecimenDialog = true }) {
                                        Text(
                                            text = tr("लक्षण चित्र गाइड", "Symptom Visual Guide", "लक्षण चित्र मार्गदर्शक", "લક્ષણ ચિત્ર માર્ગદર્શિકા", "ਲੱਛਣ ਚਿੱਤਰ ਗਾਈਡ"),
                                            fontSize = 12.sp,
                                            color = GreenDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                commonSymptoms.forEach { symptom ->
                                    val checked = selectedSymptoms.contains(symptom)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onToggleSymptom(symptom) }
                                            .padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = { onToggleSymptom(symptom) },
                                            colors = CheckboxDefaults.colors(checkedColor = GreenDark)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = symptom,
                                            fontSize = 13.sp,
                                            color = if (checked) GreenDark else Color(0xFF333333),
                                            fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Start Diagnosis Button
                Button(
                    onClick = onStartDiagnosis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                ) {
                    Text(
                        text = tr("जाँच शुरू करें (AI विश्लेषण)", "Start Diagnosis (AI Analysis)", "तपासणी सुरू करा (AI विश्लेषण)", "તપાસ શરૂ કરો (AI વિશ્લેષણ)", "ਜਾਂਚ ਸ਼ੁਰੂ ਕਰੋ (AI ਵਿਸ਼ਲੇਸ਼ਣ)"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Camera Options Dialog (Live Camera vs Clinical Reference Specimen)
        if (showPhotoOptionsDialog) {
            AlertDialog(
                onDismissRequest = { showPhotoOptionsDialog = false },
                title = { Text(tr("फोटो का माध्यम चुनें", "Choose Photo Source", "फोटोचा पर्याय निवडा", "ફોટો માધ્યમ પસંદ કરો", "ਫੋਟੋ ਦਾ ਮਾਧਿਅਮ ਚੁਣੋ")) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showPhotoOptionsDialog = false
                                    startLiveCamera()
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GreenDark)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = tr("लाइव कैमरा से फोटो लें", "Take Live Photo (Camera)", "थेट कॅमेरामधून फोटो काढा", "લાઇવ કેમેરાથી ફોટો લો", "ਲਾਈਵ ਕੈਮਰੇ ਨਾਲ ਫੋਟੋ ਲਵੋ"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = tr("पशु के मुंह या खुर की तुरंत तस्वीर लें", "Capture animal mouth/hoof right now", "जनावराचे तोंड किंवा खुराचा त्वरित फोटो काढा", "પશુના મોં અથવા ખરીનો તુરંત ફોટો લો", "ਪਸ਼ੂ ਦੇ ਮੂੰਹ ਜਾਂ ਖੁਰ ਦੀ ਤੁਰੰਤ ਤਸਵੀਰ ਲਵੋ"),
                                        fontSize = 12.sp,
                                        color = Color(0xFF555555)
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showPhotoOptionsDialog = false
                                    showSpecimenDialog = true
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFFF57C00))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = tr("नैदानिक संदर्भ गाइड देखें", "View Clinical Reference Specimen", "वैद्यकीय संदर्भ मार्गदर्शक पहा", "તબીબી સંદર્ભ માર્ગદર્શિકા જુઓ", "ਕਲੀਨਿਕਲ ਹਵਾਲਾ ਗਾਈਡ ਦੇਖੋ"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = tr("FMD छाले और खुर घाव का मानक चित्र", "Standard clinical mouth/hoof lesions guide", "FMD फोड आणि खूर जखमांचे मानक चित्र", "FMD ચાંદા અને ખરીના ઘાનું પ્રમાણભૂત ચિત્ર", "FMD ਛਾਲੇ ਅਤੇ ਖੁਰ ਜ਼ਖ਼ਮਾਂ ਦਾ ਮਿਆਰੀ ਚਿੱਤਰ"),
                                        fontSize = 12.sp,
                                        color = Color(0xFF555555)
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showPhotoOptionsDialog = false }) {
                        Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"))
                    }
                }
            )
        }

        // Specimen / Symptom Visual Guide Dialog
        if (showSpecimenDialog) {
            AlertDialog(
                onDismissRequest = { showSpecimenDialog = false },
                title = {
                    Text(
                        tr("पशु रोग पहचान चित्र गाइड", "Livestock Disease Visual Inspection Guide", "पशू रोग ओळख चित्र मार्गदर्शक", "પશુ રોગ ઓળખ ચિત્ર માર્ગદર્શિકા", "ਪਸ਼ੂ ਰੋਗ ਪਛਾਣ ਚਿੱਤਰ ਗਾਈਡ"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.img_symptom_specimen),
                            contentDescription = "Clinical Symptom Diagram",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = tr(
                                "बाईं ओर: मुंह में छाले एवं लार के लक्षण। दाईं ओर: खुरों के बीच में घाव व लालिमा।",
                                "Left: Mouth blisters & drooling salivation. Right: Interdigital hoof lesions & redness.",
                                "डावीकडे: तोंडात फोड आणि लाळ गळणे. उजवीकडे: खुरांच्या दरम्यान जखमा आणि लालसरपणा.",
                                "ડાબી બાજુ: મોંમાં ચાંદા અને લાળ પડવી. જમણી બાજુ: ખરીઓ વચ્ચે ઘા અને લાલાશ.",
                                "ਖੱਬੇ ਪਾਸੇ: ਮੂੰਹ ਵਿੱਚ ਛਾਲੇ ਅਤੇ ਲਾਰ ਡਿੱਗਣਾ। ਸੱਜੇ ਪਾਸੇ: ਖੁਰਾਂ ਵਿਚਕਾਰ ਜ਼ਖ਼ਮ ਅਤੇ ਲਾਲੀ।"
                            ),
                            fontSize = 12.sp,
                            color = Color(0xFF424242),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSpecimenDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(tr("समझ आ गया", "Understood", "समजले", "સમજાઈ ગયું", "ਸਮਝ ਆ ਗਿਆ"))
                    }
                }
            )
        }

        // Change Cattle Dialog
        if (showChangeCattleDialog) {
            AlertDialog(
                onDismissRequest = { showChangeCattleDialog = false },
                title = { Text(text = tr("पशु चुनें", "Select Cattle", "पशू निवडा", "પશુ પસંદ કરો", "ਪਸ਼ੂ ਚੁਣੋ"), fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        cattleList.forEach { cattle ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        onSelectCattle(cattle)
                                        showChangeCattleDialog = false
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CattleAvatar(animalType = cattle.animalType, size = 40.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${cattle.animalType} – ${cattle.tagNumber}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${tr("उम्र", "Age", "वय", "ઉંમર", "ਉਮਰ")}: ${cattle.ageYears} | ${cattle.status}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showChangeCattleDialog = false }) {
                        Text(tr("बंद करें", "Close", "बंद करा", "બંધ કરો", "ਬੰਦ ਕਰੋ"))
                    }
                }
            )
        }
    }
}

@Composable
private fun DiagnosisStepBar(currentStep: Int, selectedLanguage: String) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StepItem(number = 1, label = tr("पशु", "Cattle", "पशू", "પશુ", "ਪਸ਼ੂ"), isActive = currentStep >= 1)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (currentStep >= 2) GreenDark else Color(0xFFE0E0E0))
        )
        StepItem(number = 2, label = tr("लक्षण", "Symptoms", "लक्षणे", "લક્ષણો", "ਲੱਛਣ"), isActive = currentStep >= 2)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (currentStep >= 3) GreenDark else Color(0xFFE0E0E0))
        )
        StepItem(number = 3, label = tr("परिणाम", "Result", "निकाल", "પરિણામ", "ਨਤੀਜਾ"), isActive = currentStep >= 3)
    }
}

@Composable
private fun StepItem(number: Int, label: String, isActive: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) GreenDark else Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$number",
                color = if (isActive) Color.White else Color(0xFF757575),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) GreenDark else Color(0xFF757575)
        )
    }
}

@Composable
private fun InputModeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFE8F5E9) else Color(0xFFF1F8E9)
        ),
        border = if (isActive) CardDefaults.outlinedCardBorder().copy(width = 1.5.dp) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isActive) GreenDark else Color(0xFF2E7D32),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B241C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = if (isActive) GreenDark else Color(0xFF616161),
                textAlign = TextAlign.Center
            )
        }
    }
}
