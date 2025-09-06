package com.samoggino.intermit.ui.components.history

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.data.model.FastingSession
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HistorySwipeItem(
    session: FastingSession,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val swipeOffset = remember { Animatable(0f) }
    val triggeredRight = remember { mutableStateOf(false) }
    val triggeredLeft = remember { mutableStateOf(false) }
    val threshold = 200f
    val swipeScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .pointerInput(session.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = (swipeOffset.value + dragAmount.x)
                            .coerceIn(-size.width.toFloat(), size.width.toFloat())

                        swipeScope.launch { swipeOffset.snapTo(newOffset) }

                        // Vibrazione a destra
                        if (newOffset > threshold && !triggeredRight.value) {
                            haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                            triggeredRight.value = true
                        } else if (newOffset <= threshold) {
                            triggeredRight.value = false
                        }

                        // Vibrazione a sinistra
                        if (newOffset < -threshold && !triggeredLeft.value) {
                            haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                            triggeredLeft.value = true
                        } else if (newOffset >= -threshold) {
                            triggeredLeft.value = false
                        }
                    },
                    onDragEnd = {
                        // Logica swipe finale
                        if (swipeOffset.value > threshold) onSwipeRight()
                        if (swipeOffset.value < -threshold) onSwipeLeft()

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
        // Background dinamico
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
            isSelected = isSelected,
            onClick = onClick,
            onLongClick = onLongClick,
            modifier = Modifier.offset { IntOffset(swipeOffset.value.roundToInt(), 0) }
        )
    }
}
