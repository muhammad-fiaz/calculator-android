package dev.fiaz.calculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fiaz.calculator.domain.model.UnitCategory
import dev.fiaz.calculator.domain.model.UnitType
import dev.fiaz.calculator.domain.repository.AppSettingsRepository
import dev.fiaz.calculator.domain.repository.UnitConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class UnitConverterUiState(
    val categories: List<UnitCategory> = emptyList(),
    val selectedCategory: UnitCategory? = null,
    val units: List<UnitType> = emptyList(),
    val fromUnit: UnitType? = null,
    val toUnit: UnitType? = null,
    val fromValue: String = "1",
    val toValue: String = "",
    val isHapticEnabled: Boolean = true
)

@HiltViewModel
class UnitConverterViewModel @Inject constructor(
    private val repository: UnitConverterRepository,
    private val settingsRepository: AppSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnitConverterUiState())
    val uiState: StateFlow<UnitConverterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                _uiState.update { current ->
                    current.copy(isHapticEnabled = settings.hapticsEnabled)
                }
            }
        }
        val categories = repository.getCategories()
        val defaultCategory = categories.firstOrNull()
        _uiState.update { state ->
            state.copy(
                categories = categories,
                selectedCategory = defaultCategory
            )
        }
        defaultCategory?.let { selectCategory(it) }
    }

    fun selectCategory(category: UnitCategory) {
        val units = repository.getUnitsForCategory(category)
        val from = units.getOrNull(0)
        val to = units.getOrNull(1) ?: from
        _uiState.update { it.copy(
            selectedCategory = category,
            units = units,
            fromUnit = from,
            toUnit = to
        ) }
        calculate()
    }

    fun onFromValueChanged(value: String) {
        _uiState.update { it.copy(fromValue = value) }
        calculate()
    }

    fun onFromUnitChanged(unit: UnitType) {
        _uiState.update { it.copy(fromUnit = unit) }
        calculate()
    }

    fun onToUnitChanged(unit: UnitType) {
        _uiState.update { it.copy(toUnit = unit) }
        calculate()
    }

    fun swapUnits() {
        _uiState.update { state ->
            state.copy(
                fromUnit = state.toUnit,
                toUnit = state.fromUnit,
                fromValue = state.toValue
            )
        }
        calculate()
    }

    private fun calculate() {
        val state = _uiState.value
        val from = state.fromUnit ?: return
        val to = state.toUnit ?: return
        val value = state.fromValue.toDoubleOrNull() ?: 0.0
        
        val result = repository.convert(value, from, to)
        _uiState.update { it.copy(toValue = String.format(Locale.US, "%.4f", result).trimEnd('0').trimEnd('.')) }
    }
}
