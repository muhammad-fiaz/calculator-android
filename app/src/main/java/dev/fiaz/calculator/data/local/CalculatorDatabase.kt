package dev.fiaz.calculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CalculationHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun calculationHistoryDao(): CalculationHistoryDao
}