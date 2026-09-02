package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Appointment
import com.example.data.model.Cattle
import com.example.data.model.MedicalCase
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
}
