package dev.fiaz.calculator.domain.model

enum class CalculatorMode(val storageName: String) {
    Normal("normal"),
    Scientific("scientific");

    companion object {
        fun fromStorage(value: String?): CalculatorMode = entries.firstOrNull { it.storageName == value } ?: Normal
    }
}