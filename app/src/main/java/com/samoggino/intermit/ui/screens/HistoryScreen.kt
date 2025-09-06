package com.samoggino.intermit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.R
import com.samoggino.intermit.ui.components.history.HistoryItem
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

                val swipeState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        when (it) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                historyViewModel.deleteSession(session)
                                true
                            }

                            SwipeToDismissBoxValue.StartToEnd -> {
                                // Qui puoi aggiungere un’azione tipo “Completa”
                                true
                            }

                            SwipeToDismissBoxValue.Settled -> true
                        }
                    }
                )

                SwipeToDismissBox(
                    state = swipeState,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundContent = {
                        when (swipeState.dismissDirection) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .drawBehind {
                                            drawRect(
                                                lerp(Color.LightGray, Color.Green, swipeState.progress)
                                            )
                                        }
                                        .padding(start = 16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("Completa", color = Color.White)
                                }
                            }

                            SwipeToDismissBoxValue.EndToStart -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .drawBehind {
                                            drawRect(
                                                lerp(Color.LightGray, Color.Red, swipeState.progress)
                                            )
                                        }
                                        .padding(end = 16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Text("Elimina", color = Color.White)
                                }
                            }

                            SwipeToDismissBoxValue.Settled -> {}
                        }
                    }
                ) {
                    HistoryItem(
                        session = session,
                        isSelected = selectedSessions.contains(session.id),
                        onClick = {
                            if (selectedSessions.isNotEmpty()) {
                                historyViewModel.toggleSelection(session.id)
                            }
                        },
                        onLongClick = { historyViewModel.select(session.id) }
                    )
                }
            }
        }
    }
}
