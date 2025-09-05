package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.TimerState
import com.samoggino.intermit.ui.screens.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val timerViewModel: TimerViewModel,
    private val sessionViewModel: SessionRepositoryViewModel
) : ViewModel() {

    private var currentSessionId: Long? = null

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        restoreOngoingSession()
        observeTimerState()
    }

    /** Osserva sempre lo stato del Timer e aggiorna la HomeUiState */
    private fun observeTimerState() {
        viewModelScope.launch {
            timerViewModel.uiState.collect { timerUi ->
                _uiState.value = HomeUiState.Ready(
                    selectedPlan = timerUi.selectedPlan,
                    timeLeft = timerUi.timeLeft,
                    isRunning = timerUi.timerState == TimerState.RUNNING,
                    isPaused = timerUi.timerState == TimerState.PAUSED,
                    progress = 1f - (timerUi.timeLeft.toFloat() / timerUi.selectedPlan.durationMillis.toFloat())
                )
            }
        }
    }

    /** Ripristina eventuale sessione non terminata dal DB */
    fun restoreOngoingSession() {
        sessionViewModel.restoreLastActiveSession { session ->
            if (session != null) {
                currentSessionId = session.id
                timerViewModel.restoreFromSession(session)
            }
        }
    }

    /** Avvia nuova sessione */
    fun onStartClicked() {
        if (currentSessionId != null) return // già in corso

        val start = System.currentTimeMillis()
        val currentPlan = timerViewModel.uiState.value.selectedPlan

        val session = FastingSession(
            startTime = start,
            planHours = currentPlan.fastingHours,
            status = SessionStatus.ACTIVE,
            note = null
        )

        // Avvia prima il timer
        timerViewModel.start()

        // Poi salva la sessione e memorizza l'id
        sessionViewModel.insertSession(session) { id ->
            currentSessionId = id
        }
    }

    /** Mette in pausa sessione + timer */
    fun onPauseClicked() {
        timerViewModel.pause()
        val remaining = timerViewModel.uiState.value.timeLeft

        currentSessionId?.let { id ->
            viewModelScope.launch {
                val session = sessionViewModel.getSessionById(id)
                if (session != null) {
                    sessionViewModel.pauseSession(session as FastingSession, remaining)
                }
            }
        }
    }



    /** Ferma sessione + timer */
    fun onStopClicked() {
        timerViewModel.stop()
        currentSessionId?.let { id ->
            sessionViewModel.markSessionAs(id, SessionStatus.STOPPED)
        }
        currentSessionId = null
    }

    /** Seleziona un nuovo piano */
    fun onPlanSelected(plan: Plan) {
        timerViewModel.selectPlan(plan)
    }

    fun onResumeClicked() {
        timerViewModel.resume()
        currentSessionId?.let { id ->
            viewModelScope.launch {
                val session = sessionViewModel.getSessionById(id)
                if (session != null) {
                    // marca sessione come ACTIVE
                    sessionViewModel.markSessionAs(session, SessionStatus.ACTIVE)
                }
            }
        }
    }

}
