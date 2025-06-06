package com.samoggino.intermit.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.factory.SessionFactory
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.ui.screens.home.HomeUiState
import kotlinx.coroutines.flow.first
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
            restoreLastSessionOnce()  // ora attende che finisca
            observeTimerState()       // poi parte l’osservazione
        }
    }


    private fun observeTimerState() {
        viewModelScope.launch {
            snapshotFlow {
                Triple(
                    timerViewModel.selectedPlan,
                    timerViewModel.timeLeft,
                    timerViewModel.isRunning
                )
            }.collect { (plan, timeLeft, isRunning) ->

                val progress = if (plan.durationMillis > 0)
                    timeLeft.toFloat() / plan.durationMillis.toFloat()
                else 0f

                _uiState.value = HomeUiState.Ready(
                    selectedPlan = plan,
                    timeLeft = timeLeft,
                    isRunning = isRunning,
                    progress = progress
                )
            }
        }
    }

    suspend fun restoreLastSessionOnce() {
        val sessions = sessionViewModel.allSessions.asFlow().first()
        val last = sessions.lastOrNull()
        if (last != null && last.endTime == 0L) {
            val elapsed = System.currentTimeMillis() - last.startTime
            val duration = timerViewModel.selectedPlan.durationMillis
            val timeLeft = (duration - elapsed).coerceAtLeast(0L)

            timerViewModel.updateTimeLeft(timeLeft)

            if (timeLeft > 0) {
                timerViewModel.startWithoutReset()
            }

            currentSessionId = last.id
        }
    }

    fun onStartClicked() {
        val start = System.currentTimeMillis()
        val session = SessionFactory.createSession(startTime = start)
        timerViewModel.start()

        sessionViewModel.insertSession(session) { id ->
            currentSessionId = id
        }
    }


    fun onPauseClicked() {
        timerViewModel.pause()
        currentSessionId?.let { id ->
            val updatedSession = SessionFactory.createSession(
                startTime = System.currentTimeMillis() - timerViewModel.timeLeft, // o altro metodo
                endTime = 0L
            ).copy(id = id)
            sessionViewModel.updateSession(updatedSession)
        }
    }


    fun onStopClicked() {
        timerViewModel.stop()
        val end = System.currentTimeMillis()

        currentSessionId?.let { id ->
            val updatedSession = SessionFactory.createSession(
                startTime = end - timerViewModel.selectedPlan.durationMillis,
                endTime = end
            ).copy(id = id)
            sessionViewModel.updateSession(updatedSession)
            currentSessionId = null
        }
    }


    fun onPlanSelected(plan: Plan) {
        timerViewModel.selectPlan(plan)
    }
}
