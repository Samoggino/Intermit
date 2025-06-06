package com.samoggino.intermit.data.factory

import com.samoggino.intermit.data.model.FastingSession

object SessionFactory {
    fun createSession(
        id: Long? = null,
        startTime: Long,
        endTime: Long? = null,
        note: String? = null
    ): FastingSession {
        return FastingSession(
            id = id ?: 0L,
            startTime = startTime,
            endTime = endTime ?: 0L,
            note = note ?: ""
        )
    }
}