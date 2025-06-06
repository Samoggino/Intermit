package com.samoggino.intermit.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.data.model.Plan

@Composable
fun PlanSelector(
    selectedPlan: Plan,
    onPlanSelected: (Plan) -> Unit,
    enabled: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.12f
        )
    )
    val contentColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.38f
        )
    )

    Text("Piano selezionato", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))

    AnimatedContent(targetState = isExpanded && enabled, label = "planExpansion") { expanded ->
        if (expanded) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Plan.entries.toList()) { plan ->
                    FilterChip(
                        selected = plan == selectedPlan,
                        onClick = {
                            if (enabled) {
                                onPlanSelected(plan)
                                isExpanded = false
                            }
                        },
                        label = { Text(text = plan.displayName) },
                        enabled = enabled
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .then(
                        if (enabled) Modifier.clickable { isExpanded = true }
                        else Modifier
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedPlan.displayName,
                        color = contentColor
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Espandi",
                        tint = contentColor
                    )
                }
            }
        }
    }
}
