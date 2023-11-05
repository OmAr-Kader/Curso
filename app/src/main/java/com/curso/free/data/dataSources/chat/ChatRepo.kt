package com.curso.free.data.dataSources.chat

import com.curso.free.data.model.Conversation
import com.curso.free.data.model.ResultRealm

interface ChatRepo {

    suspend fun getMainChatFlow(
        courseId: String,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Conversation>>?

    suspend fun getTimelineChatFlow(
        courseId: String,
        type: Int,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Conversation>>?

    suspend fun createChat(conversation: Conversation): ResultRealm<Conversation?>

    suspend fun editChat(
        conversation: Conversation,
        edit: Conversation,
    ): ResultRealm<Conversation?>
}