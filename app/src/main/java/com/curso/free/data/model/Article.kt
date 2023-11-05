package com.curso.free.data.model

import com.curso.free.data.util.toArticleText
import com.curso.free.data.util.toArticleTextDate
import io.realm.kotlin.ext.toRealmList

class Article(
    var title: String,
    var lecturerName: String,
    @io.realm.kotlin.types.annotations.Index
    var lecturerId: String,
    @io.realm.kotlin.types.annotations.Index
    var imageUri: String,
    var text: io.realm.kotlin.types.RealmList<ArticleText>,
    var readerIds: io.realm.kotlin.types.RealmList<String>,
    var rate: Double = 5.0,
    var raters: Int = 0,
    var lastEdit: Long,
    var isDraft: Int = 0,
    var partition: String = "public",
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = org.mongodb.kbson.ObjectId.invoke(),
) : io.realm.kotlin.types.RealmObject {

    constructor() : this(
        title = "",
        lecturerName = "",
        lecturerId = "",
        imageUri = "",
        text = io.realm.kotlin.ext.realmListOf<ArticleText>(),
        readerIds = io.realm.kotlin.ext.realmListOf<String>(),
        rate = 5.0,
        raters = 0,
        lastEdit = 0L,
        isDraft = 0,
        _id = org.mongodb.kbson.ObjectId.invoke(),
    )

    constructor(update: Article, readerIds: List<String>) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        imageUri = update.imageUri,
        text = update.text,
        readerIds = readerIds.toRealmList(),
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        isDraft = update.isDraft,
        _id = org.mongodb.kbson.ObjectId.invoke(update._id.toHexString()),
    )

    constructor(update: ArticleForData) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        imageUri = update.imageUri,
        text = update.text.toArticleText(),
        readerIds = update.readerIds.toRealmList(),
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        partition = "public",
        isDraft = update.isDraft,
        _id = org.mongodb.kbson.ObjectId.invoke(update.id),
    )

    fun copy(update: Article): Article {
        title = update.title
        lecturerName = update.lecturerName
        lecturerId = update.lecturerId
        imageUri = update.imageUri
        text = update.text
        readerIds = update.readerIds
        rate = update.rate
        raters = update.raters
        lastEdit = update.lastEdit
        isDraft = update.isDraft
        return this
    }
}

class ArticleText(
    var font: Int = 14,
    var text: String = ""
) : io.realm.kotlin.types.EmbeddedRealmObject {
    constructor() : this(
        text = "",
        font = 0,
    )
}

data class ArticleTextDate(
    val font: Int = 14,
    val text: String = ""
)

data class ArticleForData(
    val title: String,
    val lecturerName: String,
    val lecturerId: String,
    val imageUri: String,
    val text: List<ArticleTextDate>,
    val readerIds: List<String>,
    val readers: String,
    val rate: Double = 5.0,
    val raters: Int = 0,
    val lastEdit: Long,
    val isDraft: Int = 0,
    val id: String,
) {

    constructor() : this(
        title = "",
        lecturerName = "",
        lecturerId = "",
        imageUri = "",
        text = listOf<ArticleTextDate>(),
        readerIds = listOf<String>(),
        readers = "0",
        rate = 5.0,
        raters = 0,
        lastEdit = 0L,
        isDraft = 0,
        id = "",
    )

    constructor(update: Article) : this(
        title = update.title,
        lecturerName = update.lecturerName,
        lecturerId = update.lecturerId,
        imageUri = update.imageUri,
        text = update.text.toArticleTextDate(),
        readerIds = update.readerIds,
        readers = update.readerIds.size.toString(),
        rate = update.rate,
        raters = update.raters,
        lastEdit = update.lastEdit,
        isDraft = update.isDraft,
        id = update._id.toHexString()
    )
}
