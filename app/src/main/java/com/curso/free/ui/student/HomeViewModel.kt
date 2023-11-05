package com.curso.free.ui.student

import com.curso.free.data.model.Lecturer
import com.curso.free.data.util.toArticleDataClass
import com.curso.free.data.util.toCourseForData
import com.curso.free.global.util.toStr
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class HomeViewModel @javax.inject.Inject constructor(
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

    fun getCoursesForStudent(id: String) {
        _state.value = state.value.copy(
            isLoading = true,
        )
        this@HomeViewModel.launch {
            project.course.getStudentCourses(id) {
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(courses = value.toCourseForData(), isLoading = false)
                }
            }
        }
    }

    fun getAvailableStudentTimeline(studentId: String, studentName: String) {
        this@HomeViewModel.launch {
            project.course.getAvailableStudentTimeline(studentId, System.currentTimeMillis()) {
                value.sortedBy { it.lastEdit }.splitCourses(studentId = studentId, studentName = studentName).let {
                    _state.value = state.value.copy(sessionForDisplay = it)
                }
            }
        }
    }

    fun getUpcomingStudentTimeline(studentId: String, studentName: String) {
        this@HomeViewModel.launch {
            project.course.getUpcomingStudentTimeline(studentId, System.currentTimeMillis()) {
                value.sortedBy { it.lastEdit }.splitCourses(studentId = studentId, studentName = studentName).let {
                    _state.value = state.value.copy(sessionForDisplay = it)
                }
            }
        }
    }

    private fun List<com.curso.free.data.model.Course>.splitCourses(
        studentId: String,
        studentName: String
    ): List<com.curso.free.data.model.SessionForDisplay> {
        val courses: MutableList<com.curso.free.data.model.SessionForDisplay> = mutableListOf()
        this@splitCourses.forEach { course ->
            course.timelines.forEachIndexed { i, timeline ->
                com.curso.free.data.model.SessionForDisplay(
                    title = timeline.title,
                    date = timeline.date,
                    dateStr = timeline.date.toStr,
                    note = timeline.note,
                    video = timeline.video,
                    timelineMode = timeline.mode,
                    courseId = course._id.toHexString(),
                    courseName = course.title,
                    lecturerId = course.lecturerId,
                    lecturerName = course.lecturerName,
                    studentId = studentId,
                    studentName = studentName,
                    timelineIndex = i,
                    mode = 0,
                    duration = timeline.duration,
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

    fun updateCurrentTabIndex(it: Int) {
        _state.value = state.value.copy(currentTab = it)
    }

    fun allCoursesFollowers(studentId: String) {
        _state.value = state.value.copy(
            isLoading = true,
        )
        this@HomeViewModel.launch {
            project.lecturer.getLecturerFollowed(studentId) {
                if (value.isEmpty()) {
                    _state.value = state.value.copy(isLoading = false, followedCourses = emptyList())
                    return@getLecturerFollowed
                }
                value.doAllCoursesFollowers()
            }
        }
    }

    private fun List<Lecturer>.doAllCoursesFollowers() {
        this@HomeViewModel.launch {
            project.course.getAllCoursesFollowed(this@doAllCoursesFollowers.map { it._id.toHexString() }.toTypedArray()) {
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(followedCourses = value.toCourseForData(), isLoading = false)
                }
            }
        }
    }

    fun allArticles() {
        _state.value = state.value.copy(
            isLoading = true,
        )
        this@HomeViewModel.launch {
            project.article.getAllArticles {
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(allArticles = value.toArticleDataClass(), isLoading = false)
                }
            }
        }
    }

    fun allArticlesFollowers(studentId: String) {
        _state.value = state.value.copy(
            isLoading = true,
        )
        this@HomeViewModel.launch {
            project.lecturer.getLecturerFollowed(studentId) {
                if (value.isEmpty()) {
                    _state.value = state.value.copy(isLoading = false, followedArticles = emptyList())
                    return@getLecturerFollowed
                }
                value.doAllArticlesFollowers()
            }
        }
    }

    private fun List<Lecturer>.doAllArticlesFollowers() {
        this@HomeViewModel.launch {
            project.article.getAllArticlesFollowed(this@doAllArticlesFollowers.map { it._id.toHexString() }.toTypedArray()) {
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(followedArticles = value.toArticleDataClass(), isLoading = false)
                }
            }
        }
    }

    fun allCourses() {
        _state.value = state.value.copy(
            isLoading = true,
        )
        this@HomeViewModel.launch {
            project.course.getAllCourses {
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(allCourses = value.toCourseForData(), isLoading = false)
                }
            }
        }
    }

    data class State(
        val courses: List<com.curso.free.data.model.CourseForData> = emptyList(),
        val sessionForDisplay: List<com.curso.free.data.model.SessionForDisplay> = emptyList(),
        val followedCourses: List<com.curso.free.data.model.CourseForData> = emptyList(),
        val allCourses: List<com.curso.free.data.model.CourseForData> = emptyList(),
        val followedArticles: List<com.curso.free.data.model.ArticleForData> = emptyList(),
        val allArticles: List<com.curso.free.data.model.ArticleForData> = emptyList(),
        val isOrderSectionVisible: Boolean = false,
        val currentTab: Int = 0,
        val isLoading: Boolean = false,
    )

}