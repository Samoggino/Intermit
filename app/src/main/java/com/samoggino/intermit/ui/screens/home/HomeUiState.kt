package com.samoggino.intermit.ui.screens.home

import com.samoggino.intermit.data.model.Plan

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Ready(
        val selectedPlan: Plan,
        val timeLeft: Long,
        val isRunning: Boolean,
        val progress: Float
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}