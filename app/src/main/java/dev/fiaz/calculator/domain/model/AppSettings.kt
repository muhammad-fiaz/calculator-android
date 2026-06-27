package dev.fiaz.calculator.domain.model

data class AppSettings(
    val theme: AppThemeMode = AppThemeMode.System,
    val hapticsEnabled: Boolean = true,
    val defaultMode: CalculatorMode = CalculatorMode.Normal,
    val calculationCount: Int = 0
)