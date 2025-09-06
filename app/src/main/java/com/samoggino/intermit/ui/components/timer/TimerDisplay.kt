package com.samoggino.intermit.ui.components.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.utils.toHMSString

@Composable
fun TimerDisplay(
    timeLeft: Long,
    progress: Float
) {

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(325.dp)) {
        // sfondo (track) con colore “soft” e stroke più spesso
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            strokeWidth = 16.dp,  // stroke più spesso nello stile expressive
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            strokeCap = StrokeCap.Round, // estremità arrotondate
        )

        // cerchio principale che si svuota
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 16.dp,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
            strokeCap = StrokeCap.Round,
        )

        // testo centrale
        Text(
            text = timeLeft.toHMSString(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


