package com.curso.free.data.dataSources.chat

import com.curso.free.data.model.Conversation
import com.curso.free.data.model.ResultRealm

class ChatData(
    private val repository: ChatRepo
) {
    suspend fun getMainChatFlow(
        courseId: String,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Conversation>>? = repository.getMainChatFlow(courseId)

    suspend fun getTimelineChatFlow(
        courseId: String,
        type: Int,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Conversation>>? = repository.getTimelineChatFlow(courseId, type)

    suspend fun createChat(
        conversation: Conversation
    ): ResultRealm<Conversation?> = repository.createChat(conversation)

    suspend fun editChat(
        conversation: Conversation,
        edit: Conversation,
    ): ResultRealm<Conversation?> = repository.editChat(conversation, edit)
}