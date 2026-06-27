package dev.fiaz.calculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: CalculationHistoryEntity)

    @Delete
    suspend fun delete(history: CalculationHistoryEntity)

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<CalculationHistoryEntity>>

    @Query("DELETE FROM calculation_history")
    suspend fun clearAll()
}