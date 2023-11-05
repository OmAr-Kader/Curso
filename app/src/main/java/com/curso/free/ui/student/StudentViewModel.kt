package com.curso.free.ui.student

import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.StudentForData
import com.curso.free.data.util.toCourseForData
import com.curso.free.global.util.ifNotEmpty
import com.curso.free.global.util.toStr
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class StudentViewModel @javax.inject.Inject constructor(
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
    inline val certificatesRate: Double
        get() = state.value.certificates.ifNotEmpty {
            sumOf { it.rate } / state.value.certificates.size.toDouble()
        } ?: 0.0

    fun inti(studentId: String) {
        this@StudentViewModel.launch {
            doFetchStudent(studentId)
        }
    }

    private suspend fun doFetchStudent(studentId: String) {
        project.student.getStudent(studentId) {
            _state.value = state.value.copy(student = StudentForData(value ?: return@getStudent))
            studentCourses(studentId = studentId, studentName = value.studentName)
        }
    }

    private fun studentCourses(studentId: String, studentName: String) {
        this@StudentViewModel.launch {
            doFetchStudentCourses(studentId)
            doFetchStudentTimeline(studentId, studentName)
        }
    }

    private suspend fun doFetchStudentCourses(studentId: String) {
        _state.value = state.value.copy(courses = emptyList())
        project.course.getStudentCourses(studentId) {
            if (result == com.curso.free.data.util.REALM_SUCCESS) {
                _state.value = state.value.copy(courses = value.toCourseForData())
            }
        }
    }

    private suspend fun doFetchStudentTimeline(studentId: String, studentName: String) {
        project.course.getUpcomingStudentTimeline(studentId, System.currentTimeMillis()) {
            state.value.courses.sortedBy { it.lastEdit }.splitCourses(studentId = studentId, studentName = studentName).let {
                _state.value = state.value.copy(sessionForDisplay = it)
            }
        }
    }

    private fun List<CourseForData>.splitCourses(studentId: String, studentName: String): List<com.curso.free.data.model.SessionForDisplay> {
        val courses: MutableList<com.curso.free.data.model.SessionForDisplay> = mutableListOf()
        this@splitCourses.forEach { course ->
            course.timelines.forEachIndexed { i, (title, date, note, duration, video, mode, _) ->
                com.curso.free.data.model.SessionForDisplay(
                    title = title,
                    date = date,
                    dateStr = date.toStr,
                    note = note,
                    video = video,
                    timelineMode = mode,
                    courseId = course.id,
                    courseName = course.title,
                    lecturerId = course.lecturerId,
                    lecturerName = course.lecturerName,
                    studentId = studentId,
                    studentName = studentName,
                    timelineIndex = i,
                    mode = 0,
                    duration = duration,
                    imageUri = course.imageUri,
                    isDraft = course.isDraft,
                ).let {
                    courses.add(
                        it
                    )
                }
            }
        }
        return courses
    }

    data class State(
        val student: StudentForData = StudentForData(),
        val sessionForDisplay: List<com.curso.free.data.model.SessionForDisplay> = emptyList(),
        val courses: List<CourseForData> = emptyList(),
        val certificates: List<com.curso.free.data.model.Certificate> = emptyList(),
    )
}