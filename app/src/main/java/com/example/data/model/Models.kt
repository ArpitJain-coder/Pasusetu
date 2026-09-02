package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val titleHindi: String, val titleEnglish: String) {
    FARMER("किसान", "Farmer"),
    VET("पशु चिकित्सक (Vet)", "Veterinarian"),
    OFFICER("जिला अधिकारी", "District Officer")
}

enum class AnimalStatus(val hindi: String, val english: String) {
    HEALTHY("स्वस्थ", "Healthy"),
    SICK("बीमार", "Sick"),
    PREGNANT("गर्भवती", "Pregnant")
}

@Entity(tableName = "cattle")
data class Cattle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tagNumber: String,       // e.g. "G001", "B002", "G003", "C004"
    val animalType: String,      // "गाय", "भैंस", "बछड़ा", "बैल"
    val ageYears: Int,
    val status: String,          // "स्वस्थ", "बीमार", "गर्भवती"
    val breed: String = "देशी / संकर",
    val ownerName: String = "राम किसान",
    val village: String = "गाँव भाटी",
    val phone: String = "+91 98765 43210",
    val lastVaccine: String = "FMD वैक्सीन - 20 मई 2025",
    val notes: String = ""
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cattleTag: String,
    val animalType: String,
    val farmerName: String,
    val village: String = "गाँव भाटी",
    val timeSlot: String,        // e.g. "09:30 AM"
    val date: String,            // "15 मई 2025"
    val reason: String = "स्वास्थ्य जाँच",
    val isEmergency: Boolean = false,
    val status: String = "लंबित"  // "लंबित", "पूर्ण", "रद्द"
)

@Entity(tableName = "medical_cases")
data class MedicalCase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cattleTag: String,
    val animalType: String,
    val farmerName: String,
    val village: String,
    val date: String,            // "15 मई 2025"
    val symptoms: String,        // "मुंह में छाले, लार आना, बुखार, खाने में कमी"
    val diagnosis: String,       // "FMD (खुरपका मुंहपका)"
    val treatment: String,       // "1. Melonex ORS\n2. टेट्रासाइक्लिन (Tetracycline)\n3. विटामिन बी-कॉम्प्लेक्स"
    val nextVisit: String,       // "18 मई 2025"
    val riskLevel: String = "मध्यम", // "उच्च", "मध्यम", "सामान्य"
    val status: String = "उपचाराधीन" // "उपचाराधीन", "ठीक हुआ"
)

data class DistrictSummary(
    val state: String = "राजस्थान",
    val district: String = "जयपुर",
    val dateRange: String = "01 मई 2025 - 31 मई 2025",
    val totalAnimals: Int = 24583,
    val sickAnimals: Int = 356,
    val sickPercentage: Double = 1.45,
    val vaccinatedAnimals: Int = 24227,
    val zones: List<HeatZone> = emptyList()
)

data class HeatZone(
    val name: String,
    val cases: Int,
    val riskColor: Long, // color hex
    val xPercent: Float,
    val yPercent: Float
)
