package com.curso.free.data.dataSources.course

import com.curso.free.data.model.Course
import com.curso.free.data.model.ResultRealm

class CourseData(
    private val repository: CourseRepo
) {
    suspend fun getAllCourses(
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getAllCourses(course)

    suspend fun getAllCoursesFollowed(
        lecturerIds: Array<String>,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getAllCourses {
        value.filter {
            lecturerIds.contains(it.lecturerId)
        }.let {
            ResultRealm(it, result)
        }.let(course)
    }

    suspend fun getCoursesById(
        id: String,
        course: ResultRealm<Course?>.() -> Unit,
    ): Unit = repository.getCoursesById(id, course)

    suspend fun getLecturerCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getLecturerCourses(id, course)

    suspend fun getStudentCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getStudentCourses(id, course)

    suspend fun getAvailableLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getAvailableLecturerTimeline(id, currentTime, course)

    suspend fun getUpcomingLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getUpcomingLecturerTimeline(id, currentTime, course)

    suspend fun getAvailableStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getAvailableStudentTimeline(id, currentTime, course)

    suspend fun getUpcomingStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = repository.getUpcomingStudentTimeline(id, currentTime, course)

    suspend fun insertCourse(course: Course): ResultRealm<Course?> = repository.insertCourse(course)

    suspend fun editCourse(course: Course, edit: Course): ResultRealm<Course?> = repository.editCourse(course, edit)

    suspend fun deleteCourse(course: Course): Int = repository.deleteCourse( course)
}