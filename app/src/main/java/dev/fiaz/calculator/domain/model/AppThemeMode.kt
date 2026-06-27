package dev.fiaz.calculator.domain.model

enum class AppThemeMode(val storageName: String) {
    System("system"),
    Light("light"),
    Dark("dark");

    companion object {
        fun fromStorage(value: String?): AppThemeMode = entries.firstOrNull { it.storageName == value } ?: System
    }
}