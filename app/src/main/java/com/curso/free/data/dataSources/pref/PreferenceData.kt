package com.curso.free.data.dataSources.pref

import com.curso.free.data.model.Preference

class PreferenceData(
    private val repository: PrefRepo
) {

    suspend fun prefs(invoke: List<Preference>.() -> Unit): Unit = repository.prefs(invoke)

    suspend fun insertPref(pref: Preference): Preference? = repository.insertPref(pref)

    suspend fun updatePref(pref: Preference, newValue: String): Preference = repository.updatePref(pref, newValue)

    suspend fun deletePref(key: String): Int = repository.deletePref(key)

    suspend fun deletePrefAll(): Int= repository.deletePrefAll()

}
