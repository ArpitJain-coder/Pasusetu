package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.local.AppDatabase
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.DistrictSummary
import com.example.data.model.HeatZone
import com.example.data.model.MedicalCase
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import com.example.ui.viewmodel.DiagnosisResult
import kotlinx.coroutines.flow.Flow

class PashuSetuRepository(
    private val database: AppDatabase,
    val diagnosisRepository: LivestockDiagnosisRepository = FirebaseLivestockDiagnosisRepository()
) {
    private val cattleDao = database.cattleDao()
    private val appointmentDao = database.appointmentDao()
    private val caseDao = database.medicalCaseDao()
    private val vaccineDao = database.vaccineDao()
    private val alertDao = database.alertDao()
    private val medicineDao = database.medicineDao()
    private val userProfileDao = database.userProfileDao()

    val allCattle: Flow<List<Cattle>> = cattleDao.getAllCattle()
    val allAppointments: Flow<List<Appointment>> = appointmentDao.getAllAppointments()
    val allCases: Flow<List<MedicalCase>> = caseDao.getAllCases()
    val allVaccines: Flow<List<VaccineRecord>> = vaccineDao.getAllVaccineRecords()
    val dueVaccines: Flow<List<VaccineRecord>> = vaccineDao.getDueVaccines()
    val allAlerts: Flow<List<com.example.data.model.AlertRecord>> = alertDao.getAllAlerts()
    val allMedicines: Flow<List<com.example.data.model.MedicineRecord>> = medicineDao.getAllMedicines()
    val userProfile: Flow<com.example.data.model.UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun getCattleByTag(tag: String): Cattle? = cattleDao.getCattleByTag(tag)
    suspend fun getCaseByTag(tag: String): MedicalCase? = caseDao.getCaseByTag(tag)
    suspend fun getCaseById(id: Long): MedicalCase? = caseDao.getCaseById(id)

    suspend fun insertCattle(cattle: Cattle): Long = cattleDao.insertCattle(cattle)
    suspend fun updateCattle(cattle: Cattle) = cattleDao.updateCattle(cattle)
    suspend fun deleteCattle(cattle: Cattle) = cattleDao.deleteCattle(cattle)
    suspend fun deleteCattleById(id: Long) = cattleDao.deleteCattleById(id)

    suspend fun insertAppointment(appointment: Appointment): Long = appointmentDao.insertAppointment(appointment)
    suspend fun updateAppointment(appointment: Appointment) = appointmentDao.updateAppointment(appointment)
    suspend fun updateAppointmentStatus(id: Long, status: String) = appointmentDao.updateAppointmentStatus(id, status)
    suspend fun deleteAppointment(appointment: Appointment) = appointmentDao.deleteAppointment(appointment)
    suspend fun deleteAppointmentById(id: Long) = appointmentDao.deleteAppointmentById(id)

    suspend fun insertCase(medicalCase: MedicalCase): Long = caseDao.insertCase(medicalCase)
    suspend fun updateCase(medicalCase: MedicalCase) = caseDao.updateCase(medicalCase)
    suspend fun updateCaseStatus(id: Long, status: String) = caseDao.updateCaseStatus(id, status)
    suspend fun deleteCase(medicalCase: MedicalCase) = caseDao.deleteCase(medicalCase)

    suspend fun insertVaccine(vaccine: VaccineRecord): Long = vaccineDao.insertVaccine(vaccine)
    suspend fun updateVaccine(vaccine: VaccineRecord) = vaccineDao.updateVaccine(vaccine)
    suspend fun updateVaccineStatus(id: Long, status: VaccineStatus) = vaccineDao.updateStatus(id, status)
    suspend fun deleteVaccine(vaccine: VaccineRecord) = vaccineDao.deleteVaccine(vaccine)

    fun getAlertsByDistrict(district: String): Flow<List<com.example.data.model.AlertRecord>> = alertDao.getAlertsByDistrict(district)
    suspend fun insertAlert(alert: com.example.data.model.AlertRecord): Long = alertDao.insertAlert(alert)
    suspend fun markAlertAsRead(id: Long) = alertDao.markAsRead(id)
    suspend fun deleteAlert(alert: com.example.data.model.AlertRecord) = alertDao.deleteAlert(alert)

    fun searchMedicines(query: String): Flow<List<com.example.data.model.MedicineRecord>> = medicineDao.searchMedicines(query)
    fun getMedicinesByCategory(category: String): Flow<List<com.example.data.model.MedicineRecord>> = medicineDao.getMedicinesByCategory(category)
    suspend fun insertMedicine(medicine: com.example.data.model.MedicineRecord): Long = medicineDao.insertMedicine(medicine)
    suspend fun updateMedicine(medicine: com.example.data.model.MedicineRecord) = medicineDao.updateMedicine(medicine)

    suspend fun getUserProfileOnce(): com.example.data.model.UserProfileEntity? = userProfileDao.getUserProfileOnce()
    suspend fun saveUserProfile(profile: com.example.data.model.UserProfileEntity) = userProfileDao.saveUserProfile(profile)

    /**
     * Connects with Gemini via the Firebase AI SDK in [diagnosisRepository] to generate
     * smart diagnosis suggestions, with seamless fallback to clinical heuristic rules.
     */
    suspend fun getSmartDiagnosis(
        cattle: Cattle?,
        symptoms: Set<String>,
        voiceNotes: String?,
        photo: Bitmap?,
        language: String
    ): DiagnosisResult {
        return diagnosisRepository.getSmartDiagnosis(
            cattle = cattle,
            symptoms = symptoms,
            voiceNotes = voiceNotes,
            photo = photo,
            language = language
        ).getOrElse {
            ClinicalRuleEngine.diagnose(cattle, symptoms, language)
        }
    }

    suspend fun ensureInitialData() {
        AppDatabase.populateInitialData(database)
    }

    fun getDistrictSummary(district: String = "जयपुर"): DistrictSummary {
        val zones = when (district) {
            "जयपुर" -> listOf(
                HeatZone("शाहपुरा / कोटपूतली", 32, 0xFFD32F2F, 0.45f, 0.22f),
                HeatZone("चाकसू / सांगानेर", 18, 0xFFF57C00, 0.52f, 0.45f),
                HeatZone("दूदू / फुलेरा", 12, 0xFFFFA000, 0.35f, 0.65f),
                HeatZone("बस्सी / जमवारामगढ़", 8, 0xFF388E3C, 0.72f, 0.52f),
                HeatZone("चाकसू दक्षिण", 5, 0xFF2E7D32, 0.60f, 0.78f)
            )
            "जोधपुर" -> listOf(
                HeatZone("ओसियां", 24, 0xFFD32F2F, 0.35f, 0.30f),
                HeatZone("फलोदी", 15, 0xFFF57C00, 0.50f, 0.48f),
                HeatZone("लूणी", 9, 0xFF388E3C, 0.65f, 0.68f)
            )
            else -> listOf(
                HeatZone("केंद्रीय क्षेत्र", 21, 0xFFE65100, 0.45f, 0.40f),
                HeatZone("ग्रामीण पूर्व", 14, 0xFFFFA000, 0.65f, 0.55f),
                HeatZone("ग्रामीण पश्चिम", 6, 0xFF388E3C, 0.30f, 0.60f)
            )
        }
        return DistrictSummary(
            state = "राजस्थान",
            district = district,
            dateRange = "01 मई 2025 - 31 मई 2025",
            totalAnimals = if (district == "जयपुर") 24583 else 19840,
            sickAnimals = if (district == "जयपुर") 356 else 210,
            sickPercentage = if (district == "जयपुर") 1.45 else 1.05,
            vaccinatedAnimals = if (district == "जयपुर") 24227 else 19630,
            zones = zones
        )
    }
}
