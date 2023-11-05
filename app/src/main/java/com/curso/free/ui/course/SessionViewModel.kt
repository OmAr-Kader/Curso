package com.curso.free.ui.course

import com.curso.free.data.model.Conversation
import com.curso.free.data.model.ConversationForData
import com.curso.free.data.util.toMessage
import com.curso.free.global.util.isNetworkAvailable
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class SessionViewModel @javax.inject.Inject constructor(
    private val project: com.curso.free.di.Project
) : androidx.lifecycle.ViewModel(), kotlinx.coroutines.CoroutineScope {

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    private val _state = androidx.compose.runtime.mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    val changeChatText: (String) -> Unit
        get() = {
            _state.value = state.value.copy(chatText = it)
        }

    fun getTimelineConversation(id: String, timelineIndex: Int) {
        this@SessionViewModel.launch {
            project.chat.getTimelineChatFlow(id, timelineIndex)?.collect { changes ->
                _state.value = state.value.copy(conversation = changes.obj?.let { ConversationForData(it) })
            }
        }
    }

    fun android.content.Context.send(sessionForDisplay: com.curso.free.data.model.SessionForDisplay, failed: (String) -> Unit) {
        this@SessionViewModel.launch {
            isNetworkAvailable().also { available ->
                if (!available) {
                    failed.invoke("Failed: Internet is disconnected")
                    return@launch
                }
                state.value.conversation.let {
                    if (it == null) {
                        doCreateConversation(sessionForDisplay, failed)
                    } else {
                        it.doEditConversation(sessionForDisplay, failed)
                    }
                }
            }
        }
    }

    private suspend fun doCreateConversation(
        sessionForDisplay: com.curso.free.data.model.SessionForDisplay,
        failed: (String) -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        project.chat.createChat(
            Conversation(
                courseId = sessionForDisplay.courseId,
                courseName = sessionForDisplay.courseName,
                type = sessionForDisplay.timelineIndex,
                //partition = "private",
                messages = mutableListOf<com.curso.free.data.model.MessageForData>().messageCreator(sessionForDisplay, state.value.chatText)
                    .toRealmList().toMessage(),
                _id = org.mongodb.kbson.ObjectId.invoke()
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS) {
                _state.value = state.value.copy(conversation = it.value?.let { it1 -> ConversationForData(it1) }, chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private suspend fun ConversationForData.doEditConversation(
        sessionForDisplay: com.curso.free.data.model.SessionForDisplay,
        failed: (String) -> Unit
    ) = kotlinx.coroutines.coroutineScope {
        project.chat.editChat(
            Conversation(this@doEditConversation),
            Conversation(
                courseId = sessionForDisplay.courseId,
                courseName = sessionForDisplay.courseName,
                type = sessionForDisplay.timelineIndex,
                messages = messages.messageCreator(sessionForDisplay, state.value.chatText).toMessage(),
                _id = org.mongodb.kbson.ObjectId.invoke(id)
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS) {
                _state.value = state.value.copy(conversation = it.value?.let { it1 -> ConversationForData(it1) }, chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private fun List<com.curso.free.data.model.MessageForData>.messageCreator(
        sessionForDisplay: com.curso.free.data.model.SessionForDisplay,
        message: String, timestamp: Long = 0
    ): MutableList<com.curso.free.data.model.MessageForData> {
        return if (sessionForDisplay.mode == 0) {
            com.curso.free.data.model.MessageForData(
                message = message,
                data = System.currentTimeMillis(),
                senderId = sessionForDisplay.studentId,
                senderName = sessionForDisplay.studentName,
                timestamp = timestamp,
                fromStudent = true
            )
        } else {
            com.curso.free.data.model.MessageForData(
                message = message,
                data = System.currentTimeMillis(),
                senderId = sessionForDisplay.lecturerId,
                senderName = sessionForDisplay.lecturerName,
                timestamp = 0,
                fromStudent = false
            )
        }.let {
            toMutableList().apply {
                add(
                    it
                )
            }
        }
    }

    fun changeFocus(newFocus: Boolean) {
        if (state.value.textFieldFocus == newFocus) {
            return
        }
        _state.value = state.value.copy(
            textFieldFocus = newFocus,
        )
    }

    data class State(
        val conversation: ConversationForData? = null,
        val chatText: String = "",
        val textFieldFocus: Boolean = false,
    )

}