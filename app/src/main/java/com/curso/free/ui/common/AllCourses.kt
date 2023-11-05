package com.curso.free.ui.common

import androidx.compose.runtime.Composable
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.CourseForData
import com.curso.free.global.ui.ListBody

@Composable
fun HomeOwnCoursesView(courses: List<CourseForData>, nav: (CourseForData) -> Unit,) {
    ListBody(list = courses, bodyClick = nav) { course ->
        MainItem(title = course.title, imageUri = course.imageUri) {
            OwnCourseItem(nextTimeLine = course.nextTimeLine, students = course.studentsSize)
        }
    }
}

@Composable
fun HomeAllCoursesView(
    courses: List<CourseForData>,
    additionalItem: (@Composable () -> Unit)? = null,
    nav: (CourseForData) -> Unit,
) {
    ListBody(list = courses, bodyClick = nav, additionalItem) { (title, lecturerName, _, price, imageUri, _, _, _, _, _, _, _, _, _, _, _) ->
        MainItem(title = title, imageUri = imageUri) {
            AllCourseItem(lecturerName = lecturerName, price = price)
        }
    }
}


@Composable
fun HomeAllArticlesView(
    articles: List<ArticleForData>,
    additionalItem: (@Composable () -> Unit)? = null,
    nav: (ArticleForData) -> Unit,
) {
    ListBody(list = articles, bodyClick = nav, additionalItem) { (title, lecturerName, _, imageUri, _, _, readers, _, _, _, _, _) ->
        MainItem(title = title, imageUri = imageUri) {
            AllArticleIem(lecturerName = lecturerName, readers = readers)
        }
    }
}


@Composable
fun LecturerCoursesView(
    courses: List<CourseForData>,
    nav: (CourseForData) -> Unit,
) {
    ListBody(list = courses, bodyClick = { course ->
        nav.invoke(course)
    }) { course ->
        MainItem(title = course.title, imageUri = course.imageUri) {
            LecturerCourseItem(nextTimeLine = course.nextTimeLine, students = course.studentsSize, price = course.price)
        }
    }
}