package com.titan.newsappnative.feature_news.domain.repository

import androidx.lifecycle.MutableLiveData
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.model.News
import retrofit2.Call

interface NewsRepository {
    suspend fun getHeadlines(): Call<News>
    suspend fun getSearchResults(search: String): Call<News>
    suspend fun getBookmarks(): List<Bookmark>
    suspend fun bookmarkArticle(bookmark: Bookmark)
    suspend fun deleteFromBookmarks(bookmark: Bookmark)
    fun isBookmarkSnackBarShown(): Boolean
    fun setBookmarkSnackBarShown()
}