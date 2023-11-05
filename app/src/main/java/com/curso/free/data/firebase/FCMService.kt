package com.curso.free.data.firebase

import com.curso.free.data.util.logger
import com.google.firebase.messaging.RemoteMessage

class FCMService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    private fun sendNotification(message: RemoteMessage) {
        val messageData = message.data
        logger("sendNotification", message.data["routeKey"].toString())
        val intent = android.content.Intent(this, com.curso.free.MainActivity::class.java).apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("routeKey", messageData["routeKey"])
            putExtra("argOne", messageData["argOne"])
            putExtra("argTwo", messageData["argTwo"])
            putExtra("argThree", messageData["argThree"])
        }

        val pendingIntent = android.app.PendingIntent.getActivity(
            this, 0, intent, android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = this.getString(com.curso.free.R.string.default_notification_channel_id)

        val notificationBuilder = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setContentTitle(messageData["title"])
            .setContentText(messageData["message"])
            .setSmallIcon(com.curso.free.R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(channelId, channelId, android.app.NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(kotlin.random.Random.nextInt(), notificationBuilder.build())
    }

    @Suppress("RedundantOverride")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Checking while the next App launch
    }
}