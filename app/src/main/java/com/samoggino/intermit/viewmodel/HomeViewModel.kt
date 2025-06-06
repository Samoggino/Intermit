package com.samoggino.intermit.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.factory.SessionFactory
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.TimerState
import com.samoggino.intermit.data.model.getPlan
import com.samoggino.intermit.data.model.getTimeLeftMillis
import com.samoggino.intermit.ui.screens.home.HomeUiState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val timerViewModel: TimerViewModel,
    private val sessionViewModel: SessionRepositoryViewModel
) : ViewModel() {

    private var currentSessionId: Long? = null

    private val _uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
    val uiState: State<HomeUiState> get() = _uiState


    init {
        viewModelScope.launch {
            restoreOngoingSession()  // ora attende che finisca
            observeTimerState()       // poi parte l’osservazione
        }
    }


    private fun observeTimerState() {
        viewModelScope.launch {
            snapshotFlow {
                Triple(
                    timerViewModel.selectedPlan,
                    timerViewModel.timeLeft,
                    timerViewModel.timerState
                )
            }.collect { (plan, timeLeft, timerState) ->

                val progress = if (plan.durationMillis > 0)
                    timeLeft.toFloat() / plan.durationMillis.toFloat()
                else 0f

                _uiState.value = HomeUiState.Ready(
                    selectedPlan = plan,
                    timeLeft = timeLeft,
                    isRunning = (timerState == TimerState.RUNNING),
                    isPaused = (timerState == TimerState.PAUSED),
                    progress = progress
                )
            }
        }
    }


    fun restoreOngoingSession() {
        sessionViewModel.restoreLastActiveSession { session ->
            if (session != null) {
                val plan = session.getPlan()
                val timeLeft = session.getTimeLeftMillis()
                val isRunning = session.status == SessionStatus.ACTIVE

                timerViewModel.restoreFromSession(plan, timeLeft, isRunning)
            }
        }
    }

    fun onStartClicked() {
        val start = System.currentTimeMillis()
        val session =
            SessionFactory.createSession(startTime = start, plan = timerViewModel.selectedPlan)
        timerViewModel.start()

        sessionViewModel.insertSession(session) { id ->
            currentSessionId = id
        }
    }


    fun onPauseClicked() {
        sessionViewModel.markSessionAs(currentSessionId, SessionStatus.PAUSED)
        timerViewModel.pause()
    }

    fun onStopClicked() {
        sessionViewModel.markSessionAs(currentSessionId, SessionStatus.STOPPED)
        timerViewModel.stop()
    }


    fun onPlanSelected(plan: Plan) {
        timerViewModel.selectPlan(plan)
    }
}
