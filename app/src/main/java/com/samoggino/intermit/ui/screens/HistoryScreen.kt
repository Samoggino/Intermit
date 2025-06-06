package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.model.getDurationMillis
import com.samoggino.intermit.data.model.getPlan
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import java.sql.Date

@Composable
fun HistoryScreen(viewModel: SessionRepositoryViewModel) {
    val sessions by viewModel.allSessions.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { viewModel.deleteAllSessions() }
        ) {
            Text(text = "Elimina tutte le sessioni")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sessions) { session ->
                SessionItem(session = session)
            }
        }
    }
}

@Composable
fun SessionItem(session: FastingSession) {
    val durationMillis = session.getDurationMillis()
    val durationHours = durationMillis / (1000 * 60 * 60)
    val durationMinutes = (durationMillis / (1000 * 60)) % 60

    val statusColor = when (session.status) {
        SessionStatus.ACTIVE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        SessionStatus.PAUSED -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
        SessionStatus.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant
        SessionStatus.STOPPED -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
    }

    val statusText = when (session.status) {
        SessionStatus.ACTIVE -> "In corso"
        SessionStatus.PAUSED -> "In pausa"
        SessionStatus.COMPLETED -> "Completato"
        SessionStatus.STOPPED -> "Interrotto"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = statusColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Inizio: ${Date(session.startTime)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Stato: $statusText",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = when (session.status) {
                        SessionStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                        SessionStatus.PAUSED -> MaterialTheme.colorScheme.secondary
                        SessionStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                        SessionStatus.STOPPED -> MaterialTheme.colorScheme.error
                    }
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (session.status == SessionStatus.COMPLETED || session.status == SessionStatus.STOPPED) {
                Text(
                    text = "Durata: ${durationHours}h ${durationMinutes}m",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (session.status == SessionStatus.ACTIVE || session.status == SessionStatus.PAUSED) {
                val planName = session.getPlan().displayName
                Text(
                    text = "Piano: $planName",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            session.note?.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Note: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
