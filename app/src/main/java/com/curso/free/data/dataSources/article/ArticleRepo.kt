package com.curso.free.data.dataSources.article

import com.curso.free.data.model.Article
import com.curso.free.data.model.ResultRealm

interface ArticleRepo {

    suspend fun getAllArticles(
        article: ResultRealm<List<Article>>.() -> Unit,
    )

    suspend fun getArticlesById(
        id: String,
        article: ResultRealm<Article?>.() -> Unit,
    )

    suspend fun getLecturerArticles(
        id: String,
        article: ResultRealm<List<Article>>.() -> Unit,
    )

    suspend fun insertArticle(article: Article): ResultRealm<Article?>

    suspend fun editArticle(article: Article, edit: Article): ResultRealm<Article?>

    suspend fun deleteArticle(article: Article): Int
}