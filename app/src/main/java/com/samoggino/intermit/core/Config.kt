package com.samoggino.intermit.core

import com.samoggino.intermit.data.model.Plan

object TimerConfig {
    const val TICK_INTERVAL = 1000L
    val DEFAULT_PLAN: Plan = Plan.ONE
    val DEFAULT_DURATION_MILLIS: Long = DEFAULT_PLAN.durationMillis * 60 * 60 * 1000L
}


object DBConfig {
    const val DB_NAME = "intermit_database"
    const val DB_VERSION = 4
}


