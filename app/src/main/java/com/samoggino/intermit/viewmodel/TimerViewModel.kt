package com.samoggino.intermit.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.Plan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(initialPlan: Plan) : ViewModel() {
    var selectedPlan by mutableStateOf(initialPlan)
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
                    Log.d("TimerViewModel", "Tick: timeLeft = $timeLeft")
                } else if (timeLeft <= 0 && isRunning) {
                    Log.d("TimerViewModel", "Timer finito, stoppo")
                    isRunning = false
                } else {
                    delay(100)
                }
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

    fun reset() {
        timeLeft = selectedPlan.durationMillis
        isRunning = false
    }
}

class TimerViewModelFactory(private val plan: Plan) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(plan) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

