package com.samoggino.intermit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun TimerWidget() {
    val totalTime = 60000L  // 60 secondi
    val timeLeft = remember { mutableLongStateOf(totalTime) }  // Tempo rimanente
    val isRunning = remember { mutableStateOf(false) }  // Stato del timer: se è in esecuzione

    // Progressione animata da 0 a 1 per la CircularProgressIndicator
    val progress = remember { mutableFloatStateOf(1f) }

    // Effetto lanciato per il timer
    LaunchedEffect(isRunning.value) {
        while (isRunning.value && timeLeft.longValue > 0) {
            delay(16)  // Aspetta circa 16 millisecondi per un aggiornamento fluido (60 fps)
            timeLeft.longValue -= 16  // Decresce di 16 millisecondi
            progress.floatValue =
                timeLeft.longValue.toFloat() / totalTime.toFloat()  // Aggiorna il progresso
        }
        if (timeLeft.longValue <= 0) {
            isRunning.value = false  // Ferma il timer quando finisce
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Barra di progresso che diminuisce con il tempo
        CircularProgressIndicator(
            progress = { progress.value },
            modifier = Modifier.size(200.dp),  // Dimensione del timer circolare
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 16.dp,  // Spessore della barra di progresso
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostra il tempo rimanente in formato secondi
        Text(
            text = "Time left: ${timeLeft.longValue / 1000} sec",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Pulsante Start/Pause
        Button(
            onClick = {
                if (isRunning.value) {
                    isRunning.value = false  // Ferma il timer se è in esecuzione
                } else {
                    timeLeft.longValue = totalTime  // Resetta il timer
                    isRunning.value = true  // Avvia il timer
                }
            }
        ) {
            Text(text = if (isRunning.value) "Pause" else "Start")
        }
    }
}