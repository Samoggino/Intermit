package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.ui.components.PlanSelector
import com.samoggino.intermit.ui.components.TimerControls
import com.samoggino.intermit.ui.components.TimerDisplay
import com.samoggino.intermit.viewmodel.TimerViewModel

@Composable
fun HomeScreen(timerViewModel: TimerViewModel) {
    val selectedPlan = timerViewModel.selectedPlan
    val timeLeft = timerViewModel.timeLeft
    val isRunning = timerViewModel.isRunning
    val progress = timerViewModel.progress

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PlanSelector(
            selectedPlan = selectedPlan,
            onPlanSelected = { timerViewModel.selectPlan(it) }
        )

        TimerDisplay(
            timeLeft = timeLeft,
            progress = progress
        )

        Spacer(modifier = Modifier.height(32.dp))

        TimerControls(
            isRunning = isRunning,
            onPause = { timerViewModel.pause() },
            onStop = { timerViewModel.stop() },
            onStart = { timerViewModel.start() }
        )
    }
}
