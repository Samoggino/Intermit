package com.samoggino.intermit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.SessionStatus
import com.samoggino.intermit.data.repository.FastingRepository
import kotlinx.coroutines.launch

class SessionRepositoryViewModel(
    private val repository: FastingRepository
) : ViewModel() {

    val allSessions: LiveData<List<FastingSession>> = repository.allSessions.asLiveData()

    fun insertSession(session: FastingSession, onResult: (Long) -> Unit) = viewModelScope.launch {
        val id = repository.insert(session)
        onResult(id)
    }

    fun updateSession(session: FastingSession) = viewModelScope.launch {
        repository.update(session)
    }

    fun deleteSession(session: FastingSession) = viewModelScope.launch {
        repository.delete(session)
    }

    fun deleteAllSessions() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun restoreLastActiveSession(onResult: (FastingSession?) -> Unit) = viewModelScope.launch {
        val session = repository.getCurrentNonTerminatedSession()
        onResult(session)
    }

    fun markSessionAs(session: FastingSession, newStatus: SessionStatus) = viewModelScope.launch {
        val updated = session.copy(status = newStatus)
        updateSession(updated)
        Log.d("SessionRepositoryViewModel", "Session ${session.id} marked as $newStatus")
    }

    fun markSessionAs(sessionId: Long?, newStatus: SessionStatus) = viewModelScope.launch {
        if (sessionId != null) {
            val session = repository.getSessionById(sessionId)
            if (session != null) {
                markSessionAs(session, newStatus)
            }
        }
    }

    fun pauseSession(session: FastingSession, remainingTime: Long) = viewModelScope.launch {
        val updated = session.copy(
            status = SessionStatus.PAUSED,
            pausedTimeLeft = remainingTime
        )
        updateSession(updated)
    }

    suspend fun getSessionById(id: Long): FastingSession? {
        return repository.getSessionById(id)
    }

}