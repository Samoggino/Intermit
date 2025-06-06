package com.samoggino.intermit.data.factory

import com.samoggino.intermit.data.model.FastingSession
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.model.SessionStatus

object SessionFactory {
    fun createSession(
        id: Long? = null,
        startTime: Long,
        endTime: Long? = null,
        plan: Plan = Plan.SIXTEEN,
        note: String? = null,
        status: SessionStatus = SessionStatus.ACTIVE
    ): FastingSession {
        return FastingSession(
            id = id ?: 0L,
            startTime = startTime,
            endTime = endTime ?: 0L,
            planHours = plan.fastingHours,
            note = note ?: "",
            status = status
        )
    }
}
