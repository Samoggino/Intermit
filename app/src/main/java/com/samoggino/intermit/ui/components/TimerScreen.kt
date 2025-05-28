package com.samoggino.intermit.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.data.model.Plan
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TimerScreen() {
    var selectedPlan by remember { mutableStateOf(Plan.TWENTY_FOUR) }
    var isExpanded by remember { mutableStateOf(false) }

    val totalTime = selectedPlan.durationMillis
    var timeLeft by remember { mutableLongStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    val progress = remember(timeLeft, totalTime) { timeLeft.toFloat() / totalTime.toFloat() }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft = (timeLeft - 1000).coerceAtLeast(0)
        }
        if (timeLeft <= 0) {
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Piano selezionato", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(targetState = isExpanded, label = "planExpansion") { expanded ->
            if (expanded) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Plan.entries.toTypedArray()) { plan ->
                        FilterChip(
                            selected = plan == selectedPlan,
                            onClick = {
                                selectedPlan = plan
                                timeLeft = plan.durationMillis
                                isRunning = false
                                isExpanded = false // torna compatto
                            },
                            label = { Text(text = plan.displayName) }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { isExpanded = true } // solo onClick
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedPlan.displayName,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Espandi",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

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

        Spacer(modifier = Modifier.height(32.dp))

        var isRunning by remember { mutableStateOf(false) }
        var timeLeft by remember { mutableLongStateOf(0L) }
        val selectedPlan = remember { selectedPlan }

        TimerControls(
            isRunning = isRunning,
            onPause = { isRunning = false },
            onStop = {
                isRunning = false
                timeLeft = selectedPlan.durationMillis
            },
            onStart = {
                timeLeft = selectedPlan.durationMillis
                isRunning = true
            }
        )
    }
}