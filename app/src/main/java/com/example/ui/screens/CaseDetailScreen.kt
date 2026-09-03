package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.data.model.MedicalCase
import com.example.ui.components.CattleAvatar
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.appTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailScreen(
    medicalCase: MedicalCase,
    selectedLanguage: String = "हिंदी",
    onBackClick: () -> Unit,
    onCallFarmerClick: () -> Unit,
    onUpdateCase: (newTreatment: String, nextVisit: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showCallSuccessDialog by remember { mutableStateOf(false) }

    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar matching Screen 8
            TopAppBar(
                title = {
                    Text(
                        text = tr("केस विवरण", "Case Details", "केस तपशील", "કેસ વિગતો", "ਕੇਸ ਦੇ ਵੇਰਵੇ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
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
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(tr("प्रिस्क्रिप्शन शेयर करें", "Share Prescription", "प्रिस्क्रिप्शन शेअर करा", "પ્રિસ્ક્રિપ્શન શેર કરો", "ਨੁਸਖ਼ਾ ਸਾਂਝਾ ਕਰੋ")) },
                            onClick = { showMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text(tr("केस बंद करें (ठीक हुआ)", "Close Case (Resolved)", "केस बंद करा (बरे झाले)", "કેસ બંધ કરો (સાજા થયા)", "ਕੇਸ ਬੰਦ ਕਰੋ (ਠੀਕ ਹੋਇਆ)")) },
                            onClick = {
                                onUpdateCase(medicalCase.treatment, tr("केस बंद (ठीक हुआ)", "Case Closed (Recovered)", "केस बंद (बरे झाले)", "કેસ બંધ (સાજા થયા)", "ਕੇਸ ਬੰਦ (ਠੀਕ ਹੋਇਆ)"))
                                showMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Header Animal & Farmer Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CattleAvatar(
                                animalType = medicalCase.animalType,
                                size = 68.dp,
                                status = tr("बीमार", "Sick", "आजारी", "બીમાર", "ਬਿਮਾਰ")
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = medicalCase.cattleTag,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${medicalCase.farmerName}, ${medicalCase.village}",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${tr("दिनांक", "Date", "तारीख", "તારીખ", "ਮਿਤੀ")}: ${medicalCase.date}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // Section 1: लक्षण
                    CaseInfoSection(
                        title = tr("लक्षण", "Symptoms", "लक्षणे", "લક્ષણો", "ਲੱਛਣ"),
                        content = medicalCase.symptoms
                    )

                    // Section 2: निदान
                    CaseInfoSection(
                        title = tr("निदान", "Diagnosis", "निदान", "નિદાન", "ਨਿਦਾਨ"),
                        content = medicalCase.diagnosis,
                        isBoldContent = true
                    )

                    // Section 3: उपचार
                    CaseInfoSection(
                        title = tr("उपचार", "Treatment", "उपचार", "સારવાર", "ਇਲਾਜ"),
                        content = medicalCase.treatment
                    )

                    // Section 4: अगली विजिट
                    CaseInfoSection(
                        title = tr("अगली विजिट", "Next Visit", "पुढील भेट", "આગામી મુલાકાત", "ਅਗਲੀ ਮੁਲਾਕਾਤ"),
                        content = medicalCase.nextVisit,
                        isBoldContent = true
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Bottom Action Buttons: Update + Call button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Update Case Button (weight = 1f)
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(
                            text = tr("अपडेट करें", "Update Case", "अपडेट करा", "અપડેટ કરો", "ਅਪਡੇਟ ਕਰੋ"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Call Farmer Button
                    OutlinedButton(
                        onClick = {
                            showCallSuccessDialog = true
                            onCallFarmerClick()
                        },
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = GreenDark
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GreenDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Farmer",
                            tint = GreenDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Edit Treatment Dialog
        if (showEditDialog) {
            var editedTreatment by remember { mutableStateOf(medicalCase.treatment) }
            var editedNextVisit by remember { mutableStateOf(medicalCase.nextVisit) }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text(tr("केस उपचार अपडेट करें", "Update Case Treatment", "केस उपचार अपडेट करा", "કેસ સારવાર અપડેટ કરો", "ਕੇਸ ਇਲਾਜ ਅਪਡੇਟ ਕਰੋ"), fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = editedTreatment,
                            onValueChange = { editedTreatment = it },
                            label = { Text(tr("उपचार व दवाएं", "Treatment & Medicines", "उपचार आणि औषधे", "સારવાર અને દવાઓ", "ਇਲਾਜ ਅਤੇ ਦਵਾਈਆਂ")) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = appTextFieldColors()
                        )
                        OutlinedTextField(
                            value = editedNextVisit,
                            onValueChange = { editedNextVisit = it },
                            label = { Text(tr("अगली विजिट की तारीख", "Next Visit Date", "पुढील भेटीची तारीख", "આગામી મુલાકાતની તારીખ", "ਅਗਲੀ ਮੁਲਾਕਾਤ ਦੀ ਮਿਤੀ")) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = appTextFieldColors()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onUpdateCase(editedTreatment, editedNextVisit)
                            showEditDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                    ) {
                        Text(tr("सेव करें", "Save", "जतन करा", "સાચવો", "ਸੇਵ ਕਰੋ"), color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"), color = TextSecondary)
                    }
                }
            )
        }

        // Call notification dialog
        if (showCallSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showCallSuccessDialog = false },
                title = { Text(tr("किसान से संपर्क", "Contact Farmer", "शेतकऱ्याशी संपर्क", "ખેડૂત સાથે સંપર્ક", "ਕਿਸਾਨ ਨਾਲ ਸੰਪਰਕ"), fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Text(tr(
                        "${medicalCase.farmerName} (+91 98765 43210) को कॉल कनेक्ट किया जा रहा है...",
                        "Connecting call to ${medicalCase.farmerName} (+91 98765 43210)...",
                        "${medicalCase.farmerName} (+91 98765 43210) यांच्याशी कॉल जोडला जात आहे...",
                        "${medicalCase.farmerName} (+91 98765 43210) ને કોલ જોડાઈ રહ્યો છે...",
                        "${medicalCase.farmerName} (+91 98765 43210) ਨੂੰ ਕਾਲ ਮਿਲਾਈ ਜਾ ਰਹੀ ਹੈ..."
                    ), color = TextSecondary)
                },
                confirmButton = {
                    TextButton(onClick = { showCallSuccessDialog = false }) {
                        Text(tr("ठीक है", "OK", "ठीक आहे", "બરાબર", "ਠੀਕ ਹੈ"), color = GreenDark)
                    }
                }
            )
        }
    }
}

@Composable
private fun CaseInfoSection(
    title: String,
    content: String,
    isBoldContent: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = if (isBoldContent) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isBoldContent) TextPrimary else TextSecondary
        )
    }
}
