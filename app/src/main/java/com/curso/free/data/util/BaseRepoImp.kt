package com.curso.free.data.util

import com.curso.free.data.model.ResultRealm
import io.realm.kotlin.mongodb.subscriptions

abstract class BaseRepoImp(val realmSync: RealmSync) {

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> insert(
        item: T,
        updatePolicy: io.realm.kotlin.UpdatePolicy = io.realm.kotlin.UpdatePolicy.ALL
    ): ResultRealm<T?> {
        return realmSync.cloud?.write {
            return@write try {
                copyToRealm(item, updatePolicy)
            } catch (e: Exception) {
                loggerError("insertSync", e.stackTraceToString())
                null
            }
        }.let {
            return@let ResultRealm(it, if (it == null) REALM_FAILED else REALM_SUCCESS)
        }
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> edit(
        id: org.mongodb.kbson.ObjectId,
        crossinline edit: T.() -> T
    ): ResultRealm<T?> {
        return realmSync.cloud?.write {
            return@write query(T::class, "_id == $0", id).first().find()?.let {
                return@let edit(it)
            }
        }.let {
            ResultRealm(it, if (it != null) REALM_SUCCESS else REALM_FAILED)
        }
    }

    suspend inline fun <reified T : io.realm.kotlin.types.TypedRealmObject> delete(query: String, vararg args: Any?): Int {
        return realmSync.cloud?.write {
            try {
                query(T::class, query, *args).first().find()?.let {
                    delete(it)
                }
            } catch (e: Exception) {
                return@write null
            }
        }.let {
            return@let if (it == null) REALM_FAILED else REALM_SUCCESS
        }
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> findSubscription(
        queryName: String,
        query: String,
        args: Array<Any>,
    ): io.realm.kotlin.query.RealmQuery<T>? = kotlinx.coroutines.coroutineScope {
        realmSync.cloud?.subscriptions?.findByName(queryName)?.asQuery(T::class).let { q ->
            q ?: realmSync.cloud?.subscriptions?.update {
                add(
                    query = it.query(clazz = T::class, query = query, args = args),
                    name = queryName,
                    updateExisting = true,
                )
            }.let {
                it?.findByName(queryName)?.asQuery(T::class)
            }
        }
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> querySingleFlow(
        queryName: String,
        query: String,
        args: Array<Any>,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<T>>? {
        return findSubscription<T>(queryName = queryName, query = query, args = args)?.first()?.asFlow()
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> querySingle(
        crossinline invoke: ResultRealm<T?>.() -> Unit,
        queryName: String,
        query: String,
        args: Array<Any>,
    ) {
        findSubscription<T>(queryName = queryName, query = query, args = args)?.let { que ->
            kotlinx.coroutines.coroutineScope {
                kotlin.runCatching {
                    que.first().find()
                }.getOrNull()?.let {
                    invoke.invoke(ResultRealm(it, REALM_SUCCESS))
                } ?: invoke.invoke(ResultRealm(null, REALM_FAILED))
            }
        } ?: invoke.invoke(ResultRealm(null, REALM_FAILED))
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> query(
        crossinline invoke: ResultRealm<List<T>>.() -> Unit,
        queryName: String,
        query: String,
        args: Array<Any>,
    ) {
        findSubscription<T>(queryName = queryName, query = query, args = args)?.let { que ->
            kotlinx.coroutines.coroutineScope {
                kotlin.runCatching {
                    que.find()
                }.getOrNull()?.let {
                    loggerError("realmSync", it.size.toString())
                    invoke.invoke(ResultRealm(it, REALM_SUCCESS))
                } ?: kotlin.run {
                    loggerError("realmSync", "REALM_FAILED")
                    invoke.invoke(ResultRealm(emptyList(), REALM_FAILED))
                }
            }
        }
    }

    suspend inline fun <reified T : io.realm.kotlin.types.RealmObject> queryLess(
        crossinline invoke: ResultRealm<List<T>>.() -> Unit,
        query: String,
        args: Array<Any>,
    ) {
        kotlinx.coroutines.coroutineScope {
            kotlin.runCatching {
                realmSync.cloud?.query(clazz = T::class, query = query, args = args)?.find()
            }.getOrNull()?.let {
                loggerError("realmSync", it.size.toString())
                invoke.invoke(ResultRealm(it, REALM_SUCCESS))
            } ?: kotlin.run {
                loggerError("realmSync", "REALM_FAILED")
                invoke.invoke(ResultRealm(emptyList(), REALM_FAILED))
            }
        }
    }

}