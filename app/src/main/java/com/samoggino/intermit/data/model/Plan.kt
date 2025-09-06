package com.samoggino.intermit.data.model

enum class Plan(val fastingHours: Int) {
    ONE(fastingHours = 1),
    SIX(fastingHours = 6),
    TWO(fastingHours = 2),
    FOURTEEN(fastingHours = 14),
    SIXTEEN(fastingHours = 16),
    EIGHTEEN(fastingHours = 18),
    TWENTY(fastingHours = 20),
    TWENTY_FOUR(fastingHours = 24),
    THIRTY_SIX(fastingHours = 36),
    FORTY_EIGHT(fastingHours = 48),
    SEVENTY_TWO(fastingHours = 72);

    val durationMillis: Long
        get() = fastingHours * 60 * 60 * 1000L

    val displayName: String
        get() = "$fastingHours h"
}
