package com.samoggino.intermit.utils

import com.samoggino.intermit.data.model.NotificationState

fun getFastingState(hours: Int): NotificationState? {
//    return NotificationState.entries.firstOrNull { hours in it.hoursRange }
    // avrò l'ora di inizio e devo verifare se il delay è passato,
    // nel caso sia passato ritorno quello stato, altrimenti controllo lo stato successivo
    return NotificationState.entries.firstOrNull { it.delay <= hours * 3600 * 1000L }

}
