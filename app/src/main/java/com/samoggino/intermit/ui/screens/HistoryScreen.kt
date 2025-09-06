package com.samoggino.intermit.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.R
import com.samoggino.intermit.ui.components.history.HistoryItem
import com.samoggino.intermit.viewmodel.HistoryViewModel
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HistoryScreen(
    sessionViewModel: SessionRepositoryViewModel,
    historyViewModel: HistoryViewModel
) {
    val sessions by sessionViewModel.allSessions.observeAsState(emptyList())
    val selectedSessions by historyViewModel.selectedSessions.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize()) {

        // Pulsante elimina selezioni o tutto
        if (selectedSessions.isNotEmpty()) {
            Button(
                onClick = { historyViewModel.deleteSelected(sessions) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Elimina (${selectedSessions.size})")
            }
        } else {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { sessionViewModel.deleteAllSessions() }
            ) {
                Text(text = stringResource(R.string.delete_all_sessions))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sessions, key = { it.id }) { session ->

                val swipeOffset = remember { Animatable(0f) }
                val triggeredRight = remember { mutableStateOf(false) }
                val triggeredLeft = remember { mutableStateOf(false) }
                val threshold = 200f // soglia in px per vibrazione

                val swipeScope = rememberCoroutineScope() // crea un CoroutineScope valido


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val newOffset = (swipeOffset.value + dragAmount.x)
                                        .coerceIn(-size.width.toFloat(), size.width.toFloat())

                                    swipeScope.launch {
                                        swipeOffset.snapTo(newOffset)
                                    }

                                    // Vibrazione destra
                                    if (swipeOffset.value > threshold && !triggeredRight.value) {
                                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        triggeredRight.value = true
                                    } else if (swipeOffset.value < threshold) {
                                        triggeredRight.value = false
                                    }

                                    // Vibrazione sinistra
                                    if (swipeOffset.value < -threshold && !triggeredLeft.value) {
                                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        triggeredLeft.value = true
                                    } else if (swipeOffset.value > -threshold) {
                                        triggeredLeft.value = false
                                    }
                                },
                                onDragEnd = {
                                    // Logica di swipe
                                    swipeScope.launch {
                                        swipeOffset.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                    }
                                }
                            )
                        }

                ) {
                    // Background colore dinamico
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                when {
                                    swipeOffset.value > 0f -> Color.Green.copy(alpha = (swipeOffset.value / threshold).coerceIn(0f, 1f))
                                    swipeOffset.value < 0f -> Color.Red.copy(alpha = (-swipeOffset.value / threshold).coerceIn(0f, 1f))
                                    else -> Color.Transparent
                                },
                                RoundedCornerShape(8.dp)
                            )
                    )

                    HistoryItem(
                        session = session,
                        isSelected = selectedSessions.contains(session.id),
                        onClick = { if (selectedSessions.isNotEmpty()) historyViewModel.toggleSelection(session.id) },
                        onLongClick = { historyViewModel.select(session.id) },
                        modifier = Modifier.offset { IntOffset(swipeOffset.value.roundToInt(), 0) } // <-- qui
                    )

                }
            }
        }
    }
}
