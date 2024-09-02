package com.titan.newsappnative

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Bookmarks::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarks(): BookmarksDao

    companion object {
        val instance: AppDatabase by lazy {
            Room.databaseBuilder(
                NewsApp.application,
                AppDatabase::class.java,
                NewsApp.application.packageName
            ).build()
        }
    }
}