package com.curso.free.data.model

import com.curso.free.data.util.nextSession
import com.curso.free.data.util.toAboutCourse
import com.curso.free.data.util.toAboutCourseData
import com.curso.free.data.util.toStudentCourses
import com.curso.free.data.util.toStudentCoursesData
import com.curso.free.data.util.toTimeline
import com.curso.free.data.util.toTimelineData
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.util.toStr

class Course(
    var title: String,
    var lecturerName: String,
    @io.realm.kotlin.types.annotations.Index
    var lecturerId: String,
    @io.realm.kotlin.types.annotations.Index
    var price: String,
    var imageUri: String,
    var about: io.realm.kotlin.types.RealmList<AboutCourse>,
    var briefVideo: String,
    var timelines: io.realm.kotlin.types.RealmList<Timeline>,
    var students: io.realm.kotlin.types.RealmList<StudentCourses> = io.realm.kotlin.ext.realmListOf(),
    var rate: Double = 5.0,
    var raters: Int = 0,
    var lastEdit: Long,
    var isDraft: Int = 0,
    var partition: String = "public",
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = org.mongodb.kbson.ObjectId.invoke(),
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        title = "",
        lecturerName = "",
        lecturerId = "",
        price = "",
        imageUri = "",
        about = io.realm.kotlin.ext.realmListOf<AboutCourse>(),
        briefVideo = "",
        timelines = io.realm.kotlin.ext.realmListOf<Timeline>(),
        students = io.realm.kotlin.ext.realmListOf<StudentCourses>(),
        rate = 5.0,
        raters = 0,
        lastEdit = 0L,
        isDraft = 0,
        _id = org.mongodb.kbson.ObjectId.invoke(),
    )

    constructor(update: CourseForData) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        price = update.price,
        imageUri = update.imageUri,
        about = update.about.toAboutCourse(),
        briefVideo = update.briefVideo,
        timelines = update.timelines.toTimeline(),
        students = update.students.toStudentCourses(),
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        isDraft = update.isDraft,
        _id = org.mongodb.kbson.ObjectId.invoke(update.id),
    )

    constructor(update: Course, hexString: String, student: io.realm.kotlin.types.RealmList<StudentCourses>) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        price = update.price,
        imageUri = update.imageUri,
        about = update.about,
        briefVideo = update.briefVideo,
        timelines = update.timelines,
        students = student,
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        isDraft = update.isDraft,
        _id = org.mongodb.kbson.ObjectId.invoke(hexString),
    )

    fun copy(update: Course): Course {
        title = update.title
        lecturerName = update.lecturerName
        lecturerId = update.lecturerId
        price = update.price
        imageUri = update.imageUri
        about = update.about
        briefVideo = update.briefVideo
        timelines = update.timelines
        students = update.students
        rate = update.rate
        raters = update.raters
        lastEdit = update.lastEdit
        isDraft = update.isDraft
        return this
    }
}

class Timeline(
    var title: String,
    var date: Long,
    var note: String,
    var duration: String = "",
    var video: String = "",
    var mode: Int = 0, // EXAM = 1
    @io.realm.kotlin.types.annotations.Index
    var degree: Int = 0,
) : io.realm.kotlin.types.EmbeddedRealmObject {

    constructor() : this(
        title = "",
        date = -1L,
        note = "",
        duration = "",
        degree = 0,
    )

    constructor(it: TimelineData) : this(
        title = it.title,
        date = it.date,
        note = it.note,
        duration = it.duration,
        video = it.video,
        mode = it.mode,
        degree = it.degree,
    )

}

class AboutCourse(
    var font: Int = 14,
    var text: String = ""
) : io.realm.kotlin.types.EmbeddedRealmObject {
    constructor() : this(
        text = "",
        font = 0,
    )
}

class StudentCourses(
    @io.realm.kotlin.types.annotations.Index
    var studentId: String,
    @io.realm.kotlin.types.annotations.Index
    var studentName: String,
    @io.realm.kotlin.types.annotations.Index
    var type: Int,
) : io.realm.kotlin.types.EmbeddedRealmObject {

    constructor() : this(
        studentId = "",
        studentName = "",
        type = com.curso.free.data.util.COURSE_TYPE_FOLLOWED,
    )

    constructor(update: StudentCourses) : this(
        studentId = update.studentId,
        studentName = update.studentName,
        type = update.type,
    )

    constructor(it: StudentCoursesData) : this(it.studentId, it.studentName, it.type)

}


