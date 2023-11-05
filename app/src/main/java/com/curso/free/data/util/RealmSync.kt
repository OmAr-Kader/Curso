package com.curso.free.data.util

import com.curso.free.global.base.SCHEMA_VERSION
import kotlin.time.Duration.Companion.seconds

data class RealmSync(
    val realmApp: io.realm.kotlin.mongodb.App,
    var realmCloud: io.realm.kotlin.Realm?,
    val appContext: android.content.Context
) {

    val cloud: io.realm.kotlin.Realm?
        get() {
            return if (realmCloud != null) {
                realmCloud
            } else {
                val user = realmApp.currentUser
                if (user == null) {
                    null
                } else {
                    realmCloud = io.realm.kotlin.Realm.open(user.initialSubscriptionBlock)
                    realmCloud
                }
            }
        }

    companion object {
        val io.realm.kotlin.mongodb.User.initialSubscriptionBlock: io.realm.kotlin.mongodb.sync.SyncConfiguration
            get() {
                return io.realm.kotlin.mongodb.sync.SyncConfiguration.Builder(
                    user = this@initialSubscriptionBlock,
                    schema = listOfSchemaClass,
                ).waitForInitialRemoteData(30.seconds).schemaVersion(SCHEMA_VERSION)
                    .initialSubscriptions { sub ->
                        listOfSchemaRealmClass.forEach {
                            add(query = sub.query(it))
                        }
                    }.errorHandler { _, e ->
                        loggerError("SyncConfiguration", e.stackTraceToString())
                    }.build()
            }

        /*val io.realm.kotlin.mongodb.User.initialSubscriptionBlockPartition: (String) -> io.realm.kotlin.mongodb.sync.SyncConfiguration
            get() = {
                io.realm.kotlin.mongodb.sync.SyncConfiguration.Builder(
                    user = this@initialSubscriptionBlockPartition,
                    schema = listOfSchemaClass,
                ).waitForInitialRemoteData(30.seconds).maxNumberOfActiveVersions(2).schemaVersion(SCHEMA_VERSION).errorHandler { _, e ->
                    loggerError("SyncConfiguration", e.stackTraceToString())
                }.build()
            }*/
    }

}

/*{
 "%%partition": {
   "$in": [
     "public",
     "%%user.id"
   ]
 }
}
{
 "$or": [
     { "%%partition": "%%user.id" },
     { "%%partition": "public" }
 ]
{ "%%partition": [ "%%user.id", "public"] }
}*/
