package com.curso.free.data.firebase

import com.curso.free.data.util.loggerError

inline fun android.content.Context.notificationPermission(invoke: (String) -> Unit) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        androidx.core.content.ContextCompat.checkSelfPermission(
            this@notificationPermission,
            android.Manifest.permission.POST_NOTIFICATIONS
        ).let { per ->
            if (per != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                invoke.invoke(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}


fun getFcmToken(invoke: (String) -> Unit, failed: () -> Unit) {
    com.google.firebase.messaging.FirebaseMessaging.getInstance().token.addOnSuccessListener {
        loggerError("fcmToken", it.toString())
        if (it == null) failed.invoke() else invoke.invoke(it)
    }.addOnFailureListener {
        loggerError("fcmToken", it.stackTraceToString())
        failed.invoke()
    }
}

fun subscribeToTopic(courseId: String, invoke: () -> Unit) {
    com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("/topics/$courseId").addOnSuccessListener {
        loggerError("subscribeToTopic", "Done")
        invoke.invoke()
    }.addOnFailureListener {
        loggerError("subscribeToTopic", it.stackTraceToString())
    }
}

fun unsubscribeToTopic(courseId: String) {
    com.google.firebase.messaging.FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/$courseId").addOnSuccessListener {
    }.addOnFailureListener {
        loggerError("subscribeToTopic", it.stackTraceToString())
    }
}

fun getFcmLogout(invoke: () -> Unit) {
    com.google.firebase.messaging.FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
        invoke.invoke()
    }.addOnFailureListener {
        invoke.invoke()
        loggerError("deleteFcmToken", it.stackTraceToString())
    }
}
