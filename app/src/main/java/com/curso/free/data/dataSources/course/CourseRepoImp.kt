package com.curso.free.data.dataSources.course

import com.curso.free.data.model.Course
import com.curso.free.data.model.ResultRealm
import com.curso.free.data.util.BaseRepoImp
import com.curso.free.data.util.RealmSync
import org.mongodb.kbson.ObjectId

class CourseRepoImp(realmSync: RealmSync) : BaseRepoImp(realmSync), CourseRepo {

    override suspend fun getCoursesById(
        id: String,
        course: ResultRealm<Course?>.() -> Unit,
    ): Unit = querySingle(course, "getCoursesById$id", "partition == $0 AND _id == $1", arrayOf("public", ObjectId.invoke(id)))

    override suspend fun getAllCourses(
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = query(course, "getAllCourses", "partition == $0 AND isDraft == $1", arrayOf("public", -1))

    override suspend fun getStudentCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = query(course, "getStudentCourses$id", "partition == $0 AND students.studentId == $1 AND isDraft == $2", arrayOf("public", id, -1))

    override suspend fun getLecturerCourses(
        id: String,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = query(course, "getLecturerCourses$id", "partition == $0 AND lecturerId == $1", arrayOf("public", id))

    override suspend fun getAvailableLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = queryLess(
        course,
        "partition == $0 AND lecturerId == $1 AND timelines.date < $2",
        arrayOf("public", id, currentTime)
    )

    override suspend fun getUpcomingLecturerTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = queryLess(
        course,
        "partition == $0 AND lecturerId == $1 AND timelines.date > $2",
        arrayOf("public", id, currentTime)
    )

    override suspend fun getAvailableStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = queryLess(
        course,
        "partition == $0 AND students.studentId == $1 AND isDraft == $2 AND timelines.date < $3",
        arrayOf("public", id, -1, currentTime)
    )


    override suspend fun getUpcomingStudentTimeline(
        id: String,
        currentTime: Long,
        course: ResultRealm<List<Course>>.() -> Unit,
    ): Unit = queryLess(
        course,
        "partition == $0 AND students.studentId == $1 AND isDraft == $2 AND timelines.date > $3",
        arrayOf("public", id, -1, currentTime)
    )

    override suspend fun insertCourse(course: Course): ResultRealm<Course?> = insert(course)

    override suspend fun editCourse(
        course: Course,
        edit: Course,
    ): ResultRealm<Course?> = edit(course._id) { this@edit.copy(edit) }

    override suspend fun deleteCourse(course: Course): Int = delete<Course>("_id == $0", course._id)

}