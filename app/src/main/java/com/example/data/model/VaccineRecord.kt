package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VaccineStatus(val labelHindi: String, val labelEnglish: String) {
    DUE("टीकाकरण बाकी (Due)", "Due Now"),
    UPCOMING("आगामी (Upcoming)", "Upcoming"),
    COMPLETED("सम्पन्न (Done)", "Completed"),
    OVERDUE("अति-आवश्यक / विलंबित (Overdue)", "Overdue")
}

@Entity(tableName = "vaccine_records")
data class VaccineRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vaccineName: String,          // e.g. "FMD (खुरपका-मुंहपका)", "HS (गलघोंटू)", "BQ (लंगड़ा बुखार)", "ब्रूसेलोसिस (Brucellosis)", "एंथ्रेक्स (Anthrax)"
    val englishName: String,
    val targetDisease: String,        // Disease description
    val targetAnimal: String,         // "सभी पशु (All)", "गाय / भैंस", "मादा बछिया (Female Calf 4-8 months)"
    val scheduledDate: String,        // "20 मई 2025", "10 जून 2025", "आज"
    val dueDateIso: String = "",       // e.g. "2025-05-20"
    val locationCenter: String = "प्राथमिक पशु चिकित्सा केंद्र, गाँव भाटी",
    val status: VaccineStatus = VaccineStatus.DUE,
    val batchOrCattleTag: String = "सभी गाय व भैंस (Herd)",
    val dosage: String = "2 ml (Subcutaneous / Intramuscular)",
    val intervalOrFrequency: String = "वर्ष में दो बार (छमाही)",
    val isGovernmentCamp: Boolean = true,
    val isAlertActive: Boolean = true,
    val alertMessageHindi: String = "",
    val alertMessageEnglish: String = ""
)
