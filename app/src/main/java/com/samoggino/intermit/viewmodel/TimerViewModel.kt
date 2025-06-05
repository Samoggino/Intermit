package com.samoggino.intermit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.Plan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    var selectedPlan by mutableStateOf(Plan.TWENTY_FOUR)
        private set

    var timeLeft by mutableLongStateOf(selectedPlan.durationMillis)
        private set

    var isRunning by mutableStateOf(false)
        private set

    val progress: Float
        get() = timeLeft.toFloat() / selectedPlan.durationMillis.toFloat()

    init {
        viewModelScope.launch {
            while (true) {
                if (isRunning && timeLeft > 0) {
                    delay(1000)
                    timeLeft = (timeLeft - 1000).coerceAtLeast(0)
                } else if (timeLeft <= 0 && isRunning) {
                    isRunning = false
                }
                delay(200)
            }
        }
    }

    fun start() {
        timeLeft = selectedPlan.durationMillis
        isRunning = true
    }

    fun pause() {
        isRunning = false
    }

    fun stop() {
        isRunning = false
        timeLeft = selectedPlan.durationMillis
    }

    fun selectPlan(plan: Plan) {
        selectedPlan = plan
        timeLeft = plan.durationMillis
        isRunning = false
    }
}
