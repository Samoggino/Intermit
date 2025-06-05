package com.samoggino.intermit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fasting_sessions")
data class FastingSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long = 0L, // 0L indica che il digiuno è ancora in corso
    val note: String?
)

// fai il metodo per calcolare la durata del digiuno
fun FastingSession.getDurationMillis(): Long {
    return if (endTime > 0) {
        endTime - startTime
    } else {
        System.currentTimeMillis() - startTime // Se il digiuno è ancora in corso, calcola la durata fino ad ora
    }
}

// fai il metodo per calcolare la durata del digiuno in secondi
fun FastingSession.getDurationSeconds(): Long {
    return getDurationMillis() / 1000
}
