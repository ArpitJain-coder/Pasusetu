package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.MedicalCase
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Cattle::class, Appointment::class, MedicalCase::class, VaccineRecord::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cattleDao(): CattleDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun medicalCaseDao(): MedicalCaseDao
    abstract fun vaccineDao(): VaccineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pashusetu_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val cattleDao = database.cattleDao()
            val appointmentDao = database.appointmentDao()
            val caseDao = database.medicalCaseDao()

            // Pre-populate Cattle matching the exact screenshot
            cattleDao.insertAll(
                listOf(
                    Cattle(
                        tagNumber = "G001",
                        animalType = "गाय",
                        ageYears = 4,
                        status = "स्वस्थ",
                        breed = "हॉल्स्टीन फ्रीजियन / साहीवाल",
                        ownerName = "राम किसान",
                        village = "गाँव भाटी",
                        phone = "+91 98765 43210",
                        lastVaccine = "FMD वैक्सीन - 20 मई 2025"
                    ),
                    Cattle(
                        tagNumber = "B002",
                        animalType = "भैंस",
                        ageYears = 5,
                        status = "स्वस्थ",
                        breed = "मुर्रा भैंस",
                        ownerName = "मोहन किसान",
                        village = "गाँव भाटी",
                        phone = "+91 98234 56789",
                        lastVaccine = "HS वैक्सीन - 10 अप्रैल 2025"
                    ),
                    Cattle(
                        tagNumber = "G003",
                        animalType = "गाय",
                        ageYears = 3,
                        status = "बीमार",
                        breed = "गिर गाय",
                        ownerName = "सीता किसान",
                        village = "गाँव भाटी",
                        phone = "+91 97654 32190",
                        lastVaccine = "FMD वैक्सीन - 15 जनवरी 2025",
                        notes = "मुंह में छाले और लार गिरना"
                    ),
                    Cattle(
                        tagNumber = "C004",
                        animalType = "बछड़ा",
                        ageYears = 1,
                        status = "स्वस्थ",
                        breed = "साहीवाल क्रॉस",
                        ownerName = "राम किसान",
                        village = "गाँव भाटी",
                        phone = "+91 98765 43210",
                        lastVaccine = "ब्रूसेलोसिस - 1 मार्च 2025"
                    )
                )
            )

            // Appointments matching screenshot
            appointmentDao.insertAll(
                listOf(
                    Appointment(
                        cattleTag = "गाय – G001",
                        animalType = "गाय",
                        farmerName = "राम किसान",
                        village = "गाँव भाटी",
                        timeSlot = "09:30 AM",
                        date = "15 मई 2025",
                        reason = "नियमित स्वास्थ्य जाँच और टीकाकरण"
                    ),
                    Appointment(
                        cattleTag = "भैंस – B002",
                        animalType = "भैंस",
                        farmerName = "मोहन किसान",
                        village = "गाँव भाटी",
                        timeSlot = "11:00 AM",
                        date = "15 मई 2025",
                        reason = "दूध उत्पादन में कमी का परामर्श"
                    ),
                    Appointment(
                        cattleTag = "गाय – G003",
                        animalType = "गाय",
                        farmerName = "सीता किसान",
                        village = "गाँव भाटी",
                        timeSlot = "01:30 PM",
                        date = "15 मई 2025",
                        reason = "मुंहपका छाले और तेज बुखार",
                        isEmergency = true
                    )
                )
            )

            // Medical Case matching screenshot
            caseDao.insertAll(
                listOf(
                    MedicalCase(
                        cattleTag = "गाय – G001",
                        animalType = "गाय",
                        farmerName = "राम किसान",
                        village = "गाँव भाटी",
                        date = "15 मई 2025",
                        symptoms = "मुंह में छाले, लार आना, बुखार, खाने में कमी",
                        diagnosis = "FMD (खुरपका मुंहपका)",
                        treatment = "1. Melonex ORS (दर्द व सूजन निवारक)\n2. टेट्रासाइक्लिन (Tetracycline - एंटीबायोटिक)\n3. विटामिन बी-कॉम्प्लेक्स और लिवर टॉनिक",
                        nextVisit = "18 मई 2025",
                        riskLevel = "मध्यम",
                        status = "उपचाराधीन"
                    ),
                    MedicalCase(
                        cattleTag = "गाय – G003",
                        animalType = "गाय",
                        farmerName = "सीता किसान",
                        village = "गाँव भाटी",
                        date = "14 मई 2025",
                        symptoms = "लंगड़ापन, खुरों के बीच छाले, लार गिरना",
                        diagnosis = "FMD (खुरपका मुंहपका)",
                        treatment = "1. पोटैशियम परमैंगनेट (लाल दवा) से धुलाई\n2. एनाल्जेसिक इंजेक्शन\n3. एंटीसेप्टिक स्प्रे",
                        nextVisit = "17 मई 2025",
                        riskLevel = "उच्च",
                        status = "उपचाराधीन"
                    )
                )
            )

            // Pre-populate Standard Indian Veterinary Vaccine Schedule
            val vaccineDao = database.vaccineDao()
            vaccineDao.insertAll(
                listOf(
                    VaccineRecord(
                        vaccineName = "FMD बूस्टर वैक्सीन (खुरपका-मुंहपका)",
                        englishName = "FMD Booster Vaccine",
                        targetDisease = "खुरपका व मुंहपका विषाणु जनित रोग",
                        targetAnimal = "सभी वयस्क गाय व भैंस",
                        scheduledDate = "20 मई 2025 (3 दिन शेष)",
                        dueDateIso = "2025-05-20",
                        locationCenter = "गाँव भाटी प्राथमिक पशु केंद्र",
                        status = VaccineStatus.DUE,
                        batchOrCattleTag = "गाय (G001, G003) व भैंस (B002)",
                        dosage = "2 ml subcutaneous",
                        intervalOrFrequency = "हर 6 माह में बूस्टर खुराक",
                        isGovernmentCamp = true,
                        isAlertActive = true,
                        alertMessageHindi = "अलर्ट: FMD बूस्टर का समय हो गया है! कोटपूतली क्षेत्र में संक्रमण के मामलों के कारण तुरंत लगवाएं।",
                        alertMessageEnglish = "Alert: FMD booster vaccine is due! Outbreak cases reported nearby in Kotputli block."
                    ),
                    VaccineRecord(
                        vaccineName = "ब्रूसेलोसिस कॉटन 19 स्ट्रेन",
                        englishName = "Brucellosis S19 Strain Vaccine",
                        targetDisease = "ब्रूसेलोसिस (गर्भपात रोग)",
                        targetAnimal = "मादा बछिया (उम्र 4-8 महीने)",
                        scheduledDate = "25 मई 2025",
                        dueDateIso = "2025-05-25",
                        locationCenter = "कोटपूतली पशु चिकित्सालय शिविर",
                        status = VaccineStatus.DUE,
                        batchOrCattleTag = "बछड़ा – C004 व युवा बछिया",
                        dosage = "2 ml sub-cut",
                        intervalOrFrequency = "जीवन में केवल एक बार (4-8 माह आयु)",
                        isGovernmentCamp = true,
                        isAlertActive = true,
                        alertMessageHindi = "अलर्ट: 4-8 माह की मादा बछिया के लिए ब्रूसेलोसिस टीका अनिवार्य है।",
                        alertMessageEnglish = "Alert: Brucellosis calfhood vaccination mandatory for female calves aged 4-8 months."
                    ),
                    VaccineRecord(
                        vaccineName = "गलघोंटू (HS) वर्षा-पूर्व वैक्सीन",
                        englishName = "HS (Hemorrhagic Septicemia) Pre-Monsoon",
                        targetDisease = "पाश्चुरेला जीवाणु से होने वाला गलघोंटू",
                        targetAnimal = "सभी गाय व भैंस",
                        scheduledDate = "10 जून 2025",
                        dueDateIso = "2025-06-10",
                        locationCenter = "गाँव भाटी पशु उपकेंद्र",
                        status = VaccineStatus.UPCOMING,
                        batchOrCattleTag = "समस्त पशुधन",
                        dosage = "5 ml sub-cut (Alum-precipitated)",
                        intervalOrFrequency = "वार्षिक (मानसून शुरू होने से पहले मई-जून)",
                        isGovernmentCamp = true,
                        isAlertActive = false,
                        alertMessageHindi = "मानसून से पूर्व गलघोंटू का टीका अवश्य लगवाएं।",
                        alertMessageEnglish = "Ensure HS pre-monsoon vaccination before rainy season begins."
                    ),
                    VaccineRecord(
                        vaccineName = "लंगड़ा बुखार (Black Quarter - BQ)",
                        englishName = "BQ (Black Quarter) Vaccine",
                        targetDisease = "क्लॉस्ट्रिडियम जीवाणु से होने वाला लंगड़ा रोग",
                        targetAnimal = "युवा पशु (6 माह से 2 वर्ष आयु)",
                        scheduledDate = "28 जून 2025",
                        dueDateIso = "2025-06-28",
                        locationCenter = "गाँव भाटी पशु उपकेंद्र",
                        status = VaccineStatus.UPCOMING,
                        batchOrCattleTag = "युवा पशु (G003, C004)",
                        dosage = "5 ml sub-cut",
                        intervalOrFrequency = "वार्षिक (मानसून पूर्व)",
                        isGovernmentCamp = true,
                        isAlertActive = false,
                        alertMessageHindi = "बारिश से पूर्व युवा मवेशियों में BQ का टीका लगवाएं।",
                        alertMessageEnglish = "Administer BQ vaccine to young stock before monsoon."
                    ),
                    VaccineRecord(
                        vaccineName = "लम्पी स्किन रोग (LSD) गोट पॉक्स वैक्सीन",
                        englishName = "Lumpy Skin Disease (Goat Pox) Vaccine",
                        targetDisease = "गांठदार त्वचा रोग (विषाणु जनित)",
                        targetAnimal = "सभी गोवंश",
                        scheduledDate = "15 जनवरी 2025",
                        dueDateIso = "2025-01-15",
                        locationCenter = "राजकीय पशु चिकित्सालय",
                        status = VaccineStatus.COMPLETED,
                        batchOrCattleTag = "गाय G001, G003",
                        dosage = "3 ml sub-cut",
                        intervalOrFrequency = "वार्षिक (वर्ष में एक बार)",
                        isGovernmentCamp = true,
                        isAlertActive = false,
                        alertMessageHindi = "लम्पी स्किन टीकाकरण सफलतापूर्वक पूर्ण हो चुका है।",
                        alertMessageEnglish = "LSD vaccination completed successfully."
                    )
                )
            )
        }
    }
}
