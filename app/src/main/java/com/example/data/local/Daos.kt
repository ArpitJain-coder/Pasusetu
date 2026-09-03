package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AlertRecord
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.MedicalCase
import com.example.data.model.MedicineRecord
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CattleDao {
    @Query("SELECT * FROM cattle ORDER BY id ASC")
    fun getAllCattle(): Flow<List<Cattle>>

    @Query("SELECT * FROM cattle WHERE tagNumber = :tag LIMIT 1")
    suspend fun getCattleByTag(tag: String): Cattle?

    @Query("SELECT COUNT(*) FROM cattle")
    fun getCattleCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM cattle WHERE status = 'बीमार'")
    fun getSickCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM cattle WHERE status = 'गर्भवती'")
    fun getPregnantCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCattle(cattle: Cattle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cattleList: List<Cattle>)

    @Update
    suspend fun updateCattle(cattle: Cattle)

    @Delete
    suspend fun deleteCattle(cattle: Cattle)

    @Query("DELETE FROM cattle WHERE id = :id")
    suspend fun deleteCattleById(id: Long)
}

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY id ASC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT COUNT(*) FROM appointments")
    fun getAppointmentCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM appointments WHERE isEmergency = 1")
    fun getEmergencyCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Appointment>)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: Long, status: String)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointmentById(id: Long)
}

@Dao
interface MedicalCaseDao {
    @Query("SELECT * FROM medical_cases ORDER BY id DESC")
    fun getAllCases(): Flow<List<MedicalCase>>

    @Query("SELECT * FROM medical_cases WHERE cattleTag = :tag LIMIT 1")
    suspend fun getCaseByTag(tag: String): MedicalCase?

    @Query("SELECT * FROM medical_cases WHERE id = :id LIMIT 1")
    suspend fun getCaseById(id: Long): MedicalCase?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(medicalCase: MedicalCase): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cases: List<MedicalCase>)

    @Update
    suspend fun updateCase(medicalCase: MedicalCase)

    @Query("UPDATE medical_cases SET status = :status WHERE id = :id")
    suspend fun updateCaseStatus(id: Long, status: String)

    @Delete
    suspend fun deleteCase(medicalCase: MedicalCase)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY id DESC")
    fun getAllAlerts(): Flow<List<AlertRecord>>

    @Query("SELECT * FROM alerts WHERE district = :district OR district = 'सभी' ORDER BY id DESC")
    fun getAlertsByDistrict(district: String): Flow<List<AlertRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<AlertRecord>)

    @Query("UPDATE alerts SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Delete
    suspend fun deleteAlert(alert: AlertRecord)
}

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines ORDER BY id ASC")
    fun getAllMedicines(): Flow<List<MedicineRecord>>

    @Query("SELECT * FROM medicines WHERE category = :category ORDER BY id ASC")
    fun getMedicinesByCategory(category: String): Flow<List<MedicineRecord>>

    @Query("SELECT * FROM medicines WHERE name LIKE '%' || :query || '%' OR genericName LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchMedicines(query: String): Flow<List<MedicineRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: MedicineRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineRecord>)

    @Update
    suspend fun updateMedicine(medicine: MedicineRecord)

    @Delete
    suspend fun deleteMedicine(medicine: MedicineRecord)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)
}
