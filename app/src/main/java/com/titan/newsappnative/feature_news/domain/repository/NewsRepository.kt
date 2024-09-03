package com.titan.newsappnative.feature_news.domain.repository

import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.model.News
import retrofit2.Call

interface NewsRepository {
    suspend fun getHeadlines(): Call<News>
    suspend fun getSearchResults(search: String): Call<News>
    suspend fun bookmarkArticle(bookmark: Bookmark)
    suspend fun deleteFromBookmarks(bookmark: Bookmark)
    fun isBookmarkSnackBarShown(): Boolean
    fun setBookmarkSnackBarShown()
}