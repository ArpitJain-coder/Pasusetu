package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VaccineRecord
import com.example.data.model.VaccineStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {
    @Query("SELECT * FROM vaccine_records ORDER BY id ASC")
    fun getAllVaccineRecords(): Flow<List<VaccineRecord>>

    @Query("SELECT * FROM vaccine_records WHERE status = :status ORDER BY id ASC")
    fun getVaccinesByStatus(status: VaccineStatus): Flow<List<VaccineRecord>>

    @Query("SELECT * FROM vaccine_records WHERE status = 'DUE' OR status = 'OVERDUE' ORDER BY id ASC")
    fun getDueVaccines(): Flow<List<VaccineRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccine(vaccine: VaccineRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vaccines: List<VaccineRecord>)

    @Update
    suspend fun updateVaccine(vaccine: VaccineRecord)

    @Delete
    suspend fun deleteVaccine(vaccine: VaccineRecord)

    @Query("UPDATE vaccine_records SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: VaccineStatus)
}
