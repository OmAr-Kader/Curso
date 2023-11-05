package com.curso.free.data.util

const val REALM_SUCCESS: Int = 1
const val REALM_FAILED: Int = -1

const val COURSE_TYPE_FOLLOWED: Int = 0
const val COURSE_TYPE_ENROLLED: Int = 1

internal val listOfOnlyLocalSchemaRealmClass: Set<kotlin.reflect.KClass<out io.realm.kotlin.types.TypedRealmObject>>
    get() = setOf(com.curso.free.data.model.Preference::class)

internal val listOfSchemaClass: Set<kotlin.reflect.KClass<out io.realm.kotlin.types.TypedRealmObject>>
    get() = listOfSchemaRealmClass + listOfSchemaEmbeddedRealmClass

internal val listOfSchemaRealmClass: Set<kotlin.reflect.KClass<out io.realm.kotlin.types.RealmObject>>
    get() = setOf(
        com.curso.free.data.model.Conversation::class,
        com.curso.free.data.model.Lecturer::class,
        com.curso.free.data.model.Course::class,
        com.curso.free.data.model.Certificate::class,
        com.curso.free.data.model.Student::class,
        com.curso.free.data.model.Article::class,
    )

internal val listOfSchemaEmbeddedRealmClass: Set<kotlin.reflect.KClass<out io.realm.kotlin.types.TypedRealmObject>>
    get() = setOf(
        com.curso.free.data.model.Message::class,
        com.curso.free.data.model.Timeline::class,
        com.curso.free.data.model.AboutCourse::class,
        com.curso.free.data.model.StudentCourses::class,
        com.curso.free.data.model.StudentLecturer::class,
        com.curso.free.data.model.ArticleText::class,
    )

@androidx.compose.runtime.Composable
@androidx.compose.runtime.ReadOnlyComposable
fun com.curso.free.data.model.Course.nextTimeLine(current: Long): String? {
    return timelines.firstOrNull {
        it.date > current
    }?.title
}

@androidx.compose.runtime.Composable
@androidx.compose.runtime.ReadOnlyComposable
fun com.curso.free.data.model.CourseForData.nextTimeLine(current: Long): String? {
    return timelines.firstOrNull {
        it.date > current
    }?.title
}

fun com.curso.free.data.model.Course.nextSession(current: Long): String? {
    return timelines.firstOrNull {
        it.date > current
    }?.title
}

fun logger(tag: String, it: String) {
    android.util.Log.w("==> $tag", it)
}

fun loggerError(tag: String, it: String) {
    android.util.Log.e("==> $tag", it)
}