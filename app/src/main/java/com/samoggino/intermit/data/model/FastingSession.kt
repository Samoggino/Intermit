package com.samoggino.intermit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SessionStatus {
    ACTIVE,
    PAUSED,
    COMPLETED,
    STOPPED
}

@Entity(tableName = "fasting_sessions")
data class FastingSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val planHours: Int,
    val endTime: Long? = null,
    val note: String? = null,
    val status: SessionStatus = SessionStatus.ACTIVE,
    val pausedTimeLeft: Long? = null // residuo se messo in pausa
)


fun FastingSession.getPlan(): Plan {
    return Plan.entries.firstOrNull { it.fastingHours == planHours } ?: Plan.SIXTEEN
}

fun FastingSession.getTimeLeftMillis(): Long {
    return when {
        pausedTimeLeft != null -> pausedTimeLeft
        endTime != null -> 0L
        else -> (startTime + getPlan().durationMillis - System.currentTimeMillis()).coerceAtLeast(0L)
    }
}


fun FastingSession.getDurationMillis(): Long {
    return if (endTime != null) {
        endTime - startTime
    } else {
        System.currentTimeMillis() - startTime
    }
}

fun FastingSession.markAs(newStatus: SessionStatus): FastingSession {
    return when (newStatus) {
        SessionStatus.COMPLETED, SessionStatus.STOPPED ->
            copy(status = newStatus, endTime = System.currentTimeMillis())

        else -> copy(status = newStatus)
    }
}
