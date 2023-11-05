package com.curso.free.global.util


val android.content.Context.fetchConnectivityManager: android.net.ConnectivityManager
    get() {
        return (androidx.core.content.ContextCompat.getSystemService(
            this@fetchConnectivityManager,
            android.net.ConnectivityManager::class.java
        ) ?: (getSystemService(
            android.content.Context.CONNECTIVITY_SERVICE
        ) as android.net.ConnectivityManager))
    }

fun android.content.Context.isNetworkAvailable(): Boolean = run {
    return@run kotlin.runCatching {
        fetchConnectivityManager.let {
            val nw = it.activeNetwork ?: return@let false
            val actNw = it.getNetworkCapabilities(nw) ?: return@let false
            return@let when {
                actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }
    }.getOrDefault(false)
}
