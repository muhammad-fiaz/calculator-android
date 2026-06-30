package dev.fiaz.calculator

import dev.fiaz.calculator.domain.model.AppSettings
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.CalculatorMode
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorModelMappingsTest {
    @Test
    fun app_theme_mode_reads_storage_values() {
        assertEquals(AppThemeMode.System, AppThemeMode.fromStorage(null))
        assertEquals(AppThemeMode.System, AppThemeMode.fromStorage("unknown"))
        assertEquals(AppThemeMode.Light, AppThemeMode.fromStorage("light"))
        assertEquals(AppThemeMode.Dark, AppThemeMode.fromStorage("dark"))
    }

    @Test
    fun calculator_mode_reads_storage_values() {
        assertEquals(CalculatorMode.Normal, CalculatorMode.fromStorage(null))
        assertEquals(CalculatorMode.Normal, CalculatorMode.fromStorage("unknown"))
        assertEquals(CalculatorMode.Normal, CalculatorMode.fromStorage("normal"))
        assertEquals(CalculatorMode.Scientific, CalculatorMode.fromStorage("scientific"))
    }

    @Test
    fun app_settings_defaults_are_safe() {
        val settings = AppSettings()

        assertEquals(AppThemeMode.System, settings.theme)
        assertEquals(true, settings.hapticsEnabled)
        assertEquals(0, settings.calculationCount)
        assertEquals(CalculatorMode.Normal, settings.defaultMode)
    }
}