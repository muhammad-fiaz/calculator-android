package dev.fiaz.calculator.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.fiaz.calculator.domain.model.AppSettings
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.CalculatorMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme_mode")
    private val hapticsKey = booleanPreferencesKey("haptics_enabled")
    private val defaultModeKey = stringPreferencesKey("default_mode")
    private val calculationCountKey = androidx.datastore.preferences.core.intPreferencesKey("calculation_count")

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { preferences ->
        AppSettings(
            theme = AppThemeMode.fromStorage(preferences[themeKey]),
            hapticsEnabled = preferences[hapticsKey] ?: true,
            defaultMode = CalculatorMode.fromStorage(preferences[defaultModeKey]),
            calculationCount = preferences[calculationCountKey] ?: 0
        )
    }

    suspend fun incrementCalculationCount() {
        context.settingsDataStore.edit { preferences ->
            val current = preferences[calculationCountKey] ?: 0
            preferences[calculationCountKey] = current + 1
        }
    }

    suspend fun updateTheme(theme: AppThemeMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[themeKey] = theme.storageName
        }
    }

    suspend fun updateHaptics(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[hapticsKey] = enabled
        }
    }

    suspend fun updateDefaultMode(mode: CalculatorMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[defaultModeKey] = mode.storageName
        }
    }
}