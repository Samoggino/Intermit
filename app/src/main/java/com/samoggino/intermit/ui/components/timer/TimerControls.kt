package com.samoggino.intermit.ui.components.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.data.model.TimerState
import com.samoggino.intermit.ui.theme.LightGreen
import com.samoggino.intermit.ui.theme.LightRed
import com.samoggino.intermit.ui.theme.LightYellow

@Composable
fun TimerControls(
    isRunning: Boolean,
    isPaused: Boolean,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onStart: () -> Unit,
    onResume: () -> Unit
) {
    val timerState = when {
        isRunning -> TimerState.RUNNING
        isPaused -> TimerState.PAUSED
        else -> TimerState.STOPPED
    }

    AnimatedContent(
        targetState = timerState,
        transitionSpec = {
            slideInHorizontally { width -> width } + fadeIn() togetherWith
                    slideOutHorizontally { width -> -width } + fadeOut()
        },
        label = "TimerControlsTransition"
    ) { state ->
        when (state) {
            TimerState.RUNNING -> {
                Row {
                    Button(
                        onClick = onPause,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightYellow,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Pausa")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightRed,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Stop")
                    }
                }
            }

            TimerState.PAUSED -> {
                Row {
                    Button(
                        onClick = onResume,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Riprendi")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightRed,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Stop")
                    }
                }
            }

            TimerState.STOPPED -> {
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Avvia")
                }
            }
        }
    }
}