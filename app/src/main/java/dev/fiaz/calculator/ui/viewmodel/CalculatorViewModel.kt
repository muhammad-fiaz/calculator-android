package dev.fiaz.calculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fiaz.calculator.domain.engine.CalculationError
import dev.fiaz.calculator.domain.engine.CalculatorExpressionEvaluator
import dev.fiaz.calculator.domain.engine.EvaluationResult
import dev.fiaz.calculator.domain.model.CalculatorMode
import dev.fiaz.calculator.domain.repository.AppSettingsRepository
import dev.fiaz.calculator.domain.repository.CalculationHistoryRepository
import dev.fiaz.calculator.ui.state.CalculatorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.E
import kotlin.math.PI
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val evaluator: CalculatorExpressionEvaluator,
    private val historyRepository: CalculationHistoryRepository,
    private val settingsRepository: AppSettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                _uiState.update { current ->
                    current.copy(
                        mode = settings.defaultMode,
                        isHapticEnabled = settings.hapticsEnabled
                    )
                }
            }
        }
    }

    fun setMode(mode: CalculatorMode) {
        _uiState.update { it.copy(mode = mode) }
    }

    fun toggleAngleMode() {
        _uiState.update { it.copy(isDegreeMode = !it.isDegreeMode) }
        recalculate()
    }

    fun onExpressionChange(expression: String) {
        _uiState.update {
            it.copy(
                expression = expression,
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = expression.length
            )
        }
        recalculate()
    }

    fun onExpressionEdited(expression: String, cursorPosition: Int) {
        val clampedCursor = cursorPosition.coerceIn(0, expression.length)
        _uiState.update {
            it.copy(
                expression = expression,
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = clampedCursor
            )
        }
        recalculate()
    }

    fun onButtonPressed(value: String) {
        when (value) {
            "C", "AC" -> clear()
            "⌫" -> backspace()
            "=" -> commitResult()
            "()" -> appendParenthesis()
            "π" -> append("π")
            "e" -> append("e")
            "DEG", "RAD" -> toggleAngleMode()
            "Deg", "Inv" -> Unit
            else -> append(value)
        }
    }

    fun overwriteExpression(expression: String) {
        _uiState.update {
            it.copy(
                expression = expression,
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = expression.length
            )
        }
        recalculate()
    }

    fun insertExpression(expression: String) {
        overwriteExpression(expression)
    }

    fun clear() {
        _uiState.update {
            it.copy(
                expression = "",
                preview = "",
                result = "",
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = 0
            )
        }
    }

    private fun append(value: String) {
        _uiState.update { current ->
            val isOperator = value in setOf("+", "-", "×", "÷", "^", "%")
            val isFunction = value in setOf("sin", "cos", "tan", "asin", "atan", "ln", "log", "√")
            
            val nextExpression = when {
                current.justEvaluated && isOperator -> {
                    val seed = current.result.ifBlank { current.expression }
                    seed + value
                }
                current.justEvaluated -> {
                    if (isFunction) "$value(" else value
                }
                else -> {
                    val cursor = current.cursorPosition.coerceIn(0, current.expression.length)
                    val prefix = current.expression.substring(0, cursor)
                    val suffix = current.expression.substring(cursor)
                    val toInsert = if (isFunction) "$value(" else value
                    prefix + toInsert + suffix
                }
            }

            val nextCursor = when {
                current.justEvaluated -> nextExpression.length
                else -> {
                    val addedLength = if (isFunction) value.length + 1 else value.length
                    (current.cursorPosition + addedLength).coerceIn(0, nextExpression.length)
                }
            }

            current.copy(
                expression = nextExpression,
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = nextCursor
            )
        }
        recalculate()
    }

    private fun backspace() {
        _uiState.update { current ->
            if (current.expression.isEmpty() || current.cursorPosition <= 0) {
                current.copy(justEvaluated = false)
            } else {
                val cursor = current.cursorPosition.coerceIn(0, current.expression.length)
                val nextExpression = current.expression.removeRange(cursor - 1, cursor)
                current.copy(
                    expression = nextExpression,
                    justEvaluated = false,
                    cursorPosition = (cursor - 1).coerceAtLeast(0)
                )
            }
        }
        recalculate()
    }

    private fun appendParenthesis() {
        _uiState.update { current ->
            val cursor = current.cursorPosition.coerceIn(0, current.expression.length)
            val expression = current.expression
            val openCount = expression.count { it == '(' }
            val closeCount = expression.count { it == ')' }
            val token = if (openCount <= closeCount) "(" else ")"
            val next = expression.substring(0, cursor) + token + expression.substring(cursor)

            current.copy(
                expression = next,
                errorMessage = null,
                justEvaluated = false,
                cursorPosition = (cursor + 1).coerceIn(0, next.length)
            )
        }
        recalculate()
    }

    private fun commitResult() {
        val state = _uiState.value
        if (state.result.isBlank()) return
        if (state.errorMessage != null) return

        viewModelScope.launch {
            historyRepository.insert(state.expression, state.result, System.currentTimeMillis())
            settingsRepository.incrementCalculationCount()
        }
        _uiState.update {
            it.copy(
                expression = state.result,
                justEvaluated = true,
                cursorPosition = state.result.length
            )
        }
        // Note: recalculate() will be called internally if we were using a separate observer, 
        // but here we manually call it or let the next state update handle it.
        // Actually, recalculate() is called at the end of most public methods.
        // In this case, we want the "result" to be the new "expression".
    }

    private fun recalculate() {
        val state = _uiState.value
        val expression = state.expression
        if (expression.isBlank()) {
            _uiState.update { it.copy(preview = "", result = "", errorMessage = null) }
            return
        }

        when (val evaluation = evaluator.evaluate(expression, state.isDegreeMode)) {
            EvaluationResult.Empty -> _uiState.update { it.copy(preview = "", result = "", errorMessage = null) }
            is EvaluationResult.Success -> _uiState.update {
                it.copy(preview = evaluation.value, result = evaluation.value, errorMessage = null)
            }
            is EvaluationResult.Error -> {
                val message = when (evaluation.error) {
                    CalculationError.DivideByZero -> "Divide by zero"
                    CalculationError.InvalidExpression -> "Invalid Syntax"
                }
                _uiState.update { it.copy(preview = message, result = "", errorMessage = message) }
            }
        }
    }
}