package com.samoggino.intermit.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.samoggino.intermit.data.model.NotificationState

object SnackbarNotificationManager {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appContext: Context

    internal const val CHANNEL_ID = "intermit_fasting_channel_id"
    private const val CHANNEL_NAME = "Fasting Progress Updates"
    private const val NOTIFICATION_ID = 4321

    fun initialize(context: Context, manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= 36) {
            notificationManager = manager
            appContext = context.applicationContext
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
                description = "Notifiche in tempo reale per le fasi del digiuno"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun start() {
        if (Build.VERSION.SDK_INT >= 36) {
            for (state in NotificationState.entries) {
                val notification = state.buildNotification(appContext).build()
                Handler(Looper.getMainLooper()).postDelayed({
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }, state.delay)
            }
        } else {
            // do nothing
        }
    }
}
