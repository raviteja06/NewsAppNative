package com.titan.newsappnative.di

import android.app.Activity
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
object BookmarkModule{

    @Provides
    fun provideBookmarkedCallback(activity: Activity) = activity as BookmarkManager.BookmarkListener

    @Provides
    fun provideRemoveBookmarkCallback(activity: Activity) = activity as BookmarkManager.RemoveBookmarkListener
}

class BookmarkManager @Inject constructor() {

    interface BookmarkListener {
        fun onBookmarked(bookmark: Bookmark)
    }

    interface RemoveBookmarkListener {
        fun onRemoved(position: Int, bookmark: Bookmark)
    }
}