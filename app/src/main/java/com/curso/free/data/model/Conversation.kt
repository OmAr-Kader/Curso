package com.curso.free.data.model

import com.curso.free.data.util.toMessage
import com.curso.free.data.util.toMessageData

class Conversation(
    @io.realm.kotlin.types.annotations.Index
    var courseId: String,
    var courseName: String,
    var type: Int, //  -1 => Main Course Conversation,
    var messages: io.realm.kotlin.types.RealmList<Message>,
    var partition: String = "public",
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id: org.mongodb.kbson.ObjectId,
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        courseId = "",
        courseName = "",
        type = -1,
        messages = io.realm.kotlin.ext.realmListOf(),
        _id = org.mongodb.kbson.ObjectId.invoke(),
    )


    constructor(update: ConversationForData) : this(
        courseId = update.courseId,
        courseName = update.courseName,
        messages = update.messages.toMessage(),
        type = update.type,
        _id = org.mongodb.kbson.ObjectId.invoke(update.id),
    )

    constructor(update: Conversation, hexString: String) : this(
        courseId = update.courseId,
        courseName = update.courseName,
        messages = update.messages,
        type = update.type,
        _id = org.mongodb.kbson.ObjectId.invoke(hexString),
    )

    fun copy(update: Conversation): Conversation {
        courseId = update.courseId
        courseName = update.courseName
        messages = update.messages
        type = update.type
        return this
    }

}

class Message(
    var message: String,
    var data: Long,
    var senderId: String,
    var senderName: String,
    var timestamp: Long, // For Timeline Video,
    var fromStudent: Boolean,
) : io.realm.kotlin.types.EmbeddedRealmObject {
    constructor() : this(
        message = "",
        data = -1L,
        senderId = "",
        senderName = "",
        timestamp = -1L,
        fromStudent = true,
    )

    constructor(update: MessageForData) : this(
        message = update.message,
        data = update.data,
        senderId = update.senderId,
        senderName = update.senderName,
        timestamp = update.timestamp,
        fromStudent = update.fromStudent,
    )
}

class MessageForData(
    val message: String,
    val data: Long,
    val senderId: String,
    val senderName: String,
    val timestamp: Long,
    val fromStudent: Boolean,
) {
    constructor(update: Message) : this(
        message = update.message,
        data = update.data,
        senderId = update.senderId,
        senderName = update.senderName,
        timestamp = update.timestamp,
        fromStudent = update.fromStudent,
    )
}

data class ConversationForData(
    val courseId: String,
    val courseName: String,
    val type: Int, //  -1 => Main Course Conversation,
    val messages: List<MessageForData>,
    val id: String,
) {
    constructor(update: Conversation) : this(
        courseId = update.courseId,
        courseName = update.courseName,
        type = update.type,
        messages = update.messages.toMessageData(),
        id = update._id.toHexString(),
    )

}