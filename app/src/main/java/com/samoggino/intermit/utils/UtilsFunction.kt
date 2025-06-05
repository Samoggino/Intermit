package com.samoggino.intermit.utils

import com.samoggino.intermit.data.model.NotificationState
import java.util.Locale

fun getFastingState(hours: Int): NotificationState? {
//    return NotificationState.entries.firstOrNull { hours in it.hoursRange }
    // avrò l'ora di inizio e devo verifare se il delay è passato,
    // nel caso sia passato ritorno quello stato, altrimenti controllo lo stato successivo
    return NotificationState.entries.firstOrNull { it.delay <= hours * 3600 * 1000L }

}

fun Long.toHours(): Long = this / 1000 / 3600

fun Long.toMinutes(): Long = (this / 1000 / 60) % 60

fun Long.toSeconds(): Long = (this / 1000) % 60

fun Long.toHMSString(): String {
    val h = this.toHours()
    val m = this.toMinutes()
    val s = this.toSeconds()
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
}
