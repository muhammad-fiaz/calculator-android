package dev.fiaz.calculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.fiaz.calculator.domain.model.CalculationHistory
import dev.fiaz.calculator.domain.repository.CalculationHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: CalculationHistoryRepository
) : ViewModel() {
    private val _history = MutableStateFlow<List<CalculationHistory>>(emptyList())
    val history: StateFlow<List<CalculationHistory>> = _history.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeHistory().collectLatest { 
                _history.value = it
                _isLoading.value = false
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(500)
            _isRefreshing.value = false
        }
    }

    fun delete(history: CalculationHistory) = viewModelScope.launch { repository.delete(history) }

    fun clearAll() = viewModelScope.launch { repository.clearAll() }
}