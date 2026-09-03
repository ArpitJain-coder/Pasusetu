package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.model.Cattle
import com.example.ui.viewmodel.DiagnosisResult

interface LivestockDiagnosisRepository {
    suspend fun getSmartDiagnosis(
        cattle: Cattle?,
        symptoms: Set<String>,
        voiceNotes: String?,
        photo: Bitmap?,
        language: String
    ): Result<DiagnosisResult>
}
