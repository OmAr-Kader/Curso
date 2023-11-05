package com.curso.free.ui.lecturer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.data.util.REALM_SUCCESS
import com.curso.free.data.util.toArticleDataClass
import com.curso.free.data.util.toCourseForData
import com.curso.free.di.Project
import com.curso.free.global.util.toStr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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


    fun getCoursesForLecturer(id: String) {
        this@HomeViewModel.launch {
            project.course.getLecturerCourses(id) {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(courses = value.toCourseForData(), isLoading = false)
                }
            }
        }
    }

    fun getArticlesForLecturer(id: String) {
        this@HomeViewModel.launch {
            project.article.getLecturerArticles(id) {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(articles = value.toArticleDataClass(), isLoading = false)
                }
            }
        }
    }

    fun getUpcomingLecturerTimeline() {
        this@HomeViewModel.launch {
            state.value.courses.sortedBy { it.lastEdit }.splitCourses().let {
                _state.value = state.value.copy(sessionForDisplay = it, isLoading = false)
            }
        }
    }

    private fun List<CourseForData>.splitCourses(): List<SessionForDisplay> {
        val courses: MutableList<SessionForDisplay> = mutableListOf()
        this@splitCourses.forEach { course ->
            course.timelines.forEachIndexed { i, (title, date, note, duration, video, mode, _) ->
                SessionForDisplay(
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
                    studentId = "",
                    studentName = "",
                    timelineIndex = i,
                    mode = 1,
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

    fun allCourses() {
        this@HomeViewModel.launch {
            project.course.getAllCourses {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(allCourses = value.toCourseForData(), isLoading = false)
                }
            }
        }
    }

    fun allArticles() {
        this@HomeViewModel.launch {
            project.article.getAllArticles {
                if (result == REALM_SUCCESS) {
                    _state.value = state.value.copy(allArticles = value.toArticleDataClass(), isLoading = false)
                }
            }
        }
    }

    fun updateCurrentTabIndex(it: Int) {
        _state.value = state.value.copy(currentTab = it, isLoading = true)
    }

    data class State(
        val courses: List<CourseForData> = emptyList(),
        val articles: List<ArticleForData> = emptyList(),
        val sessionForDisplay: List<SessionForDisplay> = emptyList(),
        val allCourses: List<CourseForData> = emptyList(),
        val allArticles: List<ArticleForData> = emptyList(),
        val currentTab: Int = 0,
        val isLoading: Boolean = true,
        val isFABExpend: Boolean = false,
    )

}
