package com.curso.free.data.dataSources.lecturer

import com.curso.free.data.model.Lecturer
import com.curso.free.data.model.ResultRealm

class LecturerData(
    private val lecturerRepo: LecturerRepo,
) {

    suspend fun getLecturerFollowed(
        studentId: String,
        lecturer: ResultRealm<List<Lecturer>>.() -> Unit,
    ): Unit = lecturerRepo.getLecturerFollowed(studentId, lecturer)

    suspend fun getLecturer(
        id: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    ): Unit = lecturerRepo.getLecturer(id, lecturer)

    suspend fun getLecturerFlow(
        id: String,
    ): kotlinx.coroutines.flow.Flow<io.realm.kotlin.notifications.SingleQueryChange<Lecturer>>? = lecturerRepo.getLecturerFlow(id)


    suspend fun getLecturerEmail(
        email: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    ): Unit = lecturerRepo.getLecturerEmail(email, lecturer)

    suspend fun insertLecturer(lecturer: Lecturer): ResultRealm<Lecturer?> = lecturerRepo.insertLecturer(lecturer)

    suspend fun editLecturer(
        lecturer: Lecturer,
        edit: Lecturer
    ): ResultRealm<Lecturer?> = lecturerRepo.editLecturer(lecturer, edit)

}