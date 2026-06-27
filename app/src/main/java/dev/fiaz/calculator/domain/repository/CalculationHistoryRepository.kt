package dev.fiaz.calculator.domain.repository

import dev.fiaz.calculator.domain.model.CalculationHistory
import kotlinx.coroutines.flow.Flow

interface CalculationHistoryRepository {
    fun observeHistory(): Flow<List<CalculationHistory>>
    suspend fun insert(expression: String, result: String, timestamp: Long)
    suspend fun delete(history: CalculationHistory)
    suspend fun clearAll()
}