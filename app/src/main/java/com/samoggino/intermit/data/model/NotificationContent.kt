package com.samoggino.intermit.data.model

data class NotificationContent(
    val title: String,
    val text: String,
    val iconRes: Int? = null // opzionale
)
