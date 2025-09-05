package com.samoggino.intermit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanViewModel(
    private val planRepository: PlanRepository
) : ViewModel() {

    private val _selectedPlan = MutableStateFlow<Plan?>(null)
    val selectedPlan: StateFlow<Plan?> = _selectedPlan

    init {
        viewModelScope.launch {
            val activePlan = planRepository.getActivePlan()
            _selectedPlan.value = activePlan
        }
    }

    fun selectPlan(plan: Plan) {
        _selectedPlan.value = plan
    }
}
