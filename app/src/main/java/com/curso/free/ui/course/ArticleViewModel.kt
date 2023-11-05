package com.curso.free.ui.course

import com.curso.free.data.firebase.NotificationData
import com.curso.free.data.firebase.PushNotification
import com.curso.free.data.firebase.subscribeToTopic
import com.curso.free.data.model.Article
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.util.logger
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.util.isNetworkAvailable
import com.google.gson.Gson
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class ArticleViewModel @javax.inject.Inject constructor(
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

    @get:androidx.compose.runtime.Composable
    @get:androidx.compose.runtime.ReadOnlyComposable
    inline val articleRate: String
        get() = state.value.article.let {
            if (it.rate == 0.0) "5" else it.rate.toString()
        }

    fun getArticle(article: ArticleForData?, articleId: String, studentId: String, userName: String) {
        if (article != null) {
            _state.value = state.value.copy(
                userId = studentId,
                userName = userName,
                article = article,
                isLoading = false,
            )
            addNewReader(article, studentId)
            return
        }
        _state.value = state.value.copy(
            userId = studentId,
            userName = userName,
            isLoading = true,
        )
        this@ArticleViewModel.launch {
            project.article.getArticlesById(articleId) {
                if (value != null) {
                    val articleForData = ArticleForData(value)
                    _state.value = state.value.copy(
                        article = articleForData,
                        isLoading = false,
                    )
                    addNewReader(articleForData, studentId)
                }
            }
        }
    }

    private fun addNewReader(article: ArticleForData, studentId: String) {
        if (!article.readerIds.contains(studentId)) {
            article.readerIds.toMutableList().apply {
                this@apply.add(studentId)
            }.also { newList ->
                this@ArticleViewModel.launch {
                    project.article.editArticle(Article(article), Article(Article(article), newList))
                }
            }
        }
    }

    fun getMainArticleConversation(articleId: String) {
        this@ArticleViewModel.launch {
            project.chat.getMainChatFlow(articleId)?.collect { changes ->
                _state.value = state.value.copy(conversation = changes.obj)
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

    fun android.content.Context.send(
        mode: Int,
        id: String,
        name: String,
        failed: (String) -> Unit
    ) {
        this@ArticleViewModel.launch {
            isNetworkAvailable().also { available ->
                if (!available) {
                    failed.invoke("Failed: Internet is disconnected")
                    return@launch
                }
                state.value.conversation.let {
                    if (it == null) {
                        doCreateConversation(mode, id, name, failed)
                    } else {
                        it.doEditConversation(mode, id, name, failed)
                    }
                }
            }
        }
    }

    private suspend fun doCreateConversation(
        mode: Int,
        id: String,
        name: String,
        failed: (String) -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        val article = state.value.article
        project.chat.createChat(
            com.curso.free.data.model.Conversation(
                courseId = article.id,
                courseName = article.title,
                type = -1,
                messages = mutableListOf<com.curso.free.data.model.Message>().messageCreator(mode, id, name, state.value.chatText).toRealmList(),
                _id = org.mongodb.kbson.ObjectId.invoke()
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS && it.value != null) {
                pushNotification(
                    topicId = article.lecturerId,
                    msgTitle = "Article (${article.title}) Chat",
                    message = "$name new message",
                    argOne = article.id,
                    argTwo = article.title,
                    mode = mode,
                )
                _state.value = state.value.copy(conversation = it.value, chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private suspend fun com.curso.free.data.model.Conversation.doEditConversation(
        mode: Int,
        id: String,
        name: String,
        failed: (String) -> Unit
    ) = kotlinx.coroutines.coroutineScope {
        val article = state.value.article
        project.chat.editChat(
            this@doEditConversation,
            com.curso.free.data.model.Conversation(
                courseId = article.id,
                courseName = article.title,
                type = -1,
                messages = messages.messageCreator(mode, id, name, state.value.chatText).toRealmList(),
                _id = org.mongodb.kbson.ObjectId.invoke(_id.toHexString())
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS && it.value != null) {
                pushNotification(
                    topicId = article.lecturerId,
                    msgTitle = "Article (${article.title}) Chat",
                    message = "$name new message",
                    argOne = article.id,
                    argTwo = article.title,
                    mode = mode,
                )
                _state.value = state.value.copy(conversation = it.value, chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private fun MutableList<com.curso.free.data.model.Message>.messageCreator(
        mode: Int,
        id: String,
        name: String,
        message: String, timestamp: Long = 0
    ): MutableList<com.curso.free.data.model.Message> {
        return if (mode == COURSE_MODE_STUDENT) {
            com.curso.free.data.model.Message(
                message = message,
                data = System.currentTimeMillis(),
                senderId = id,
                senderName = name,
                timestamp = timestamp,
                fromStudent = true
            )
        } else {
            com.curso.free.data.model.Message(
                message = message,
                data = System.currentTimeMillis(),
                senderId = id,
                senderName = name,
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

    private fun pushNotification(
        topicId: String,
        msgTitle: String,
        message: String,
        argOne: String,
        argTwo: String,
        mode: Int,
    ) {
        this@ArticleViewModel.launch {
            subscribeToTopic(topicId) {

            }
            project.notificationRepo.postNotification(
                PushNotification(
                    to = "/topics/$topicId",
                    topic = "/topics/$topicId",
                    data = NotificationData(
                        title = msgTitle,
                        message = message,
                        routeKey = ARTICLE_SCREEN_ROUTE,
                        argOne = argOne,
                        argTwo = argTwo,
                        argThree = if (mode == COURSE_MODE_STUDENT) COURSE_MODE_LECTURER else COURSE_MODE_STUDENT,
                    )
                )
            ).let { response ->
                if (response.isSuccessful) {
                    logger("pushNotification", "Response: ${Gson().toJson(response)}")
                } else {
                    logger("pushNotification", "Response: ${response.errorBody()}")
                }
            }
        }
    }


    data class State(
        val article: ArticleForData = ArticleForData(),
        val conversation: com.curso.free.data.model.Conversation? = null,
        val userId: String = "",
        val userName: String = "",
        val chatText: String = "",
        val textFieldFocus: Boolean = false,
        val hideKeyboard: Boolean = false,
        val isLoading: Boolean = false,
    )

}

