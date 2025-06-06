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
    val endTime: Long = 0L,
    val note: String?,
    val status: SessionStatus = SessionStatus.ACTIVE // default
)


fun FastingSession.getPlan(): Plan {
    return Plan.entries.firstOrNull { it.fastingHours == planHours } ?: Plan.SIXTEEN
}

fun FastingSession.getTimeLeftMillis(): Long {
    val now = System.currentTimeMillis()
    val planDuration = getPlan().durationMillis
    return when {
        endTime > 0L -> 0L
        startTime + planDuration > now -> startTime + planDuration - now
        else -> 0L
    }
}

fun FastingSession.getDurationMillis(): Long {
    return if (endTime > 0L) {
        endTime - startTime
    } else {
        System.currentTimeMillis() - startTime
    }
}