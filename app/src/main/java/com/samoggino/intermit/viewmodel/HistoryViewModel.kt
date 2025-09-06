package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.FastingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val sessionViewModel: SessionRepositoryViewModel
) : ViewModel() {

    // Lista di sessioni selezionate (ID)
    private val _selectedSessions = MutableStateFlow<Set<Long>>(emptySet())
    val selectedSessions: StateFlow<Set<Long>> = _selectedSessions

    /** Seleziona / deseleziona una sessione */
    fun toggleSelection(sessionId: Long) {
        _selectedSessions.value = _selectedSessions.value.toMutableSet().also { set ->
            if (!set.add(sessionId)) set.remove(sessionId)
        }
    }

    /** Seleziona tramite long press */
    fun select(sessionId: Long) {
        _selectedSessions.value = _selectedSessions.value.toMutableSet().also { it.add(sessionId) }
    }

    /** Cancella tutte le sessioni selezionate tramite SessionRepositoryViewModel */
    fun deleteSelected(sessions: List<FastingSession>) {
        val toDelete = sessions.filter { _selectedSessions.value.contains(it.id) }
        viewModelScope.launch {
            toDelete.forEach { sessionViewModel.deleteSession(it) }
            _selectedSessions.value = emptySet()
        }
    }

    /** Cancella una singola sessione (per swipe) */
    fun deleteSession(session: FastingSession) {
        viewModelScope.launch {
            sessionViewModel.deleteSession(session)
            _selectedSessions.value = _selectedSessions.value - session.id
        }
    }

    /** Deseleziona tutte le sessioni */
    fun clearSelection() {
        _selectedSessions.value = emptySet()
    }
}
