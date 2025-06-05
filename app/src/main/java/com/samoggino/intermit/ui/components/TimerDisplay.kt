package com.samoggino.intermit.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TimerDisplay(
    timeLeft: Long,
    progress: Float
) {
    Spacer(modifier = Modifier.height(32.dp))

    CircularProgressIndicator(
        progress = { progress },
        modifier = Modifier.size(200.dp),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 16.dp,
        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    )

    Spacer(modifier = Modifier.height(16.dp))

    val minutesLeft = (timeLeft / 1000 / 60) % 60
    val hoursLeft = (timeLeft / 1000 / 60 / 60)

    Text(
        text = String.format(
            Locale.getDefault(),
            "Tempo restante: %02d:%02d",
            hoursLeft,
            minutesLeft
        ),
        style = MaterialTheme.typography.bodyLarge
    )
}