class Certificate(
    var title: String,
    @io.realm.kotlin.types.annotations.Index
    var date: Long,
    var rate: Double,
    @io.realm.kotlin.types.annotations.Index
    var courseId: String,
    var partition: String = "public",
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id: org.mongodb.kbson.ObjectId,
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        title = "",
        rate = 0.0,
        date = -1L,
        courseId = "",
        _id = org.mongodb.kbson.ObjectId.invoke(),
    )

    fun copy(update: Certificate): Certificate {
        title = update.title
        rate = update.rate
        date = update.date
        courseId = update.courseId
        return this
    }

}


data class AboutCourseData(
    val font: Int,
    val text: String,
)

data class SessionForDisplay(
    val title: String,
    val date: Long,
    val dateStr: String,
    val note: String,
    val video: String,
    val timelineMode: Int = 0, // EXAM = 1
    val courseId: String,
    val courseName: String,
    val lecturerId: String,
    val lecturerName: String,
    val studentId: String,
    val studentName: String,
    val timelineIndex: Int,
    val mode: Int, // STUDENT 0 , LECTURER 1,
    val duration: String = "",
    val imageUri: String = "",
    val isDraft: Int = 0,
) {
    constructor(course: CourseForData, timeline: TimelineData, mode: Int, userId: String, userName: String, i: Int) : this(
        title = timeline.title,
        date = timeline.date,
        dateStr = timeline.date.toStr,
        note = timeline.note,
        video = timeline.video,
        timelineMode = timeline.mode,
        courseId = course.id,
        courseName = course.title,
        lecturerId = course.lecturerId,
        lecturerName = course.lecturerName,
        studentId = if (mode == COURSE_MODE_STUDENT) userId else "",
        studentName = if (mode == COURSE_MODE_STUDENT) userName else "",
        timelineIndex = i,
        mode = if (mode == COURSE_MODE_STUDENT) 0 else 1,
    )

}

data class TimelineData(
    val title: String,
    val date: Long,
    val note: String,
    val duration: String,
    val video: String,
    val mode: Int,
    val degree: Int,
) {
    inline val isExam: Boolean
        get() = mode == 1

    constructor(it: Timeline) : this(
        title = it.title,
        date = it.date,
        note = it.note,
        duration = it.duration,
        video = it.video,
        mode = it.mode,
        degree = it.degree,
    )
}

data class CourseForData(
    val title: String,
    val lecturerName: String,
    val lecturerId: String,
    val price: String,
    val imageUri: String,
    val about: List<AboutCourseData>,
    val briefVideo: String,
    val timelines: List<TimelineData>,
    val nextTimeLine: String,
    val students: List<StudentCoursesData>,
    val studentsSize: String,
    val rate: Double = 5.0,
    val raters: Int = 0,
    val lastEdit: Long,
    val isDraft: Int = 0,
    val id: String,
) {

    constructor() : this(
        title = "",
        lecturerName = "",
        lecturerId = "",
        price = "",
        imageUri = "",
        about = listOf<AboutCourseData>(),
        briefVideo = "",
        timelines = listOf<TimelineData>(),
        nextTimeLine = "",
        students = listOf<StudentCoursesData>(),
        studentsSize = "0",
        rate = 5.0,
        raters = 0,
        lastEdit = 0L,
        isDraft = 0,
        id = "",
    )


    constructor(update: Course, currentTime: Long) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        price = update.price,
        imageUri = update.imageUri,
        about = update.about.toAboutCourseData(),
        briefVideo = update.briefVideo,
        timelines = update.timelines.toTimelineData(),
        nextTimeLine = update.nextSession(currentTime)?.let { "Next: $it" } ?: "",
        students = update.students.toStudentCoursesData(),
        studentsSize = update.students.size.toString(),
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        isDraft = update.isDraft,
        id = update._id.toHexString(),
    )
}

class StudentCoursesData(
    val studentId: String,
    val studentName: String,
    val type: Int,
) {
    constructor(it: StudentCourses) : this(it.studentId, it.studentName, it.type)
}