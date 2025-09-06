package com.samoggino.intermit.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samoggino.intermit.ui.components.PlanSelector
import com.samoggino.intermit.ui.components.timer.TimerControls
import com.samoggino.intermit.ui.components.timer.TimerDisplay
import com.samoggino.intermit.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is HomeUiState.Loading -> CircularProgressIndicator()
        is HomeUiState.Error -> Text("Errore: ${(state as HomeUiState.Error).message}")
        is HomeUiState.Ready -> {
            val ui = state as HomeUiState.Ready

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlanSelector(
                    selectedPlan = ui.selectedPlan,
                    onPlanSelected = { viewModel.onPlanSelected(it) },
                    enabled = !ui.isRunning  // disabilita se il timer è in esecuzione
                )

                Spacer(modifier = Modifier.height(32.dp))


                TimerDisplay(
                    timeLeft = ui.timeLeft,
                    progress = ui.progress
                )

                Spacer(modifier = Modifier.height(32.dp))


                TimerControls(
                    isRunning = ui.isRunning,
                    isPaused = ui.isPaused,  // aggiunto campo
                    onStart = { viewModel.onStartClicked() },
                    onPause = { viewModel.onPauseClicked() },
                    onStop = { viewModel.onStopClicked() },
                    onResume = { viewModel.onResumeClicked() }
                )
            }
        }
    }
}