package com.titan.newsappnative.feature_news.data.repository

import com.titan.newsappnative.feature_news.data.remote.NewsApi
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.feature_news.data.data_source.SharedPreference
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val bookmarkDao: BookmarkDao,
    private val sharedPreference: SharedPreference
) : NewsRepository {

    override suspend fun getHeadlines() = api.getHeadlines()

    override suspend fun getSearchResults(search: String) = api.getSearchResults(search)

    override suspend fun bookmarkArticle(bookmark: Bookmark) {
        bookmarkDao.insert(bookmark)
    }

    override suspend fun deleteFromBookmarks(bookmark: Bookmark) {
        bookmarkDao.delete(bookmark)
    }

    override fun isBookmarkSnackBarShown() = sharedPreference.showedBookmarkToast

    override fun setBookmarkSnackBarShown() {
        sharedPreference.showedBookmarkToast = true
    }
}