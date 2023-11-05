package com.curso.free.data.dataSources.student

import com.curso.free.data.model.ResultRealm
import com.curso.free.data.model.Student
import com.curso.free.data.util.logger

class StudentData(
    private val studentRepo: StudentRepo,
) {
    suspend fun getStudent(
        id: String,
        student: ResultRealm<Student?>.() -> Unit,
    ): Unit = studentRepo.getStudent(id, student)

    suspend fun getStudentEmail(
        email: String,
        student: ResultRealm<Student?>.() -> Unit,
    ): Unit = studentRepo.getStudentEmail(email, student)

    suspend fun insertStudent(student: Student): ResultRealm<Student?> = kotlin.run {
        logger("WWW", "insertStudent")
        studentRepo.insertStudent(student)
    }
}