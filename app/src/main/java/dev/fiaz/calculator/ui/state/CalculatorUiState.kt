package dev.fiaz.calculator.ui.state

import dev.fiaz.calculator.domain.model.CalculatorMode

data class CalculatorUiState(
    val expression: String = "",
    val preview: String = "",
    val result: String = "",
    val errorMessage: String? = null,
    val mode: CalculatorMode = CalculatorMode.Normal,
    val isDegreeMode: Boolean = true,
    val justEvaluated: Boolean = false,
    val cursorPosition: Int = 0,
    val isHapticEnabled: Boolean = true
)