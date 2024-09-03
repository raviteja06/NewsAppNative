package com.titan.newsappnative.di

import android.app.Activity
import com.titan.newsappnative.Bookmarks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
object BookmarkModule{

    @Provides
    fun provideCallback(activity: Activity) = activity as BookmarkManager.BookmarkListener
}

class BookmarkManager @Inject constructor(
    private val listener: BookmarkListener
) {

    fun activateCallback(bookmark: Bookmarks){
        listener.onBookmarked(bookmark)
    }

    interface BookmarkListener {
        fun onBookmarked(bookmark: Bookmarks)
    }
}