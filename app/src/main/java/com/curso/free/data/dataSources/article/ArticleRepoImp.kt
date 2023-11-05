package com.curso.free.data.dataSources.article

import com.curso.free.data.model.Article
import com.curso.free.data.model.ResultRealm
import com.curso.free.data.util.BaseRepoImp
import com.curso.free.data.util.RealmSync
import org.mongodb.kbson.ObjectId

class ArticleRepoImp(realmSync: RealmSync) : BaseRepoImp(realmSync = realmSync), ArticleRepo {

    override suspend fun getAllArticles(
        article: ResultRealm<List<Article>>.() -> Unit,
    ): Unit = query(article, "getAllArticles", "partition == $0 AND isDraft == $1", arrayOf("public", -1))

    override suspend fun getArticlesById(
        id: String,
        article: ResultRealm<Article?>.() -> Unit,
    ): Unit = querySingle(article, "getArticlesById$id", "partition == $0 AND _id == $1", arrayOf("public", ObjectId.invoke(id)))

    override suspend fun getLecturerArticles(
        id: String,
        article: ResultRealm<List<Article>>.() -> Unit,
    ): Unit = query(article, "getLecturerArticles$id", "partition == $0 AND lecturerId == $1", arrayOf("public", id))


    override suspend fun insertArticle(article: Article): ResultRealm<Article?> = insert(article)

    override suspend fun editArticle(article: Article, edit: Article): ResultRealm<Article?> = edit(article._id) { this@edit.copy(edit) }

    override suspend fun deleteArticle(article: Article): Int = delete<Article>("_id == $0", article._id)
}