package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.factory.SessionFactory
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.getPlan
import com.samoggino.intermit.data.model.getTimeLeftMillis
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

    private fun observeTimerState() {
        viewModelScope.launch {
            timerViewModel.uiState.collect { timerUi ->
                _uiState.value = HomeUiState.Ready(
                    selectedPlan = timerUi.selectedPlan,
                    timeLeft = timerUi.timeLeft,
                    isRunning = timerUi.isRunning,
                    isPaused = timerUi.isPaused,
                    progress = timerUi.progress
                )
            }
        }
    }

    fun restoreOngoingSession() {
        sessionViewModel.restoreLastActiveSession { session ->
            if (session != null) {
                currentSessionId = session.id // <-- tieni traccia della sessione ripristinata
                val plan = session.getPlan()
                val timeLeft = session.getTimeLeftMillis()

                // Ripristina sempre in pausa (anche se prima era ACTIVE)
                timerViewModel.restoreFromSession(plan, timeLeft)
            }
        }
    }

    fun onStartClicked() {
        val start = System.currentTimeMillis()
        val currentPlan = timerViewModel.uiState.value.selectedPlan

        val session = SessionFactory.createSession(
            startTime = start,
            plan = currentPlan
        )
        // Avvia prima il timer
        timerViewModel.start()
        // Salva la sessione e memorizza l'id quando arriva
        sessionViewModel.insertSession(session) { id ->
            currentSessionId = id
        }
    }


    fun onPauseClicked() {
        // Ferma subito il ticker per evitare il “secondo perso”
        timerViewModel.pause()
        currentSessionId?.let { sessionViewModel.markSessionAs(it, SessionStatus.PAUSED) }
    }

    fun onStopClicked() {
        timerViewModel.stop()
        currentSessionId?.let { sessionViewModel.markSessionAs(it, SessionStatus.STOPPED) }
    }

    fun onPlanSelected(plan: Plan) {
        timerViewModel.selectPlan(plan)
    }
}
