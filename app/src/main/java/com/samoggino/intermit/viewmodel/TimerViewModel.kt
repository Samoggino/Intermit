package com.samoggino.intermit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(initialPlan: Plan) : ViewModel() {

    var selectedPlan by mutableStateOf(initialPlan)
        private set

    var timeLeft by mutableLongStateOf(selectedPlan.durationMillis)
        private set

    var timerState by mutableStateOf(TimerState.STOPPED)
        private set

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        startTimerLoop()
    }

    private fun startTimerLoop() {
        scope.launch {
            while (true) {
                when (timerState) {
                    TimerState.RUNNING -> {
                        if (timeLeft > 0) {
                            delay(1000)
                            timeLeft = (timeLeft - 1000).coerceAtLeast(0)
                        } else {
                            // Timer finito: fermo il timer
                            timerState = TimerState.STOPPED
                        }
                    }

                    else -> {
                        delay(100)
                    }
                }
            }
        }
    }

    // Start dal valore corrente di timeLeft (senza reset)
    fun start() {
        if (timeLeft <= 0) {
            timeLeft = selectedPlan.durationMillis
        }
        timerState = TimerState.RUNNING
    }

    fun pause() {
        timerState = TimerState.PAUSED
    }

    fun stop() {
        timerState = TimerState.STOPPED
        timeLeft = selectedPlan.durationMillis
    }

    fun selectPlan(plan: Plan) {
        selectedPlan = plan
        timeLeft = plan.durationMillis
        timerState = TimerState.STOPPED
    }

    fun restoreFromSession(plan: Plan, timeLeft: Long, isRunning: Boolean) {
        selectedPlan = plan
        this.timeLeft = timeLeft
        timerState = TimerState.RUNNING
        if (isRunning) {
            start()
        }
    }

}
