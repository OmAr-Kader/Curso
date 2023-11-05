package com.curso.free.data.model

import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Student(
    var studentName: String,
    var email: String,
    var mobile: String,
    var imageUri: String,
    var specialty: String,
    var university: String,
    var partition: String = "public",
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke(),
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        studentName = "",
        email = "",
        mobile = "",
        imageUri = "",
        specialty = "",
        university = "",
        _id = org.mongodb.kbson.ObjectId.invoke()
    )

    constructor(update: StudentForData) : this(
        studentName = update.studentName,
        email = update.email,
        mobile = update.mobile,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        _id = org.mongodb.kbson.ObjectId.invoke(update.id),
    )

    fun copy(update: Student): Student {
        studentName = update.studentName
        email = update.email
        mobile = update.mobile
        specialty = update.specialty
        university = update.university
        imageUri = update.imageUri
        return this
    }
}


class StudentForData(
    val studentName: String,
    val email: String,
    val mobile: String,
    val imageUri: String,
    val specialty: String,
    val university: String,
    val id: String,
) {
    constructor() : this(
        studentName = "",
        email = "",
        mobile = "",
        imageUri = "",
        specialty = "",
        university = "",
        id = "",
    )

    constructor(update: Student) : this(
        studentName = update.studentName,
        email = update.email,
        mobile = update.mobile,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        id = update._id.toHexString(),
    )
}