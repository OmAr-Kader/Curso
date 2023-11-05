package com.curso.free.ui.course

import com.curso.free.data.firebase.NotificationData
import com.curso.free.data.firebase.PushNotification
import com.curso.free.data.firebase.subscribeToTopic
import com.curso.free.data.model.Conversation
import com.curso.free.data.model.ConversationForData
import com.curso.free.data.model.Course
import com.curso.free.data.model.CourseForData
import com.curso.free.data.util.logger
import com.curso.free.data.util.toMessage
import com.curso.free.data.util.toStudentCourses
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.util.isNetworkAvailable
import com.google.gson.Gson
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class CourseViewModel @javax.inject.Inject constructor(
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

    @get:androidx.compose.runtime.Composable
    @get:androidx.compose.runtime.ReadOnlyComposable
    inline val courseRate: String
        get() = state.value.course.let {
            if (it.rate == 0.0) "5" else it.rate.toString()
        }

    inline val List<com.curso.free.data.model.StudentCoursesData>.alreadyEnrolled: (String) -> Boolean
        get() = {
            find { sc ->
                sc.studentId == it
            } != null
        }

    fun getCourseLecturer(course: CourseForData?, courseId: String, userId: String, userName: String) {
        if (course != null) {
            _state.value = state.value.copy(
                userId = userId,
                userName = userName,
                isLoading = false,
                course = course
            )
            return
        }
        _state.value = state.value.copy(
            userId = userId,
            userName = userName,
            isLoading = true,
        )
        this@CourseViewModel.launch {
            project.course.getCoursesById(courseId) {
                val s = state.value
                if (value != null) {
                    _state.value = s.copy(
                        course = CourseForData(value, System.currentTimeMillis()),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun getCourse(course: CourseForData?, courseId: String, studentId: String, userName: String) {
        if (course != null) {
            _state.value = state.value.copy(
                userId = studentId,
                userName = userName,
                course = course,
                alreadyEnrolled = course.students.alreadyEnrolled(studentId),
                isLoading = false,
            )
            return
        }
        _state.value = state.value.copy(
            userId = studentId,
            userName = userName,
            isLoading = true,
        )
        this@CourseViewModel.launch {
            project.course.getCoursesById(courseId) {
                if (value != null) {
                    val courseForData = CourseForData(value, System.currentTimeMillis())
                    _state.value = state.value.copy(
                        course = courseForData,
                        alreadyEnrolled = courseForData.students.alreadyEnrolled(studentId),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun getMainConversation(courseId: String) {
        this@CourseViewModel.launch {
            project.chat.getMainChatFlow(courseId)?.collect { changes ->
                _state.value = state.value.copy(conversation = changes.obj?.let { ConversationForData(it) })
            }
        }
    }

    val changeChatText: (String) -> Unit
        get() = {
            _state.value = state.value.copy(chatText = it)
        }

    fun changeFocus(newFocus: Boolean) {
        if (state.value.textFieldFocus == newFocus) {
            return
        }
        _state.value = state.value.copy(
            textFieldFocus = newFocus,
        )
    }


    fun enroll(studentId: String, studentName: String, invoke: () -> Unit, failed: () -> Unit) {
        this@CourseViewModel.launch {
            val c = state.value.course
            val list = c.students.toMutableList()
            list.add(
                com.curso.free.data.model.StudentCoursesData(
                    studentId = studentId,
                    studentName = studentName,
                    type = com.curso.free.data.util.COURSE_TYPE_ENROLLED
                )
            )
            project.course.editCourse(
                Course(state.value.course),
                Course(Course(c), c.id, list.toStudentCourses())
            ).let {
                if (it.value != null && it.result == com.curso.free.data.util.REALM_SUCCESS) {
                    subscribeToTopic(it.value._id.toHexString()) {

                    }
                    val courseForData = CourseForData(it.value, System.currentTimeMillis())
                    _state.value = state.value.copy(course = courseForData, alreadyEnrolled = courseForData.students.alreadyEnrolled(studentId))
                    invoke.invoke()
                } else {
                    failed.invoke()
                }
            }
        }
    }


    fun android.content.Context.send(
        mode: Int,
        id: String,
        name: String,
        failed: (String) -> Unit
    ) {
        this@CourseViewModel.launch {
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
        val course = state.value.course
        project.chat.createChat(
            Conversation(
                courseId = course.id,
                courseName = course.title,
                type = -1,
                messages = mutableListOf<com.curso.free.data.model.MessageForData>().messageCreator(mode, id, name, state.value.chatText).toMessage(),
                _id = org.mongodb.kbson.ObjectId.invoke()
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS && it.value != null) {
                pushNotification(
                    topicId = it.value.courseId,
                    msgTitle = "Course (${course.title}) Chat",
                    message = "$name new message",
                    argOne = it.value.courseId,
                    argTwo = it.value.courseName,
                    mode = mode,
                )
                _state.value = state.value.copy(conversation = ConversationForData(it.value), chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private suspend fun ConversationForData.doEditConversation(
        mode: Int,
        id: String,
        name: String,
        failed: (String) -> Unit
    ) = kotlinx.coroutines.coroutineScope {
        val course = state.value.course
        project.chat.editChat(
            Conversation(this@doEditConversation),
            Conversation(
                courseId = course.id,
                courseName = course.title,
                type = -1,
                messages = messages.messageCreator(mode, id, name, state.value.chatText).toRealmList().toMessage(),
                _id = org.mongodb.kbson.ObjectId.invoke(id)
            )
        ).let {
            if (it.result == com.curso.free.data.util.REALM_SUCCESS && it.value != null) {
                pushNotification(
                    topicId = it.value.courseId,
                    msgTitle = "Course (${course.title}) Chat",
                    message = "$name new message",
                    argOne = it.value.courseId,
                    argTwo = it.value.courseName,
                    mode = mode,
                )
                _state.value = state.value.copy(conversation = ConversationForData(it.value), chatText = "")
            } else {
                failed.invoke("Failed")
            }
        }
    }

    private fun List<com.curso.free.data.model.MessageForData>.messageCreator(
        mode: Int,
        id: String,
        name: String,
        message: String, timestamp: Long = 0
    ): MutableList<com.curso.free.data.model.MessageForData> {
        return if (mode == COURSE_MODE_STUDENT) {
            com.curso.free.data.model.MessageForData(
                message = message,
                data = System.currentTimeMillis(),
                senderId = id,
                senderName = name,
                timestamp = timestamp,
                fromStudent = true
            )
        } else {
            com.curso.free.data.model.MessageForData(
                message = message,
                data = System.currentTimeMillis(),
                senderId = id,
                senderName = name,
                timestamp = 0,
                fromStudent = false
            )
        }.let {
            toMutableList().apply {
                add(it)
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
        this@CourseViewModel.launch {
            subscribeToTopic(topicId) {

            }
            project.notificationRepo.postNotification(
                PushNotification(
                    to = "/topics/$topicId",
                    topic = "/topics/$topicId",
                    data = NotificationData(
                        title = msgTitle,
                        message = message,
                        routeKey = COURSE_SCREEN_ROUTE,
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
        val course: CourseForData = CourseForData(),
        val conversation: ConversationForData? = null,
        val userId: String = "",
        val userName: String = "",
        val chatText: String = "",
        val textFieldFocus: Boolean = false,
        val hideKeyboard: Boolean = false,
        val alreadyEnrolled: Boolean = false,
        val isLoading: Boolean = false,
    )

}

