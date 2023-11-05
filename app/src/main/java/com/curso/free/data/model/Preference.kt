package com.curso.free.data.model

class Preference(
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = org.mongodb.kbson.ObjectId.invoke(),
    @io.realm.kotlin.types.annotations.Index
    var ketString: String,
    var value: String,
) : io.realm.kotlin.types.RealmObject {
    constructor() : this(org.mongodb.kbson.ObjectId.invoke(), "", "")
    constructor(ketString: String, value: String) : this(org.mongodb.kbson.ObjectId.invoke(), ketString, value)

}
