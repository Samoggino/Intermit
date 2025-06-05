package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import java.sql.Date

@Composable
fun HistoryScreen(viewModel: SessionRepositoryViewModel) {
    val sessions by viewModel.allSessions.observeAsState(emptyList())

    LazyColumn {

        item {

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    viewModel.deleteAllSessions()
                },
            ){
                Text(text = "Elimina tutte le sessioni")
            }
        }
        items(sessions) { session ->
            Text("Digiuno iniziato: ${Date(session.startTime)}")
            HorizontalDivider()
        }
    }
}

