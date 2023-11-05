package com.curso.free.data.dataSources.article

import com.curso.free.data.model.Article
import com.curso.free.data.model.ResultRealm

class ArticleData(
    private val repository: ArticleRepo
) {

    suspend fun getAllArticles(
        article: ResultRealm<List<Article>>.() -> Unit,
    ): Unit = repository.getAllArticles(article)

    suspend fun getAllArticlesFollowed(
        lecturerIds: Array<String>,
        article: ResultRealm<List<Article>>.() -> Unit,
    ): Unit = repository.getAllArticles {
        value.filter {
            lecturerIds.contains(it.lecturerId)
        }.let {
            ResultRealm(it, result)
        }.let(article)
    }

    suspend fun getArticlesById(
        id: String,
        article: ResultRealm<Article?>.() -> Unit,
    ): Unit = repository.getArticlesById(id = id, article = article)

    suspend fun getLecturerArticles(
        id: String,
        article: ResultRealm<List<Article>>.() -> Unit,
    ): Unit = repository.getLecturerArticles(id = id, article = article)

    suspend fun insertArticle(article: Article): ResultRealm<Article?> = repository.insertArticle(article)

    suspend fun editArticle(article: Article, edit: Article): ResultRealm<Article?> = repository.editArticle(article = article, edit = edit)

    suspend fun deleteArticle(article: Article): Int = repository.deleteArticle(article)
}