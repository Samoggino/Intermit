package com.samoggino.intermit.ui.components.history

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.R
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.getDurationMillis
import com.samoggino.intermit.data.model.getPlan
import java.sql.Date

@Composable
fun HistoryItem(
    session: FastingSession,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier // <-- aggiunto parametro Modifier
) {
    val durationMillis = session.getDurationMillis()
    val durationHours = durationMillis / (1000 * 60 * 60)
    val durationMinutes = (durationMillis / (1000 * 60)) % 60

    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> when (session.status) {
            SessionStatus.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
            SessionStatus.PAUSED -> MaterialTheme.colorScheme.secondaryContainer
            SessionStatus.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant
            SessionStatus.STOPPED -> MaterialTheme.colorScheme.errorContainer
        }
    }

    val statusTextRes = when (session.status) {
        SessionStatus.ACTIVE -> R.string.status_active
        SessionStatus.PAUSED -> R.string.status_paused
        SessionStatus.COMPLETED -> R.string.status_completed
        SessionStatus.STOPPED -> R.string.status_stopped
    }
    val statusText = stringResource(id = statusTextRes)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth() // attenzione: puoi concatenare qui altri modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.start, Date(session.startTime)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.status, statusText),
                style = MaterialTheme.typography.bodyMedium,
                color = when (session.status) {
                    SessionStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                    SessionStatus.PAUSED -> MaterialTheme.colorScheme.secondary
                    SessionStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                    SessionStatus.STOPPED -> MaterialTheme.colorScheme.error
                }
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (session.status == SessionStatus.COMPLETED || session.status == SessionStatus.STOPPED) {
                Text(
                    text = stringResource(R.string.duration, durationHours, durationMinutes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                val planName = session.getPlan().displayName
                Text(
                    text = stringResource(R.string.plan, planName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            session.note?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.notes, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
