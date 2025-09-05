package com.samoggino.intermit.data.repository

import com.samoggino.intermit.data.model.Plan

interface PlanRepository {
    suspend fun getActivePlan(): Plan?
}
