package com.curso.free.data.dataSources.pref

import com.curso.free.data.model.Preference

interface PrefRepo {

    suspend fun prefs(invoke: List<Preference>.() -> Unit)

    suspend fun insertPref(pref: Preference): Preference?

    suspend fun updatePref(pref: Preference, newValue: String): Preference

    suspend fun deletePref(key: String): Int

    suspend fun deletePrefAll(): Int

}