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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.Cattle
import com.example.ui.components.CattleAvatar
import com.example.ui.theme.BorderLight
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusHealthy
import com.example.ui.theme.StatusHealthyBg
import com.example.ui.theme.StatusPregnant
import com.example.ui.theme.StatusPregnantBg
import com.example.ui.theme.StatusSick
import com.example.ui.theme.StatusSickBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.appTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCattleScreen(
    cattleList: List<Cattle>,
    onBackClick: () -> Unit,
    onCattleClick: (Cattle) -> Unit,
    onAddCattle: (tag: String, type: String, age: Int, status: String, breed: String, notes: String) -> Unit,
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

    var showAddDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar matching Screen 4
            TopAppBar(
                title = {
                    Text(
                        text = tr("मेरे पशु", "My Cattle", "माझे पशू", "મારા પશુઓ", "ਮੇਰੇ ਪਸ਼ੂ"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = tr("वापस", "Back", "मागे", "પાછા", "ਵਾਪਸ"),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // "+ पशु जोड़ें" action button
                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GreenDark
                        ),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(16.dp),
                            tint = GreenDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tr("पशु जोड़ें", "Add Cattle", "पशू जोडा", "પશુ ઉમેરો", "ਪਸ਼ੂ ਜੋੜੋ"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            // Cattle List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cattleList) { cattle ->
                    CattleListItem(
                        cattle = cattle,
                        selectedLanguage = selectedLanguage,
                        onClick = { onCattleClick(cattle) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Add Animal Dialog
        if (showAddDialog) {
            AddCattleDialog(
                selectedLanguage = selectedLanguage,
                onDismiss = { showAddDialog = false },
                onAdd = { tag, type, age, status, breed, notes ->
                    onAddCattle(tag, type, age, status, breed, notes)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun CattleListItem(
    cattle: Cattle,
    selectedLanguage: String = "हिंदी",
    onClick: () -> Unit
) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    val isSick = cattle.status == "बीमार" || cattle.status.contains("Sick")
    val isPregnant = cattle.status == "गर्भवती" || cattle.status.contains("Pregnant")

    val (statusColor, statusBg) = when {
        isSick -> Pair(StatusSick, StatusSickBg)
        isPregnant -> Pair(StatusPregnant, StatusPregnantBg)
        else -> Pair(StatusHealthy, StatusHealthyBg)
    }

    val localizedAnimalType = when (cattle.animalType) {
        "गाय" -> tr("गाय", "Cow", "गाय", "ગાય", "ਗਾਂ")
        "भैंस" -> tr("भैंस", "Buffalo", "म्हैस", "ભેંસ", "ਮੱਝ")
        "बछड़ा" -> tr("बछड़ा", "Calf", "वासरू", "વાછરડું", "ਵੱਛਾ")
        else -> cattle.animalType
    }

    val localizedStatus = when {
        isSick -> tr("बीमार", "Sick", "आजारी", "બીમાર", "ਬਿਮਾਰ")
        isPregnant -> tr("गर्भवती", "Pregnant", "गाभण", "સગર્ભા", "ਗਰਭਵਤੀ")
        else -> tr("स्वस्थ", "Healthy", "निरोगी", "સ્વસ્થ", "ਸਿਹਤਮੰਦ")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cattle Avatar Photo
            CattleAvatar(
                animalType = cattle.animalType,
                status = cattle.status,
                size = 64.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$localizedAnimalType – ${cattle.tagNumber}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${tr("उम्र", "Age", "वय", "ઉંમર", "ਉਮਰ")}: ${cattle.ageYears} ${tr("वर्ष", "yrs", "वर्षे", "વર્ષ", "ਸਾਲ")}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${tr("स्थिति", "Status", "स्थिती", "સ્થિતિ", "ਸਥਿਤੀ")}: ",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = localizedStatus,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Arrow Right
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = tr("विवरण देखें", "View Details", "तपशील पहा", "વિગતો જુઓ", "ਵੇਰਵੇ ਵੇਖੋ"),
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AddCattleDialog(
    selectedLanguage: String = "हिंदी",
    onDismiss: () -> Unit,
    onAdd: (tag: String, type: String, age: Int, status: String, breed: String, notes: String) -> Unit
) {
    fun tr(hi: String, en: String, mr: String, gu: String, pa: String): String = when (selectedLanguage) {
        "English" -> en
        "मराठी" -> mr
        "ગુજરાતી" -> gu
        "ਪੰਜਾਬੀ" -> pa
        else -> hi
    }

    var tag by remember { mutableStateOf("G00" + (5..9).random()) }
    var type by remember { mutableStateOf("गाय") }
    var ageText by remember { mutableStateOf("3") }
    var status by remember { mutableStateOf("स्वस्थ") }
    var breed by remember { mutableStateOf("साहीवाल / देशी") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = tr("नया पशु जोड़ें", "Add New Cattle", "नवीन पशू जोडा", "નવું પશુ ઉમેરો", "ਨਵਾਂ ਪਸ਼ੂ ਜੋੜੋ"),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text(tr("टैग नंबर (Tag ID)", "Tag Number (Tag ID)", "टॅग क्रमांक (Tag ID)", "ટેગ નંબર (Tag ID)", "ਟੈਗ ਨੰਬਰ (Tag ID)")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = appTextFieldColors()
                )

                // Animal Type selector
                Text(tr("पशु का प्रकार:", "Animal Type:", "पशूचा प्रकार:", "પશુનો પ્રકાર:", "ਪਸ਼ੂ ਦੀ ਕਿਸਮ:"), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val animalTypes = listOf(
                        Triple("गाय", tr("गाय", "Cow", "गाय", "ગાય", "ਗਾਂ"), "Cow"),
                        Triple("भैंस", tr("भैंस", "Buffalo", "म्हैस", "ભેંસ", "ਮੱਝ"), "Buffalo"),
                        Triple("बछड़ा", tr("बछड़ा", "Calf", "वासरू", "વાછરડું", "ਵੱਛਾ"), "Calf")
                    )
                    animalTypes.forEach { (rawVal, label, _) ->
                        val selected = type == rawVal
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selected) GreenDark else BorderLight)
                                .clickable { type = rawVal }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (selected) Color.White else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it },
                    label = { Text(tr("उम्र (वर्ष)", "Age (Years)", "वय (वर्षे)", "ઉંમર (વર્ષ)", "ਉਮਰ (ਸਾਲ)")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = appTextFieldColors()
                )

                // Status selector
                Text(tr("स्वास्थ्य स्थिति:", "Health Status:", "आरोग्य स्थिती:", "આરોગ્ય સ્થિતિ:", "ਸਿਹਤ ਸਥਿਤੀ:"), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val statusOptions = listOf(
                        Triple("स्वस्थ", tr("स्वस्थ", "Healthy", "निरोगी", "સ્વસ્થ", "ਸਿਹਤਮੰਦ"), StatusHealthy),
                        Triple("बीमार", tr("बीमार", "Sick", "आजारी", "બીમાર", "ਬਿਮਾਰ"), StatusSick),
                        Triple("गर्भवती", tr("गर्भवती", "Pregnant", "गाभण", "સગર્ભા", "ਗਰਭਵਤੀ"), StatusPregnant)
                    )
                    statusOptions.forEach { (rawStatus, label, selColor) ->
                        val selected = status == rawStatus
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selected) selColor else BorderLight)
                                .clickable { status = rawStatus }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (selected) Color.White else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text(tr("नस्ल (Breed)", "Breed", "जात (Breed)", "નસલ (Breed)", "ਨਸਲ (Breed)")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = appTextFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val age = ageText.toIntOrNull() ?: 3
                    onAdd(tag, type, age, status, breed, "")
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
            ) {
                Text(tr("सुरक्षित करें", "Save", "जतन करा", "સાચવો", "ਸੁਰੱਖਿਅਤ ਕਰੋ"), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(tr("रद्द करें", "Cancel", "रद्द करा", "રદ કરો", "ਰੱਦ ਕਰੋ"), color = TextSecondary)
            }
        }
    )
}
