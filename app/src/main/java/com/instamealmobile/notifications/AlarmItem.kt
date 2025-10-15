package com.instamealmobile.notifications

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val message: String,
    val icon: Int
)