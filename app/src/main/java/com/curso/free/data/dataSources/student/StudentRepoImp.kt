package com.curso.free.data.dataSources.student

import com.curso.free.data.model.ResultRealm
import com.curso.free.data.model.Student
import com.curso.free.data.util.BaseRepoImp
import com.curso.free.data.util.RealmSync

class StudentRepoImp(realmSync: RealmSync) : BaseRepoImp(realmSync), StudentRepo {

    override suspend fun getStudent(
        id: String,
        student: ResultRealm<Student?>.() -> Unit,
    ): Unit = querySingle(student, "getStudent$id", "partition == $0 AND _id == $1", arrayOf("public", org.mongodb.kbson.ObjectId.invoke(id)))

    override suspend fun getStudentEmail(
        email: String,
        student: ResultRealm<Student?>.() -> Unit,
    ): Unit = querySingle(student, "getStudentEmail$email", "partition == $0 AND email == $1", arrayOf("public", email))

    override suspend fun insertStudent(student: Student): ResultRealm<Student?> = insert(student)

}