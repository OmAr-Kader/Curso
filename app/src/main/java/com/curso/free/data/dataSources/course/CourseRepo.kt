package com.curso.free.data.dataSources.course

import com.curso.free.data.model.Course
import com.curso.free.data.model.ResultRealm

interface CourseRepo {

    suspend fun getAllCourses(
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getStudentCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getCoursesById(
        id: String,
        course: ResultRealm<Course?>.() -> Unit,
    )

    suspend fun getLecturerCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getAvailableLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getUpcomingLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getAvailableStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun getUpcomingStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    )

    suspend fun insertCourse(course: Course): ResultRealm<Course?>

    suspend fun editCourse(course: Course, edit: Course): ResultRealm<Course?>

    suspend fun deleteCourse(course: Course): Int

}