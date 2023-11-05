package com.curso.free.ui.lecturer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.curso.free.data.firebase.subscribeToTopic
import com.curso.free.data.firebase.unsubscribeToTopic
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.Lecturer
import com.curso.free.data.model.LecturerForData
import com.curso.free.data.model.Student
import com.curso.free.data.model.StudentLecturer
import com.curso.free.data.model.StudentLecturerData
import com.curso.free.data.util.REALM_SUCCESS
import com.curso.free.data.util.toArticleDataClass
import com.curso.free.data.util.toCourseForData
import com.curso.free.data.util.toStudentLecturer
import com.curso.free.di.Project
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class LectureViewModel @javax.inject.Inject constructor(
    private val project: Project
) : ViewModel(), kotlinx.coroutines.CoroutineScope {

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    private val _state = mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    @get:Composable
    @get:ReadOnlyComposable
    inline val lecturerRate: String
        get() = state.value.lecturer.let {
            if (it.rate == 0.0) "5" else it.rate.toString()
        }

    @Composable
    @ReadOnlyComposable
    fun lecturerFollowers(mode: Int): String {
        return state.value.lecturer.let {
            if (mode == 1) if (it.follower.isEmpty()) "Be First" else it.follower.size.toString() else it.follower.size.toString()
        }
    }

    inline val List<StudentLecturer>.alreadyFollowed: (String) -> Boolean
        get() = {
            find { sc ->
                sc.studentId == it
            } != null
        }

    private fun setStudentId(studentId: String) {
        _state.value = state.value.copy(studentId = studentId)
    }

    fun fetchLecturer(lecturerId: String, studentId: String) {
        setStudentId(studentId)
        this@LectureViewModel.launch {
            project.lecturer.getLecturerFlow(lecturerId)?.collect {
                val value = it.obj
                if (value != null) {
                    _state.value = state.value.copy(
                        lecturer = LecturerForData(value),
                        alreadyFollowed = value.follower.alreadyFollowed(studentId),
                    )
                    getCoursesForLecturer(lecturerId)
                }
            }
        }
    }

    private fun getCoursesForLecturer(lecturerId: String) {
        this@LectureViewModel.launch {
            project.course.getLecturerCourses(lecturerId) {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(courses = value.toCourseForData())
                }
                getArticlesForLecturer(lecturerId)
            }
        }
    }

    private fun getArticlesForLecturer(lecturerId: String) {
        this@LectureViewModel.launch {
            project.article.getLecturerArticles(lecturerId) {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(articles = value.toArticleDataClass())
                }
            }
        }
    }

    fun followUnfollow(
        studentId: String,
        alreadyFollowed: Boolean,
        invoke: () -> Unit,
        failed: () -> Unit,
    ) {
        if (alreadyFollowed) {
            unFollow(studentId, invoke, failed)
        } else {
            follow(studentId, invoke, failed)
        }
    }

    private fun follow(
        studentId: String,
        invoke: () -> Unit,
        failed: () -> Unit,
    ) {
        this@LectureViewModel.launch {
            project.student.getStudent(studentId) {
                doFollow(value, invoke, failed)
            }
        }
    }

    private fun doFollow(value: Student?, invoke: () -> Unit, failed: () -> Unit) {
        if (value == null) {
            failed.invoke()
        } else {
            this@LectureViewModel.launch {
                val l = state.value.lecturer
                val list = l.follower.toMutableList()
                list.add(StudentLecturerData(studentId = value._id.toHexString(), studentName = value.studentName))
                val lecturer = Lecturer(l)
                project.lecturer.editLecturer(
                    lecturer,
                    Lecturer(lecturer, lecturer._id.toHexString(), list.toRealmList().toStudentLecturer())
                ).let {
                    if (it.result == REALM_SUCCESS && it.value != null) {
                        subscribeToTopic(it.value._id.toHexString()) {

                        }
                        invoke.invoke()
                    } else {
                        failed.invoke()
                    }
                }
            }
        }
    }


    private fun unFollow(studentId: String, invoke: () -> Unit, failed: () -> Unit) {
        this@LectureViewModel.launch {
            val l = state.value.lecturer
            val list = l.follower.toMutableList().filter { it.studentId != studentId }.toMutableList()
            val lecturer = Lecturer(l)
            project.lecturer.editLecturer(
                lecturer,
                Lecturer(lecturer, lecturer._id.toHexString(), list.toStudentLecturer())
            ).let {
                if (it.result == REALM_SUCCESS && it.value != null) {
                    unsubscribeToTopic(it.value._id.toHexString())
                    invoke.invoke()
                } else {
                    failed.invoke()
                }
            }
        }
    }

    data class State(
        val lecturer: LecturerForData = LecturerForData(),
        val studentId: String = "",
        val courses: List<CourseForData> = emptyList(),
        val articles: List<ArticleForData> = emptyList(),
        val alreadyFollowed: Boolean = false,
    )
}