package com.curso.free.ui.lecturer

import com.curso.free.data.firebase.NotificationData
import com.curso.free.data.firebase.PushNotification
import com.curso.free.data.firebase.deleteFile
import com.curso.free.data.firebase.subscribeToTopic
import com.curso.free.data.firebase.upload
import com.curso.free.data.model.Article
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.ArticleTextDate
import com.curso.free.data.util.logger
import com.curso.free.data.util.toArticleText
import com.curso.free.data.util.toArticleTextDate
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.util.getMimeType
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@dagger.hilt.android.lifecycle.HiltViewModel
class CreateArticleViewModel @javax.inject.Inject constructor(
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

    fun getArticle(id: String) {
        this@CreateArticleViewModel.launch {
            project.article.getArticlesById(id) {
                this.value ?: return@getArticlesById
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(
                        article = ArticleForData(value),
                        courseTitle = value.title,
                        articleText = value.text.toArticleTextDate().ifEmpty {
                            mutableListOf<ArticleTextDate>().apply {
                                add(ArticleTextDate(text = "", font = 22))
                            }
                        },
                        imageUri = value.imageUri,
                    )
                }
            }
        }
    }

    fun deleteArticle(invoke: () -> Unit) {
        this@CreateArticleViewModel.launch {
            project.article.deleteArticle(Article(state.value.article ?: return@launch)).let {
                if (it == com.curso.free.data.util.REALM_SUCCESS) {
                    invoke.invoke()
                }
            }
        }
    }

    fun android.content.Context.save(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        invoke: (Article?) -> Unit
    ) {
        val s = _state.value
        if (
            (isDraft && s.courseTitle.isEmpty()) ||
            (!isDraft && (
                    s.courseTitle.isEmpty() ||
                            s.articleText.map { it.text }.isEmpty() ||
                            s.imageUri.isEmpty())
                    )
        ) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        _state.value = state.value.copy(isProcessing = !isDraft, isDraftProcessing = isDraft)
        doSave(isDraft, lecturerId, lecturerName, s, invoke)
    }


    private suspend fun android.content.Context.articleForSave(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Article) -> Unit,
        failed: () -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        uploadImage(lecturerId, { imageUri ->
            Article(
                title = s.courseTitle,
                lecturerName = lecturerName,
                lecturerId = lecturerId,
                imageUri = imageUri,
                text = s.articleText.toArticleText(),
                readerIds = realmListOf(),
                isDraft = if (isDraft) 1 else -1,
                lastEdit = System.currentTimeMillis(),
            ).let {
                invoke.invoke(it)
            }
        }, failed)
    }

    private fun android.content.Context.doSave(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Article?) -> Unit
    ) {
        this@CreateArticleViewModel.launch {
            articleForSave(isDraft, lecturerId, lecturerName, s, { course ->
                this@CreateArticleViewModel.launch {
                    project.article.insertArticle(
                        course
                    ).let {
                        if (it.value != null) {
                            pushNotification(
                                topicId = it.value.lecturerId,
                                msgTitle = "New Article",
                                message = "$lecturerName add A new Article",
                                argOne = it.value._id.toHexString(),
                                argTwo = it.value.title
                            )
                        }
                        _state.value = state.value.copy(isProcessing = false, isDraftProcessing = false)
                        invoke.invoke(it.value)
                    }
                }
            }, {
                invoke.invoke(null)
            })
        }
    }

    fun android.content.Context.edit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        invoke: (Article?) -> Unit
    ) {
        val s = _state.value
        if (
            (isDraft && s.courseTitle.isEmpty()) ||
            (!isDraft && (
                    s.courseTitle.isEmpty() ||
                            s.articleText.map { it.text }.isEmpty() ||
                            s.imageUri.isEmpty())
                    )
        ) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        _state.value = state.value.copy(isProcessing = !isDraft, isDraftProcessing = isDraft)
        doEdit(isDraft, lecturerId, lecturerName, s, invoke)
    }

    private suspend fun android.content.Context.courseForEdit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Article) -> Unit,
        failed: () -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        val c = s.article ?: kotlin.run {
            failed.invoke()
            return@coroutineScope
        }
        uploadImage(lecturerId, { imageUri ->
            Article(
                title = s.courseTitle,
                lecturerName = lecturerName,
                lecturerId = lecturerId,
                imageUri = imageUri,
                text = s.articleText.toArticleText(),
                readerIds = realmListOf(),
                lastEdit = System.currentTimeMillis(),
                isDraft = if (isDraft) 1 else -1,
                _id = ObjectId.invoke(c.id)
            ).let {
                invoke.invoke(it)
            }
        }, failed)
    }

    private fun android.content.Context.doEdit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Article?) -> Unit
    ) {
        this@CreateArticleViewModel.launch {
            courseForEdit(isDraft, lecturerId = lecturerId, lecturerName = lecturerName, s = s, { article ->
                doEditArticle(s = s, article = article, invoke = invoke)
            }, {
                invoke.invoke(null)
            })
        }
    }

    private fun doEditArticle(s: State, article: Article, invoke: (Article?) -> Unit) {
        this@CreateArticleViewModel.launch l@{
            project.article.editArticle(
                Article(s.article ?: return@l),
                article
            ).let {
                if (it.value != null) {
                    pushNotification(
                        topicId = article.lecturerId,
                        msgTitle = "Check what's new in the article",
                        message = "${it.value.lecturerName} edit ${it.value.title}",
                        argOne = it.value._id.toHexString(),
                        argTwo = it.value.title
                    )
                }
                _state.value = state.value.copy(isProcessing = false, isDraftProcessing = false)
                invoke.invoke(it.value)
            }
        }
    }

    fun setCourseTitle(it: String) {
        _state.value = state.value.copy(courseTitle = it, isErrorPressed = false)
    }

    fun makeFontDialogVisible() {
        _state.value = state.value.copy(isFontDialogVisible = true)
    }

    fun addAbout(type: Int) {
        val list = state.value.articleText.toMutableList()
        list.add(ArticleTextDate(text = "", font = if (type == 0) 14 else 22))
        _state.value = state.value.copy(articleText = list, isFontDialogVisible = false, isErrorPressed = false)
    }

    fun removeAboutIndex(index: Int) {
        val list = state.value.articleText.toMutableList()
        list.removeAt(index)
        _state.value = state.value.copy(articleText = list, dummy = state.value.dummy + 1)
    }

    fun changeAbout(it: String, index: Int) {
        val list = state.value.articleText.toMutableList()
        list[index] = list[index].copy(text = it)
        _state.value = state.value.copy(articleText = list, dummy = state.value.dummy + 1)
    }

    fun setImageUri(it: String) {
        _state.value = state.value.copy(imageUri = it, isErrorPressed = false)
    }

    fun changeUploadDialogGone(it: Boolean) {
        _state.value = state.value.copy(isConfirmDialogVisible = it)
    }


    private fun android.content.Context.uploadImage(lecturerId: String, invoke: (String) -> Unit, failed: () -> Unit) {
        val s = state.value
        val courseUri = s.article?.imageUri
        if (s.imageUri != courseUri && s.imageUri.isNotEmpty()) {
            val uri = android.net.Uri.parse(s.imageUri)
            this@CreateArticleViewModel.launch {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    kotlinx.coroutines.coroutineScope {
                        if ((courseUri ?: return@coroutineScope).contains("https")) {
                            project.fireApp.deleteFile(courseUri)
                        } else return@coroutineScope
                    }
                    project.fireApp.upload(uri, lecturerId + "/" + "IMG_" + System.currentTimeMillis() + getMimeType(uri), {
                        invoke.invoke(it)
                        _state.value = state.value.copy(imageUri = it, isErrorPressed = false)
                    }, {
                        failed.invoke()
                    })
                }
            }
        } else {
            invoke.invoke(s.imageUri)
        }
    }

    private fun pushNotification(
        topicId: String,
        msgTitle: String,
        message: String,
        argOne: String,
        argTwo: String,
    ) {
        this@CreateArticleViewModel.launch {
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
                        argThree = COURSE_MODE_STUDENT,
                    )
                )
            ).let { response ->
                if (response.isSuccessful) {
                    logger("pushNotification", "Response: ${com.google.gson.Gson().toJson(response)}")
                } else {
                    logger("pushNotification", "Response: ${response.errorBody()}")
                }
            }
        }
    }

    data class State(
        val article: ArticleForData? = null,
        val articleText: List<ArticleTextDate> = mutableListOf<ArticleTextDate>().apply { add(ArticleTextDate(text = "", font = 22)) },
        val courseTitle: String = "",
        val imageUri: String = "",
        val isErrorPressed: Boolean = false,
        val isProcessing: Boolean = false,
        val isDraftProcessing: Boolean = false,
        val dialogMode: Int = 0,
        val isConfirmDialogVisible: Boolean = false,
        val isFontDialogVisible: Boolean = false,
        val dummy: Int = 0,
    )

}