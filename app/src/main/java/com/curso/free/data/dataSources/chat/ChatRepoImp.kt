package com.curso.free.data.dataSources.chat

import com.curso.free.data.model.Conversation
import com.curso.free.data.model.ResultRealm
import com.curso.free.data.util.BaseRepoImp
import com.curso.free.data.util.RealmSync
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

class ChatRepoImp(realmSync: RealmSync) : BaseRepoImp(realmSync), ChatRepo {

    override suspend fun getMainChatFlow(
        courseId: String,
    ): Flow<SingleQueryChange<Conversation>>? = querySingleFlow(
        queryName = "getMainChat$courseId",
        query = "partition == $0 AND courseId == $1 AND type == $2",
        args = arrayOf("public", courseId, -1)
    )

    override suspend fun getTimelineChatFlow(
        courseId: String,
        type: Int,
    ): Flow<SingleQueryChange<Conversation>>? = querySingleFlow(
        queryName = "getTimelineChat$courseId$type",
        query = "partition == $0 AND courseId == $1 AND type == $2",
        args = arrayOf("public", courseId, type),
    )

    override suspend fun createChat(conversation: Conversation): ResultRealm<Conversation?> = insert(conversation)

    override suspend fun editChat(
        conversation: Conversation,
        edit: Conversation,
    ): ResultRealm<Conversation?> = edit(conversation._id) { this@edit.copy(edit) }

}