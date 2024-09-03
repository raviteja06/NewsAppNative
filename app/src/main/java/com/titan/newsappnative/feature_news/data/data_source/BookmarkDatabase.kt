package com.titan.newsappnative.feature_news.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.titan.newsappnative.feature_news.domain.model.Bookmark

@Database(
    entities = [Bookmark::class],
    version = 1,
    exportSchema = false
)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}