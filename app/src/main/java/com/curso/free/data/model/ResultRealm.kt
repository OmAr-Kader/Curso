package com.curso.free.data.model

data class ResultRealm<T>(
    val value: T,
    @androidx.annotation.IntRange(from = -3, to = 3)
    val result: Int,
)