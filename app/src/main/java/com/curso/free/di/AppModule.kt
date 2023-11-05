package com.curso.free.di

import com.curso.free.data.dataSources.article.ArticleData
import com.curso.free.data.dataSources.article.ArticleRepo
import com.curso.free.data.dataSources.article.ArticleRepoImp
import com.curso.free.data.dataSources.chat.ChatData
import com.curso.free.data.dataSources.chat.ChatRepoImp
import com.curso.free.data.dataSources.course.CourseData
import com.curso.free.data.dataSources.course.CourseRepo
import com.curso.free.data.dataSources.course.CourseRepoImp
import com.curso.free.data.dataSources.lecturer.LecturerData
import com.curso.free.data.dataSources.lecturer.LecturerRepo
import com.curso.free.data.dataSources.lecturer.LecturerRepoImp
import com.curso.free.data.dataSources.pref.PrefRepo
import com.curso.free.data.dataSources.pref.PrefRepoImp
import com.curso.free.data.dataSources.pref.PreferenceData
import com.curso.free.data.dataSources.student.StudentData
import com.curso.free.data.dataSources.student.StudentRepo
import com.curso.free.data.dataSources.student.StudentRepoImp
import com.curso.free.data.firebase.NotificationRepo
import com.curso.free.data.util.RealmSync
import com.curso.free.data.util.RealmSync.Companion.initialSubscriptionBlock
import com.curso.free.data.util.listOfOnlyLocalSchemaRealmClass
import com.curso.free.global.base.FCM_BASE_URI
import com.curso.free.global.base.FIREBASE_API_KEY
import com.curso.free.global.base.FIREBASE_APP_ID
import com.curso.free.global.base.FIREBASE_PROJECT_ID
import com.curso.free.global.base.FIREBASE_SENDER_ID
import com.curso.free.global.base.FIREBASE_STORAGE_BUCKET
import com.curso.free.global.base.REALM_APP_ID
import com.curso.free.global.base.SCHEMA_VERSION

@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
object AppModule {

    @javax.inject.Singleton
    @dagger.Provides
    fun provideFireApp(
        @dagger.hilt.android.qualifiers.ApplicationContext appContext: android.content.Context
    ): com.google.firebase.FirebaseApp {
        return com.google.firebase.FirebaseApp.initializeApp(
            appContext,
            com.google.firebase.FirebaseOptions.Builder()
                .setApplicationId(FIREBASE_APP_ID)
                .setApiKey(FIREBASE_API_KEY)
                .setGcmSenderId(FIREBASE_SENDER_ID)
                .setStorageBucket(FIREBASE_STORAGE_BUCKET)
                .setProjectId(FIREBASE_PROJECT_ID)
                .build()
        )
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideNotificationRepo(): NotificationRepo {
        return retrofit2.Retrofit.Builder()
            .baseUrl(FCM_BASE_URI)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build().create(NotificationRepo::class.java)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideRealmApp(): io.realm.kotlin.mongodb.App {
        return io.realm.kotlin.mongodb.App.create(REALM_APP_ID)
    }

    @javax.inject.Singleton
    @dagger.Provides
    @javax.inject.Named("Cloud")
    fun provideRealmCloud(
        realmApp: io.realm.kotlin.mongodb.App?
    ): io.realm.kotlin.Realm? {
        return io.realm.kotlin.Realm.open(
            (realmApp?.currentUser ?: return null).initialSubscriptionBlock
        )
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideRealmSync(
        app: io.realm.kotlin.mongodb.App,
        @javax.inject.Named("Cloud") cloud: io.realm.kotlin.Realm?,
        @dagger.hilt.android.qualifiers.ApplicationContext appContext: android.content.Context
    ): RealmSync {
        return RealmSync(app, cloud, appContext)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideRealm(): io.realm.kotlin.Realm {
        val config = io.realm.kotlin.RealmConfiguration.Builder(
            schema = listOfOnlyLocalSchemaRealmClass,
        ).schemaVersion(SCHEMA_VERSION).compactOnLaunch().build()
        return io.realm.kotlin.Realm.open(config)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun providePrefRepo(realm: io.realm.kotlin.Realm): PrefRepo {
        return PrefRepoImp(realm = realm)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideCourseRepo(realmSync: RealmSync): CourseRepo {
        return CourseRepoImp(realmSync = realmSync)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideArticleRepo(realmSync: RealmSync): ArticleRepo {
        return ArticleRepoImp(realmSync = realmSync)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideStudentRepo(realmSync: RealmSync): StudentRepo {
        return StudentRepoImp(realmSync = realmSync)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideLecturerRepo(realmSync: RealmSync): LecturerRepo {
        return LecturerRepoImp(realmSync = realmSync)
    }

    @javax.inject.Singleton
    @dagger.Provides
    fun provideChatRepo(realmSync: RealmSync): ChatRepoImp {
        return ChatRepoImp(realmSync = realmSync)
    }

    @dagger.Provides
    @javax.inject.Singleton
    fun provideNoteUseCases(
        prefRepo: PrefRepo,
        courseRepo: CourseRepo,
        articleRepo: ArticleRepo,
        studentRepo: StudentRepo,
        lecturerRepo: LecturerRepo,
        chatRepo: ChatRepoImp,
        realmSync: RealmSync,
        notificationRepo: NotificationRepo,
        fireApp: com.google.firebase.FirebaseApp,
    ): Project {
        return Project(
            preference = PreferenceData(prefRepo),
            course = CourseData(courseRepo),
            article = ArticleData(articleRepo),
            student = StudentData(studentRepo),
            lecturer = LecturerData(lecturerRepo),
            chat = ChatData(chatRepo),
            realmSync = realmSync,
            fireApp = fireApp,
            notificationRepo = notificationRepo
        )
    }


}