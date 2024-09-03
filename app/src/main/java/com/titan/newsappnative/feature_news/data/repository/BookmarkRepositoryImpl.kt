package com.titan.newsappnative.feature_news.data.repository

import com.titan.newsappnative.feature_news.data.remote.NewsApi
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.feature_news.data.data_source.SharedPreference
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.repository.BookmarkRepository
import com.titan.newsappnative.feature_news.domain.repository.NewsRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override suspend fun getBookmarks(): List<Bookmark> = bookmarkDao.get()

    override suspend fun bookmarkArticle(bookmark: Bookmark) {
        bookmarkDao.insert(bookmark)
    }

    override suspend fun deleteFromBookmarks(bookmark: Bookmark) {
        bookmarkDao.delete(bookmark)
    }
}