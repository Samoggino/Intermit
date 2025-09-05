package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.TimerState
import com.samoggino.intermit.data.model.TimerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerViewModel(
    private val initialPlan: Plan
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TimerUiState(
            selectedPlan = initialPlan,
            timeLeft = initialPlan.durationMillis,
            timerState = TimerState.STOPPED
        )
    )
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTime: Long = 0L
    private var endTime: Long = 0L

    fun start() {
        val plan = _uiState.value.selectedPlan
        val now = System.currentTimeMillis()

        if (_uiState.value.timeLeft <= 0L) {
            _uiState.update { it.copy(timeLeft = plan.durationMillis) }
        }

        startTime = now
        endTime = now + _uiState.value.timeLeft
        _uiState.update { it.copy(timerState = TimerState.RUNNING) }

        startTicker()
    }

    private fun startTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _uiState.value.timerState == TimerState.RUNNING) {
                val remaining = (endTime - System.currentTimeMillis()).coerceAtLeast(0L)
                _uiState.update { it.copy(timeLeft = remaining) }

                if (remaining == 0L) {
                    _uiState.update { it.copy(timerState = TimerState.STOPPED) }
                    break
                }
                delay(1000L)
            }
        }
    }

    fun pause() {
        if (_uiState.value.timerState == TimerState.RUNNING) {
            val remaining = (endTime - System.currentTimeMillis()).coerceAtLeast(0L)
            _uiState.update {
                it.copy(timeLeft = remaining, timerState = TimerState.PAUSED)
            }
            timerJob?.cancel()
        }
    }

    fun stop() {
        val plan = _uiState.value.selectedPlan
        _uiState.update {
            it.copy(timeLeft = plan.durationMillis, timerState = TimerState.STOPPED)
        }
        timerJob?.cancel()
    }

    fun selectPlan(plan: Plan) {
        _uiState.update {
            TimerUiState(
                selectedPlan = plan,
                timeLeft = plan.durationMillis,
                timerState = TimerState.STOPPED
            )
        }
        timerJob?.cancel()
    }

    fun restoreFromSession(plan: Plan, remaining: Long) {
        _uiState.update {
            TimerUiState(
                selectedPlan = plan,
                timeLeft = remaining,
                timerState = TimerState.PAUSED
            )
        }
        timerJob?.cancel()
    }
}
