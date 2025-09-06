package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.ui.components.history.HistoryHeader
import com.samoggino.intermit.ui.components.history.HistorySwipeItem
import com.samoggino.intermit.viewmodel.HistoryViewModel
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel

@Composable
fun HistoryScreen(
    sessionViewModel: SessionRepositoryViewModel,
    historyViewModel: HistoryViewModel
) {
    val sessions by sessionViewModel.allSessions.observeAsState(emptyList())
    val selectedSessions by historyViewModel.selectedSessions.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        HistoryHeader(
            selectedCount = selectedSessions.size,
            onDeleteClick = { historyViewModel.deleteSelectedOrAll(sessions) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sessions, key = { it.id }) { session ->
                HistorySwipeItem(
                    session = session,
                    isSelected = selectedSessions.contains(session.id),
                    onClick = { historyViewModel.toggleSelection(session.id) },
                    onLongClick = { historyViewModel.select(session.id) },
                    onSwipeRight = { historyViewModel.onSwipeRight(session) },
                    onSwipeLeft = { historyViewModel.onSwipeLeft(session) }
                )
            }
        }
    }
}
