package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.DistrictSummary
import com.example.data.model.MedicalCase
import com.example.data.model.UserRole
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import com.example.data.repository.PashuSetuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class DiagnosisResult(
    val diseaseName: String = "खुरपका मुंहपका (FMD)",
    val englishName: String = "Foot and Mouth Disease",
    val riskLevel: String = "मध्यम", // "उच्च", "मध्यम", "कम", "सामान्य"
    val riskColor: Long = 0xFFF57C00,
    val precautions: List<String> = listOf(
        "पशु को अलग रखें",
        "साफ पानी और चारा दें",
        "पशु खांच के साफ रखें",
        "जल्दी पशु चिकित्सक से संपर्क करें"
    ),
    val recommendedMedicines: List<String> = listOf(
        "Melonex ORS",
        "टेट्रासाइक्लिन (Tetracycline)",
        "विटामिन बी-कॉम्प्लेक्स"
    ),
    val clinicalSummary: String = "",
    val differentialDiagnosis: List<String> = emptyList(),
    val confidenceScore: Int = 85,
    val isAiPowered: Boolean = true,
    val emergencyHelpline: String = "1962",
    val rawAiResponse: String = ""
)

data class UserProfile(
    val name: String = "राम किसान",
    val phoneOrEmail: String = "+91 98765 43210",
    val address: String = "गाँव भाटी, कोटपूतली",
    val district: String = "जयपुर",
    val pincode: String = "303108",
    val role: UserRole = UserRole.FARMER,
    val regOrDeptId: String = ""
)

class PashuSetuViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = PashuSetuRepository(database)

    // User Profile state
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    val cattleList: StateFlow<List<Cattle>> = repository.allCattle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointmentList: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val caseList: StateFlow<List<MedicalCase>> = repository.allCases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vaccineList: StateFlow<List<VaccineRecord>> = repository.allVaccines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueVaccinesList: StateFlow<List<VaccineRecord>> = repository.dueVaccines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.FARMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Selected Language
    private val _selectedLanguage = MutableStateFlow("हिंदी")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Real Camera photo capture state
    private val _capturedPhoto = MutableStateFlow<Bitmap?>(null)
    val capturedPhoto: StateFlow<Bitmap?> = _capturedPhoto.asStateFlow()

    // Real Voice Recognition state
    private val _spokenText = MutableStateFlow<String?>(null)
    val spokenText: StateFlow<String?> = _spokenText.asStateFlow()

    // Text to Speech playback state
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var textToSpeech: TextToSpeech? = null

    // Diagnosis Wizard State
    private val _selectedCattleForDiagnosis = MutableStateFlow<Cattle?>(null)
    val selectedCattleForDiagnosis: StateFlow<Cattle?> = _selectedCattleForDiagnosis.asStateFlow()

    private val _selectedSymptoms = MutableStateFlow<Set<String>>(emptySet())
    val selectedSymptoms: StateFlow<Set<String>> = _selectedSymptoms.asStateFlow()

    private val _photoUploaded = MutableStateFlow(false)
    val photoUploaded: StateFlow<Boolean> = _photoUploaded.asStateFlow()

    private val _voiceNoteRecorded = MutableStateFlow(false)
    val voiceNoteRecorded: StateFlow<Boolean> = _voiceNoteRecorded.asStateFlow()

    private val _diagnosisResult = MutableStateFlow(DiagnosisResult())
    val diagnosisResult: StateFlow<DiagnosisResult> = _diagnosisResult.asStateFlow()

    private val _isAnalyzingDiagnosis = MutableStateFlow(false)
    val isAnalyzingDiagnosis: StateFlow<Boolean> = _isAnalyzingDiagnosis.asStateFlow()

    // Selected Case for Vet View
    private val _selectedCase = MutableStateFlow<MedicalCase?>(null)
    val selectedCase: StateFlow<MedicalCase?> = _selectedCase.asStateFlow()

    // District officer stats
    private val _selectedDistrict = MutableStateFlow("जयपुर")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _districtSummary = MutableStateFlow(repository.getDistrictSummary("जयपुर"))
    val districtSummary: StateFlow<DistrictSummary> = _districtSummary.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialData()
        }

        // Initialize Android Text-To-Speech engine
        try {
            textToSpeech = TextToSpeech(application) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val hiLocale = Locale("hi", "IN")
                    val result = textToSpeech?.setLanguage(hiLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        textToSpeech?.language = Locale.ENGLISH
                    }
                }
            }
        } catch (_: Exception) { }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        } catch (_: Exception) { }
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun updateFarmerProfile(name: String, mobile: String, village: String, district: String, pincode: String) {
        val cleanName = name.ifBlank { "राम किसान" }
        val cleanMobile = mobile.ifBlank { "9876543210" }
        val cleanVillage = village.ifBlank { "गाँव भाटी, कोटपूतली" }
        val cleanDistrict = district.ifBlank { "जयपुर" }
        val cleanPincode = pincode.ifBlank { "303108" }

        _userProfile.value = UserProfile(
            name = cleanName,
            phoneOrEmail = if (cleanMobile.startsWith("+91")) cleanMobile else "+91 $cleanMobile",
            address = cleanVillage,
            district = cleanDistrict,
            pincode = cleanPincode,
            role = UserRole.FARMER
        )
        _currentRole.value = UserRole.FARMER
        selectDistrict(cleanDistrict)
    }

    fun updateVetProfile(email: String, name: String = "डॉ. राजेश शर्मा (B.V.Sc & A.H)", regNo: String = "RVC-2022-4102") {
        val cleanEmail = email.ifBlank { "dr.rajesh.vet@rajasthan.gov.in" }
        val cleanReg = regNo.ifBlank { "RVC-2022-4102" }
        _userProfile.value = UserProfile(
            name = name,
            phoneOrEmail = cleanEmail,
            address = "राजकीय पशु चिकित्सालय, सांगानेर",
            district = "जयपुर",
            role = UserRole.VET,
            regOrDeptId = cleanReg
        )
        _currentRole.value = UserRole.VET
    }

    fun updateOfficerProfile(email: String, name: String = "डॉ. अनिता वर्मा (अतिरिक्त निदेशक)", department: String = "पशुपालन विभाग, राजस्थान सरकार", district: String = "जयपुर") {
        val cleanEmail = email.ifBlank { "officer.ahd.jaipur@rajasthan.gov.in" }
        val cleanDistrict = district.ifBlank { "जयपुर" }
        _userProfile.value = UserProfile(
            name = name,
            phoneOrEmail = cleanEmail,
            address = department,
            district = cleanDistrict,
            role = UserRole.OFFICER,
            regOrDeptId = "RAJ-AHD-092"
        )
        _currentRole.value = UserRole.OFFICER
        selectDistrict(cleanDistrict)
    }

    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
    }

    fun toggleLanguage() {
        _selectedLanguage.value = if (_selectedLanguage.value == "हिंदी") "English" else "हिंदी"
    }

    fun isHindi(): Boolean = _selectedLanguage.value == "हिंदी"

    fun setCapturedPhoto(bitmap: Bitmap?) {
        _capturedPhoto.value = bitmap
        _photoUploaded.value = bitmap != null
    }

    fun processSpokenText(text: String) {
        _spokenText.value = text
        _voiceNoteRecorded.value = true

        // Intelligently parse symptoms from spoken Hindi/English sentence
        val lower = text.lowercase()
        val detected = mutableSetOf<String>()

        if (lower.contains("छाले") || lower.contains("blister") || lower.contains("ulcer") || lower.contains("mouth") || lower.contains("मुंह")) {
            detected.add("मुंह में छाले (Mouth blisters)")
        }
        if (lower.contains("लार") || lower.contains("saliva") || lower.contains("drool")) {
            detected.add("लार गिरना (Excessive salivation)")
        }
        if (lower.contains("बुखार") || lower.contains("fever") || lower.contains("गरम") || lower.contains("तापमान")) {
            detected.add("तेज बुखार (High fever)")
        }
        if (lower.contains("लंगड़ा") || lower.contains("खुर") || lower.contains("limp") || lower.contains("hoof") || lower.contains("leg")) {
            detected.add("लंगड़ाना / खुरों में घाव (Limping/Hoof lesions)")
        }
        if (lower.contains("खाना") || lower.contains("भूख") || lower.contains("चारा") || lower.contains("appetite") || lower.contains("eating")) {
            detected.add("खाने में कमी / सुस्ती (Loss of appetite)")
        }
        if (lower.contains("दूध") || lower.contains("milk")) {
            detected.add("दूध उत्पादन में अचानक गिरावट (Drop in milk)")
        }
        if (lower.contains("सांस") || lower.contains("breath")) {
            detected.add("सांस लेने में कठिनाई (Breathing difficulty)")
        }

        if (detected.isNotEmpty()) {
            val current = _selectedSymptoms.value.toMutableSet()
            current.addAll(detected)
            _selectedSymptoms.value = current
        }
    }

    fun speakDiagnosisAdvice() {
        val result = _diagnosisResult.value
        val isHi = isHindi()

        val speechText = if (isHi) {
            "जाँच परिणाम: ${result.diseaseName}। जोखिम स्तर: ${result.riskLevel}। मुख्य सावधानियां: ${result.precautions.take(2).joinToString("। ")}। कृपया 1962 पर कॉल करें।"
        } else {
            "Diagnosis Result: ${result.englishName}. Risk Level: ${result.riskLevel}. Key advice: ${result.precautions.take(2).joinToString(". ")}. Call 1962 for assistance."
        }

        try {
            val targetLocale = if (isHi) Locale("hi", "IN") else Locale.ENGLISH
            textToSpeech?.language = targetLocale
            textToSpeech?.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "PashuSetuTTS")
            _isSpeaking.value = true
        } catch (_: Exception) { }
    }

    fun stopSpeaking() {
        try {
            textToSpeech?.stop()
            _isSpeaking.value = false
        } catch (_: Exception) { }
    }

    fun selectCattleForDiagnosis(cattle: Cattle) {
        _selectedCattleForDiagnosis.value = cattle
    }

    fun toggleSymptom(symptom: String) {
        val current = _selectedSymptoms.value.toMutableSet()
        if (current.contains(symptom)) {
            current.remove(symptom)
        } else {
            current.add(symptom)
        }
        _selectedSymptoms.value = current
    }

    fun setPhotoUploaded(uploaded: Boolean) {
        _photoUploaded.value = uploaded
    }

    fun setVoiceNoteRecorded(recorded: Boolean) {
        _voiceNoteRecorded.value = recorded
    }

    fun performDiagnosis(): DiagnosisResult {
        // Immediate initial diagnosis from clinical rule engine
        val immediateResult = com.example.data.repository.ClinicalRuleEngine.diagnose(
            cattle = _selectedCattleForDiagnosis.value,
            symptoms = _selectedSymptoms.value,
            language = _selectedLanguage.value
        )
        _diagnosisResult.value = immediateResult

        // Asynchronously query Firebase AI (Gemini) through the Repository layer
        viewModelScope.launch {
            _isAnalyzingDiagnosis.value = true
            try {
                val smartResult = repository.getSmartDiagnosis(
                    cattle = _selectedCattleForDiagnosis.value,
                    symptoms = _selectedSymptoms.value,
                    voiceNotes = _spokenText.value,
                    photo = _capturedPhoto.value,
                    language = _selectedLanguage.value
                )
                _diagnosisResult.value = smartResult

                // If a cattle was diagnosed, also update cattle status in DB if sick
                _selectedCattleForDiagnosis.value?.let { cattle ->
                    if (smartResult.riskLevel != "सामान्य") {
                        repository.updateCattle(cattle.copy(status = "बीमार", notes = smartResult.diseaseName))
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("PashuSetuViewModel", "Failed fetching Gemini diagnosis", e)
            } finally {
                _isAnalyzingDiagnosis.value = false
            }
        }

        return immediateResult
    }

    fun selectCase(medicalCase: MedicalCase) {
        _selectedCase.value = medicalCase
    }

    fun updateCaseTreatment(caseId: Long, newTreatment: String, nextVisit: String) {
        viewModelScope.launch {
            val current = _selectedCase.value
            if (current != null && current.id == caseId) {
                val updated = current.copy(treatment = newTreatment, nextVisit = nextVisit)
                repository.updateCase(updated)
                _selectedCase.value = updated
            }
        }
    }

    fun addNewCattle(
        tagNumber: String,
        animalType: String,
        ageYears: Int,
        status: String,
        breed: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertCattle(
                Cattle(
                    tagNumber = tagNumber,
                    animalType = animalType,
                    ageYears = ageYears,
                    status = status,
                    breed = breed,
                    notes = notes
                )
            )
        }
    }

    fun selectDistrict(district: String) {
        _selectedDistrict.value = district
        _districtSummary.value = repository.getDistrictSummary(district)
    }

    fun scheduleVetAppointment(cattleTag: String, animalType: String, farmerName: String, timeSlot: String, reason: String) {
        viewModelScope.launch {
            repository.insertAppointment(
                Appointment(
                    cattleTag = cattleTag,
                    animalType = animalType,
                    farmerName = farmerName,
                    timeSlot = timeSlot,
                    date = "आज",
                    reason = reason
                )
            )
        }
    }

    fun markVaccineCompleted(vaccineId: Long) {
        viewModelScope.launch {
            repository.updateVaccineStatus(vaccineId, VaccineStatus.COMPLETED)
        }
    }

    fun addNewVaccineSchedule(
        vaccineName: String,
        englishName: String,
        targetDisease: String,
        targetAnimal: String,
        scheduledDate: String,
        locationCenter: String,
        dosage: String = "2 ml",
        intervalOrFrequency: String = "छमाही",
        alertMsg: String = ""
    ) {
        viewModelScope.launch {
            repository.insertVaccine(
                VaccineRecord(
                    vaccineName = vaccineName,
                    englishName = englishName,
                    targetDisease = targetDisease,
                    targetAnimal = targetAnimal,
                    scheduledDate = scheduledDate,
                    locationCenter = locationCenter,
                    dosage = dosage,
                    intervalOrFrequency = intervalOrFrequency,
                    status = VaccineStatus.DUE,
                    isAlertActive = true,
                    alertMessageHindi = alertMsg.ifBlank { "नया टीकाकरण निर्धारित: $vaccineName" },
                    alertMessageEnglish = "New vaccine scheduled: $englishName"
                )
            )
        }
    }
}
