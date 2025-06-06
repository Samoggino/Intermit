package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.repository.FastingRepository


class HomeViewModelFactory(
    private val timerViewModel: TimerViewModel,
    private val sessionViewModel: SessionRepositoryViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(timerViewModel, sessionViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TimerViewModelFactory(private val plan: Plan) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(plan) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SessionRepositoryViewModelFactory(private val repository: FastingRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionRepositoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionRepositoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
