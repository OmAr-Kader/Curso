package com.curso.free.data.dataSources.lecturer

import com.curso.free.data.model.Lecturer
import com.curso.free.data.model.ResultRealm
import com.curso.free.data.util.BaseRepoImp
import com.curso.free.data.util.RealmSync
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

class LecturerRepoImp(realmSync: RealmSync) : BaseRepoImp(realmSync), LecturerRepo {

    override suspend fun getLecturerFollowed(
        studentId: String,
        lecturer: ResultRealm<List<Lecturer>>.() -> Unit,
    ): Unit = query(lecturer, "getLecturerFollowed$studentId", "partition == $0 AND follower.studentId == $1", arrayOf("public", studentId))

    override suspend fun getLecturer(
        id: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    ): Unit = querySingle(
        lecturer,
        queryName = "getLecturer$id",
        query = "partition == $0 AND _id == $1",
        args = arrayOf("public", org.mongodb.kbson.ObjectId.invoke(id))
    )

    override suspend fun getLecturerFlow(
        id: String,
    ): Flow<SingleQueryChange<Lecturer>>? = querySingleFlow(
        queryName = "getLecturer$id",
        query = "partition == $0 AND _id == $1",
        args = arrayOf("public", org.mongodb.kbson.ObjectId.invoke(id))
    )

    override suspend fun getLecturerEmail(
        email: String,
        lecturer: ResultRealm<Lecturer?>.() -> Unit,
    ): Unit = querySingle(lecturer, "getLecturerEmail$email", "partition == $0 AND email == $1", arrayOf("public", email))

    override suspend fun insertLecturer(lecturer: Lecturer): ResultRealm<Lecturer?> = insert(lecturer)

    override suspend fun editLecturer(
        lecturer: Lecturer,
        edit: Lecturer,
    ): ResultRealm<Lecturer?> = edit(lecturer._id) { this@edit.copy(edit) }

}