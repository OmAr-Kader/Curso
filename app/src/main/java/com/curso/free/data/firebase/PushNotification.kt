package com.curso.free.data.firebase

data class PushNotification(
    val data: NotificationData,
    val topic: String,
    val to: String
)

data class NotificationData(
    val title: String,
    val message: String,
    val routeKey: String,
    val argOne: String,
    val argTwo: String,
    val argThree: Int,
)