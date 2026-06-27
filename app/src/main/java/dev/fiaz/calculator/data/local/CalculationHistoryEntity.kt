package dev.fiaz.calculator.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calculation_history",
    indices = [Index(value = ["timestamp"])]
)
data class CalculationHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val result: String,
    val timestamp: Long
)