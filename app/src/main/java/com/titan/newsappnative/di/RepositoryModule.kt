package com.titan.newsappnative.di

import com.titan.newsappnative.feature_news.data.repository.BookmarkRepositoryImpl
import com.titan.newsappnative.feature_news.data.repository.NewsRepositoryImpl
import com.titan.newsappnative.feature_news.domain.repository.BookmarkRepository
import com.titan.newsappnative.feature_news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository
}