package com.curso.free.data.firebase

import com.curso.free.global.base.FIREBASE_SERVER_KEY

interface NotificationRepo {

    @retrofit2.http.Headers("Authorization: key=$FIREBASE_SERVER_KEY", "Content-Type:application/json")
    @retrofit2.http.POST("fcm/send")
    suspend fun postNotification(
        @retrofit2.http.Body notification: PushNotification
    ): retrofit2.Response<okhttp3.ResponseBody>
}