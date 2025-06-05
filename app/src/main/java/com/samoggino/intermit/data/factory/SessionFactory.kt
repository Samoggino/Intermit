package com.samoggino.intermit.data.factory

import com.samoggino.intermit.data.model.FastingSession

object SessionFactory {
    fun createSession(
        startTime: Long,
        endTime: Long? = null,
        note: String? = null
    ): FastingSession {
        return FastingSession(
            startTime = startTime,
            endTime = endTime ?: 0L,
            note = note ?: ""
        )
    }
}
