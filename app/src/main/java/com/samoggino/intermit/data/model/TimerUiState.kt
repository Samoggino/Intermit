package com.samoggino.intermit.data.model

data class TimerUiState(
    val selectedPlan: Plan,
    val timeLeft: Long,
    val timerState: TimerState
) {
    val isRunning get() = timerState == TimerState.RUNNING
    val isPaused get() = timerState == TimerState.PAUSED
    val progress: Float
        get() =
            if (selectedPlan.durationMillis > 0)
                timeLeft.toFloat() / selectedPlan.durationMillis.toFloat()
            else 0f
}
