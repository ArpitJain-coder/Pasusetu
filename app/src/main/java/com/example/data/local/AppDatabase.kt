package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.MedicalCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Cattle::class, Appointment::class, MedicalCase::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cattleDao(): CattleDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun medicalCaseDao(): MedicalCaseDao

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
        }
    }
}
