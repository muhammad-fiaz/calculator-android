package dev.fiaz.calculator.data.repository

import dev.fiaz.calculator.data.preferences.SettingsDataStore
import dev.fiaz.calculator.domain.model.AppSettings
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.CalculatorMode
import dev.fiaz.calculator.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : AppSettingsRepository {
    override val settings: Flow<AppSettings> = dataStore.settings

    override suspend fun updateTheme(theme: AppThemeMode) = dataStore.updateTheme(theme)

    override suspend fun updateHaptics(enabled: Boolean) = dataStore.updateHaptics(enabled)

    override suspend fun updateDefaultMode(mode: CalculatorMode) = dataStore.updateDefaultMode(mode)

    override suspend fun incrementCalculationCount() = dataStore.incrementCalculationCount()
}