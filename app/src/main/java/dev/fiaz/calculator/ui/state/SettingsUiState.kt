package dev.fiaz.calculator.ui.state

import dev.fiaz.calculator.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val versionName: String = "",
    val versionCode: Long = 0L,
    val updateInProgress: Boolean = false,
    val reviewRequested: Boolean = false
)