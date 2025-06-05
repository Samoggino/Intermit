package com.samoggino.intermit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.repository.FastingRepository
import kotlinx.coroutines.launch

class SessionRepositoryViewModel(private val repository: FastingRepository) : ViewModel() {

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
}

class SessionRepositoryViewModelFactory(private val repository: FastingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionRepositoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionRepositoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

