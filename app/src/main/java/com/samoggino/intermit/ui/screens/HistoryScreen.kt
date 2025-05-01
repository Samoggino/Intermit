package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.samoggino.intermit.viewmodel.MainViewModel
import java.sql.Date

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    val sessions by viewModel.allSessions.observeAsState(emptyList())

    LazyColumn {
        items(sessions) { session ->
            Text("Digiuno iniziato: ${Date(session.startTime)}")
            Text("Protocollo: ${session.protocol}")
            HorizontalDivider()
        }
    }
}

