package com.curso.free.data.util

import com.curso.free.data.model.AboutCourse
import com.curso.free.data.model.AboutCourseData
import com.curso.free.data.model.Article
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.ArticleText
import com.curso.free.data.model.ArticleTextDate
import com.curso.free.data.model.Course
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.Message
import com.curso.free.data.model.MessageForData
import com.curso.free.data.model.StudentCourses
import com.curso.free.data.model.StudentCoursesData
import com.curso.free.data.model.StudentLecturer
import com.curso.free.data.model.StudentLecturerData
import com.curso.free.data.model.Timeline
import com.curso.free.data.model.TimelineData
import io.realm.kotlin.ext.toRealmList

fun List<AboutCourseData>.toAboutCourse(): io.realm.kotlin.types.RealmList<AboutCourse> {
    return map {
        AboutCourse(it.font, it.text)
    }.toRealmList()
}

fun io.realm.kotlin.types.RealmList<AboutCourse>.toAboutCourseData(): List<AboutCourseData> {
    return map {
        AboutCourseData(it.font, it.text)
    }
}

fun List<ArticleTextDate>.toArticleText(): io.realm.kotlin.types.RealmList<ArticleText> {
    return map {
        ArticleText(it.font, it.text)
    }.toRealmList()
}

fun io.realm.kotlin.types.RealmList<ArticleText>.toArticleTextDate(): List<ArticleTextDate> {
    return map {
        ArticleTextDate(it.font, it.text)
    }
}

fun List<TimelineData>.toTimeline(): io.realm.kotlin.types.RealmList<Timeline> {
    return map {
        Timeline(it)
    }.toRealmList()
}

fun io.realm.kotlin.types.RealmList<Timeline>.toTimelineData(): List<TimelineData> {
    return map {
        TimelineData(it)
    }
}

fun List<Article>.toArticleDataClass(): MutableList<ArticleForData> {
    return map {
        ArticleForData(it)
    }.toRealmList()
}

fun io.realm.kotlin.types.RealmList<StudentCourses>.toStudentCoursesData(): List<StudentCoursesData> {
    return map {
        StudentCoursesData(it)
    }
}

fun List<StudentCoursesData>.toStudentCourses(): io.realm.kotlin.types.RealmList<StudentCourses> {
    return map { update ->
        StudentCourses(update)
    }.toRealmList()
}

fun List<Course>.toCourseForData(currentTime: Long = System.currentTimeMillis()): List<CourseForData> {
    return map {
        CourseForData(it, currentTime)
    }
}

fun io.realm.kotlin.types.RealmList<Message>.toMessageData(): List<MessageForData> {
    return map {
        MessageForData(it)
    }
}

fun List<MessageForData>.toMessage(): io.realm.kotlin.types.RealmList<Message> {
    return map { update ->
        Message(update)
    }.toRealmList()
}

fun io.realm.kotlin.types.RealmList<StudentLecturer>.toStudentLecturerData(): List<StudentLecturerData> {
    return map {
        StudentLecturerData(it)
    }
}

fun List<StudentLecturerData>.toStudentLecturer(): io.realm.kotlin.types.RealmList<StudentLecturer> {
    return map { update ->
        StudentLecturer(update)
    }.toRealmList()
}
