package com.samoggino.intermit.data.model

enum class Plan(val fastingHours: Int) {
    FOURTEEN(14),
    SIXTEEN(16),
    EIGHTEEN(18),
    TWENTY(20),
    TWENTY_FOUR(24),
    THIRTY_SIX(36),
    FORTY_EIGHT(48),
    SEVENTY_TWO(72);

    val durationMillis: Long
        get() = fastingHours * 60 * 60 * 1000L

    val displayName: String
        get() = "$fastingHours h"
}
