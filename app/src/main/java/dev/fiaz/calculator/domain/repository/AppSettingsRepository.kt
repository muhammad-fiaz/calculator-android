package dev.fiaz.calculator.domain.repository

import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.AppSettings
import dev.fiaz.calculator.domain.model.CalculatorMode
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun updateTheme(theme: AppThemeMode)
    suspend fun updateHaptics(enabled: Boolean)
    suspend fun updateDefaultMode(mode: CalculatorMode)
    suspend fun incrementCalculationCount()
}