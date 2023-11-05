package com.curso.free.data.model

import com.curso.free.data.util.toStudentLecturer
import com.curso.free.data.util.toStudentLecturerData
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Lecturer(
    var lecturerName: String,
    @Index
    var email: String,
    var mobile: String,
    var rate: Double,
    var raters: Int,
    var brief: String,
    var imageUri: String,
    var specialty: String,
    var university: String,
    @Index
    var approved: Boolean,
    var follower: RealmList<StudentLecturer> = realmListOf(),
    var partition: String = "public",
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke(),
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        lecturerName = "",
        email = "",
        mobile = "",
        rate = 5.0,
        raters = 0,
        brief = "",
        imageUri = "",
        specialty = "",
        university = "",
        approved = false,
        follower = io.realm.kotlin.ext.realmListOf(),
        _id = org.mongodb.kbson.ObjectId.invoke(),
    )

    constructor(update: LecturerForData) : this(
        lecturerName = update.lecturerName,
        email = update.email,
        mobile = update.mobile,
        rate = update.rate,
        raters = update.raters,
        brief = update.brief,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        approved = update.approved,
        follower = update.follower.toStudentLecturer(),
        _id = org.mongodb.kbson.ObjectId.invoke(update.id),
    )

    constructor(update: Lecturer, hexString: String, followers: io.realm.kotlin.types.RealmList<StudentLecturer>) : this(
        lecturerName = update.lecturerName,
        email = update.email,
        mobile = update.mobile,
        rate = update.rate,
        raters = update.raters,
        brief = update.brief,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        approved = update.approved,
        follower = followers,
        _id = org.mongodb.kbson.ObjectId.invoke(hexString),
    )

    constructor(update: Lecturer, hexString: String) : this(
        lecturerName = update.lecturerName,
        email = update.email,
        mobile = update.mobile,
        rate = update.rate,
        raters = update.raters,
        brief = update.brief,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        approved = update.approved,
        follower = update.follower,
        _id = org.mongodb.kbson.ObjectId.invoke(hexString),
    )

    fun copy(update: Lecturer): Lecturer {
        lecturerName = update.lecturerName
        mobile = update.mobile
        email = update.email
        rate = update.rate
        raters = update.raters
        follower = update.follower
        brief = update.brief
        specialty = update.specialty
        university = update.university
        imageUri = update.imageUri
        approved = update.approved
        return this
    }

}


class StudentLecturer(
    @io.realm.kotlin.types.annotations.Index
    var studentId: String,
    @io.realm.kotlin.types.annotations.Index
    var studentName: String,
) : io.realm.kotlin.types.EmbeddedRealmObject {

    constructor() : this(
        studentId = "",
        studentName = "",
    )

    constructor(update: StudentCourses) : this(
        studentId = update.studentId,
        studentName = update.studentName,
    )

    constructor(update: StudentLecturerData) : this(
        studentId = update.studentId,
        studentName = update.studentName,
    )
}

class LecturerForData(
    val lecturerName: String,
    val email: String,
    val mobile: String,
    val rate: Double,
    val raters: Int,
    val brief: String,
    val imageUri: String,
    val specialty: String,
    val university: String,
    val approved: Boolean,
    val follower: List<StudentLecturerData>,
    val id: String,
) {
    constructor() : this(
        lecturerName = "",
        email = "",
        mobile = "",
        rate = 5.0,
        raters = 0,
        brief = "",
        imageUri = "",
        specialty = "",
        university = "",
        approved = false,
        follower = listOf(),
        id = "",
    )

    constructor(update: Lecturer) : this(
        lecturerName = update.lecturerName,
        email = update.email,
        mobile = update.mobile,
        rate = update.rate,
        raters = update.raters,
        brief = update.brief,
        imageUri = update.imageUri,
        specialty = update.specialty,
        university = update.university,
        approved = update.approved,
        follower = update.follower.toStudentLecturerData(),
        id = update._id.toHexString(),
    )
}

data class StudentLecturerData(
    val studentId: String,
    val studentName: String,
) {

    constructor() : this(
        studentId = "",
        studentName = "",
    )

    constructor(update: StudentLecturer) : this(
        studentId = update.studentId,
        studentName = update.studentName,
    )
}