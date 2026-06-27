package dev.fiaz.calculator.domain.model

data class CalculationHistory(
    val id: Long,
    val expression: String,
    val result: String,
    val timestamp: Long
)