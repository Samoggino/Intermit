package com.samoggino.intermit.utils

import com.samoggino.intermit.data.model.State

fun getFastingState(hours: Int): State? {
    return State.entries.firstOrNull { hours in it.start until it.end }
}
