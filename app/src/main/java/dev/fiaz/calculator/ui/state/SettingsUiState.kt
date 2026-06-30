package dev.fiaz.calculator.ui.state

import dev.fiaz.calculator.BuildConfig
import dev.fiaz.calculator.domain.model.AppSettings

data class SettingsUiState(
    val appSettings: AppSettings = AppSettings(),
    val versionName: String = BuildConfig.VERSION_NAME,
    val versionCode: Long = BuildConfig.VERSION_CODE.toLong(),
    val updateInProgress: Boolean = false,
    val reviewRequested: Boolean = false
)