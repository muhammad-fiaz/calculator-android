package dev.fiaz.calculator.data.repository

import dev.fiaz.calculator.data.local.CalculationHistoryDao
import dev.fiaz.calculator.data.local.CalculationHistoryEntity
import dev.fiaz.calculator.domain.model.CalculationHistory
import dev.fiaz.calculator.domain.repository.CalculationHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationHistoryRepositoryImpl @Inject constructor(
    private val dao: CalculationHistoryDao
) : CalculationHistoryRepository {
    override fun observeHistory(): Flow<List<CalculationHistory>> = dao.getAll().map { histories ->
        histories.map { it.toDomain() }
    }

    override suspend fun insert(expression: String, result: String, timestamp: Long) {
        dao.insert(
            CalculationHistoryEntity(
                expression = expression,
                result = result,
                timestamp = timestamp
            )
        )
    }

    override suspend fun delete(history: CalculationHistory) {
        dao.delete(history.toEntity())
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}

private fun CalculationHistoryEntity.toDomain(): CalculationHistory = CalculationHistory(
    id = id,
    expression = expression,
    result = result,
    timestamp = timestamp
)

private fun CalculationHistory.toEntity(): CalculationHistoryEntity = CalculationHistoryEntity(
    id = id,
    expression = expression,
    result = result,
    timestamp = timestamp
)