package com.curso.free.di

data class Project(
    val preference: com.curso.free.data.dataSources.pref.PreferenceData,
    val course: com.curso.free.data.dataSources.course.CourseData,
    val article: com.curso.free.data.dataSources.article.ArticleData,
    val student: com.curso.free.data.dataSources.student.StudentData,
    val lecturer: com.curso.free.data.dataSources.lecturer.LecturerData,
    val chat: com.curso.free.data.dataSources.chat.ChatData,
    val realmSync: com.curso.free.data.util.RealmSync,
    val fireApp: com.google.firebase.FirebaseApp,
    val notificationRepo: com.curso.free.data.firebase.NotificationRepo,
)
