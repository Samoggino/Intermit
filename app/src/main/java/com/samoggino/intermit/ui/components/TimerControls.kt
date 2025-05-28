package com.samoggino.intermit.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.samoggino.intermit.ui.theme.LightRed

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerControls(
    isRunning: Boolean,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onStart: () -> Unit
) {
    AnimatedContent(
        targetState = isRunning,
        transitionSpec = {
            if (targetState) {
                // Transizione da "Avvia" a "Pausa" e "Stop"
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut())
            } else {
                // Transizione da "Pausa" e "Stop" a "Avvia"
                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut())
            }
        },
        label = "TimerControlsTransition"
    ) { running ->
        if (running) {
            Row {
                Button(onClick = onPause) {
                    Text("Pausa")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onStop,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightRed, // Rosso tenue
                        contentColor = Color.Black
                    )
                ) {
                    Text("Stop")
                }
            }
        } else {
            Button(onClick = onStart) {
                Text("Avvia")
            }
        }
    }
}
