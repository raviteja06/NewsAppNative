package com.titan.newsappnative.feature_news.domain.repository

import com.titan.newsappnative.feature_news.domain.model.Bookmark

interface BookmarkRepository {
    suspend fun getBookmarks(): List<Bookmark>
    suspend fun bookmarkArticle(bookmark: Bookmark)
    suspend fun deleteFromBookmarks(bookmark: Bookmark)
}