package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.TimerState
import com.samoggino.intermit.data.model.TimerUiState
import com.samoggino.intermit.data.model.getPlan
import com.samoggino.intermit.data.model.getTimeLeftMillis
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

    /** Avvia un nuovo timer */
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

    /** Tick ogni secondo – calcolo sempre rispetto a endTime */
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

                // corregge drift, si allinea sempre al secondo successivo
                val nextTick = 1000L - (System.currentTimeMillis() % 1000L)
                delay(nextTick)
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

    /**
     * Ripristina da una sessione salvata nel DB.
     * Se la sessione è ACTIVE → riparte il timer
     * Se è PAUSED → rimane in pausa
     * Se è STOPPED/COMPLETED → tempo azzerato
     */
    fun restoreFromSession(session: FastingSession) {
        val plan = session.getPlan()
        val remaining = session.getTimeLeftMillis()

        val state = when (session.status) {
            SessionStatus.ACTIVE -> TimerState.RUNNING
            SessionStatus.PAUSED -> TimerState.PAUSED
            else -> TimerState.STOPPED
        }

        _uiState.update {
            TimerUiState(
                selectedPlan = plan,
                timeLeft = remaining,
                timerState = state
            )
        }

        if (state == TimerState.RUNNING) {
            // ricostruisco endTime basato su start + durata
            startTime = session.startTime
            endTime = session.startTime + plan.durationMillis
            startTicker()
        }
    }
}
