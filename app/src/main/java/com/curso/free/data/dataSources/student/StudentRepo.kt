package com.curso.free.data.dataSources.student

import com.curso.free.data.model.ResultRealm
import com.curso.free.data.model.Student

interface StudentRepo {

    suspend fun getStudent(
        id: String,
        student: ResultRealm<Student?>.() -> Unit,
    )

    suspend fun getStudentEmail(
        email: String,
        student: ResultRealm<Student?>.() -> Unit,
    )

    suspend fun insertStudent(student: Student): ResultRealm<Student?>
}