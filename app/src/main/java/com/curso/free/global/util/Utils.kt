package com.curso.free.global.util

inline fun <C, R> List<C>.ifNotEmpty(defaultValue: List<C>.() -> R): R? = if (isNotEmpty()) defaultValue() else null

inline fun <R> String.ifNotEmpty(defaultValue: String.() -> R): R? = if (isNotEmpty()) defaultValue() else null

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
inline val String.firstSpace: String
    get() = indexOf(" ").let { if (it == -1) this else substring(0, it) }

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
inline val String.firstCapital: String
    get() = toCharArray().apply {
        this[0] = this[0].uppercaseChar()
    }.let {
        String(it)
    }

internal val Long.fetchHour: Int
    get() = java.util.Calendar.getInstance().apply {
        timeInMillis = this@fetchHour
    }[java.util.Calendar.HOUR_OF_DAY]

internal val Long.fetchMinute: Int
    get() = java.util.Calendar.getInstance().apply {
        timeInMillis = this@fetchMinute
    }[java.util.Calendar.MINUTE]


internal val Long.fetchTimeFromCalender: (hour: Int, minute: Int) -> Long
    get() = { hour, minute ->
        java.util.Calendar.getInstance().apply {
            timeInMillis = this@fetchTimeFromCalender
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
        }.timeInMillis
    }
/*
@androidx.compose.runtime.Composable
fun Int.pxToDp() = with(androidx.compose.ui.platform.LocalDensity.current) { this@pxToDp.toDp() }

*/