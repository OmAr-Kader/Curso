package com.curso.free.data.dataSources.pref

import com.curso.free.data.model.Preference
import com.curso.free.data.util.REALM_FAILED
import com.curso.free.data.util.REALM_SUCCESS
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class PrefRepoImp(val realm: Realm) : PrefRepo {

    override suspend fun prefs(invoke: List<Preference>.() -> Unit) {
        return kotlinx.coroutines.coroutineScope {
            kotlin.runCatching {
                realm.query(Preference::class).find()
            }.getOrNull()?.let {
                invoke.invoke(it)
            } ?: invoke.invoke(emptyList())
        }
    }

    override suspend fun insertPref(pref: Preference): Preference? {
        return realm.write {
            try {
                copyToRealm(pref, UpdatePolicy.ALL)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun updatePref(pref: Preference, newValue: String): Preference {
        return realm.write {
            return@write query<Preference>("ketString == $0", pref.ketString).first().find()?.also {
                it.value = newValue
            }
        }?.let {
            return it
        } ?: realm.write {
            kotlin.run {
                copyToRealm(pref, UpdatePolicy.ALL)
                pref
            }
        }
    }

    override suspend fun deletePref(key: String): Int {
        return realm.write {
            try {
                query<Preference>("ketString == $0", key).first().find()?.let {
                    delete(it)
                }
            } catch (e: Exception) {
                com.curso.free.data.util.loggerError("editCourse", e.stackTraceToString())
                return@write null
            }
        }.let {
            return@let if (it == null) REALM_FAILED else REALM_SUCCESS
        }
    }

    override suspend fun deletePrefAll(): Int {
        return realm.write {
            kotlin.runCatching {
                delete(schemaClass = Preference::class)
            }.getOrNull()
        }.let {
            return@let if (it == null) REALM_FAILED else REALM_SUCCESS
        }
    }

}