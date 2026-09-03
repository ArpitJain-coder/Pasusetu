package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AlertRecord
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.MedicalCase
import com.example.data.model.MedicineRecord
import com.example.data.model.UserProfileEntity
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Cattle::class,
        Appointment::class,
        MedicalCase::class,
        VaccineRecord::class,
        AlertRecord::class,
        MedicineRecord::class,
        UserProfileEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cattleDao(): CattleDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun medicalCaseDao(): MedicalCaseDao
    abstract fun vaccineDao(): VaccineDao
    abstract fun alertDao(): AlertDao
    abstract fun medicineDao(): MedicineDao
    abstract fun userProfileDao(): UserProfileDao

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

            // Pre-populate Disease and Weather Outbreak Alerts
            val alertDao = database.alertDao()
            alertDao.insertAll(
                listOf(
                    AlertRecord(
                        title = "FMD (खुरपका मुंहपका) अलर्ट",
                        englishTitle = "FMD (Foot & Mouth) Alert",
                        description = "कोटपूतली व शाहपुरा क्षेत्र में 32 पशुओं में लक्षण पाए गए हैं। अपने पशुओं को स्वच्छ पानी दें और लक्षण दिखते ही अलग रखें।",
                        englishDescription = "Symptoms detected in 32 animals in nearby area. Provide clean water and isolate symptomatic cattle immediately.",
                        timestamp = "आज, 10:30 AM",
                        isUrgent = true,
                        source = "पशुपालन विभाग, राजस्थान सरकार",
                        district = "जयपुर"
                    ),
                    AlertRecord(
                        title = "गर्मियों में लू व निर्जलीकरण की चेतावनी",
                        englishTitle = "Summer Heatwave & Dehydration Warning",
                        description = "मौसम विभाग के अनुसार तापमान 44°C तक पहुँच सकता है। पशुओं को दोपहर में छाया में रखें और ओआरएस युक्त पानी दें।",
                        englishDescription = "Temperature may reach 44°C. Keep cattle in shade during afternoons and provide electrolyte-enriched water.",
                        timestamp = "कल, 04:15 PM",
                        isUrgent = false,
                        source = "मौसम एवं पशु कल्याण विभाग",
                        district = "जयपुर"
                    ),
                    AlertRecord(
                        title = "मुफ्त राष्ट्रीय पशु रोग नियंत्रण टीकाकरण शिविर",
                        englishTitle = "Free National Animal Disease Vaccination Camp",
                        description = "गाँव भाटी प्राथमिक पशु केंद्र पर 20 मई को ब्रूसेलोसिस व FMD का निःशुल्क टीकाकरण किया जाएगा।",
                        englishDescription = "Free vaccination against Brucellosis & FMD on May 20 at primary veterinary center.",
                        timestamp = "14 मई 2025",
                        isUrgent = false,
                        source = "प्राथमिक पशु केंद्र, भाटी",
                        district = "जयपुर"
                    )
                )
            )

            // Pre-populate Essential Veterinary Medicines Inventory
            val medicineDao = database.medicineDao()
            medicineDao.insertAll(
                listOf(
                    MedicineRecord(
                        name = "Melonex ORS",
                        genericName = "Meloxicam + Paracetamol + Electrolytes",
                        category = "दर्द निवारक",
                        descriptionHindi = "दर्द व सूजन निवारक (Non-steroidal Anti-inflammatory)",
                        descriptionEnglish = "Pain & Anti-inflammatory relief oral solution",
                        dosageInfo = "बड़ा पशु: 100ml दिन में दो बार, छोटा पशु: 30ml",
                        inStock = true,
                        price = "₹ 145"
                    ),
                    MedicineRecord(
                        name = "टेट्रासाइक्लिन (Tetracycline)",
                        genericName = "Oxytetracycline HCl 500mg",
                        category = "एंटीबायोटिक",
                        descriptionHindi = "ब्रॉड स्पेक्ट्रम एंटीबायोटिक (500mg बोलस)",
                        descriptionEnglish = "Broad spectrum antibiotic bolus",
                        dosageInfo = "1 बोलस प्रतिदिन 3-5 दिन तक पशु चिकित्सक के परामर्श अनुसार",
                        inStock = true,
                        price = "₹ 85"
                    ),
                    MedicineRecord(
                        name = "विटामिन बी-कॉम्प्लेक्स सिरप",
                        genericName = "Vitamin B-Complex with Liver Extract",
                        category = "टॉनिक",
                        descriptionHindi = "ऊर्जा व भूख वर्धक टॉनिक (Belamyl/Liv-52)",
                        descriptionEnglish = "Energy & appetite stimulant tonic",
                        dosageInfo = "वयस्क पशु: 50ml प्रतिदिन, बछड़ा: 20ml प्रतिदिन",
                        inStock = true,
                        price = "₹ 190"
                    ),
                    MedicineRecord(
                        name = "पोटैशियम परमैंगनेट (लाल दवा)",
                        genericName = "Potassium Permanganate Crystals",
                        category = "एंटीसेप्टिक",
                        descriptionHindi = "खुर व मुंह के छाले धोने हेतु एंटीसेप्टिक घोल (1:1000)",
                        descriptionEnglish = "Antiseptic wash for hooves & mouth blisters",
                        dosageInfo = "हल्का गुलाबी घोल बनाकर छालों और खुरों को दिन में 2 बार धोएं",
                        inStock = true,
                        price = "₹ 45"
                    ),
                    MedicineRecord(
                        name = "हिमालय बतीसा",
                        genericName = "Himalaya Himabatisah Ayurvedic Formulation",
                        category = "पाचक",
                        descriptionHindi = "पाचन व अपच निवारक आयुर्वेदिक चूर्ण",
                        descriptionEnglish = "Digestive & appetite stimulant ayurvedic powder",
                        dosageInfo = "50 ग्राम गुड़ के साथ मिलाकर दिन में दो बार खिलाएं",
                        inStock = true,
                        price = "₹ 110"
                    ),
                    MedicineRecord(
                        name = "टॉपिक्योर स्प्रे (Topicure Spray)",
                        genericName = "Natural Fly Repellent & Wound Healing Spray",
                        category = "एंटीसेप्टिक",
                        descriptionHindi = "खुरपका घाव एवं कीड़ा मारने वाला हर्बल स्प्रे",
                        descriptionEnglish = "Herbal wound healing & maggot repellent spray",
                        dosageInfo = "घाव को साफ करके दिन में 2-3 बार छिड़कें",
                        inStock = true,
                        price = "₹ 160"
                    )
                )
            )

            // Pre-populate Default User Profile
            val profileDao = database.userProfileDao()
            profileDao.saveUserProfile(
                UserProfileEntity(
                    id = 1,
                    name = "राम किसान",
                    phoneOrEmail = "+91 98765 43210",
                    address = "गाँव भाटी, कोटपूतली",
                    district = "जयपुर",
                    pincode = "303108",
                    role = "FARMER",
                    regOrDeptId = "K-RAJ-4091",
                    selectedLanguage = "हिंदी"
                )
            )
        }
    }
}
