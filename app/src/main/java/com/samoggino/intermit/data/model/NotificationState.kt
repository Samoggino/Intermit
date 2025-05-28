package com.samoggino.intermit.data.model

import android.app.Notification
import android.content.Context
import androidx.core.graphics.drawable.IconCompat
import com.samoggino.intermit.R
import com.samoggino.intermit.utils.SnackbarNotificationManager

enum class NotificationState(val delay: Long) {
    FED(2000L),
    EARLY_FASTING(9000L),
    FAT_BURNING(12000L),
    KETOSIS(15000L),
    DEEP_KETOSIS(18000L),
    AUTOPHAGY(21000L);

    fun buildNotification(appContext: Context): Notification.Builder {
        val stateContents = mapOf(
            FED to NotificationContent(
                appContext.getString(R.string.FED_state),
                appContext.getString(R.string.FED_description)
            ),
            EARLY_FASTING to NotificationContent(
                appContext.getString(R.string.EARLY_FASTING_state),
                appContext.getString(R.string.EARLY_FASTING_description)
            ),
            FAT_BURNING to NotificationContent(
                appContext.getString(R.string.FAT_BURNING_state),
                appContext.getString(R.string.FAT_BURNING_description)
            ),
            KETOSIS to NotificationContent(
                appContext.getString(R.string.KETOSIS_state),
                appContext.getString(R.string.KETOSIS_description)
            ),
            DEEP_KETOSIS to NotificationContent(
                appContext.getString(R.string.DEEP_KETOSIS_state),
                appContext.getString(R.string.DEEP_KETOSIS_description)
            ),
            AUTOPHAGY to NotificationContent(
                appContext.getString(R.string.AUTOPHAGY_state),
                appContext.getString(R.string.AUTOPHAGY_description)
            )
        )

        val content = stateContents[this] ?: NotificationContent("Unknown", "No description")

        return Notification.Builder(appContext, SnackbarNotificationManager.CHANNEL_ID)
            .setContentTitle(content.title)
            .setContentText(content.text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(buildBaseProgressStyle(this))
    }

    companion object {
        fun buildBaseProgressStyle(state: NotificationState): Notification.Style {
            // dummy style - implement your ProgressStyle if needed
            return Notification.BigTextStyle().bigText("Stage: ${state.name}")
        }
    }
}
