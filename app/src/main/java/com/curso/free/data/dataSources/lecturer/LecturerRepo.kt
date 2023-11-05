package com.curso.free.data.dataSources.lecturer

import com.curso.free.data.model.Lecturer
import com.curso.free.data.model.ResultRealm

interface LecturerRepo {

    suspend fun getLecturerFollowed(
        studentId: String,
        lecturer: ResultRealm<List<Lecturer>>.() -> Unit,
    )

    suspend fun getLecturer(
        id: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    )

    suspend fun getLecturerFlow(
        id: String,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Lecturer>>?

    suspend fun getLecturerEmail(
        email: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    )

    suspend fun insertLecturer(lecturer: Lecturer): ResultRealm<Lecturer?>

    suspend fun editLecturer(lecturer: Lecturer, edit: Lecturer): ResultRealm<Lecturer?>

}