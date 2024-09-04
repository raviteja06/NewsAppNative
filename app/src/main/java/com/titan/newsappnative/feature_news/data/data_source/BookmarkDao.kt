package com.titan.newsappnative.feature_news.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.titan.newsappnative.feature_news.domain.model.Bookmark

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM Bookmark")
    suspend fun get(): List<Bookmark>

    @Query("SELECT * FROM Bookmark WHERE url=:url limit 1")
    suspend fun get(url: String): Bookmark?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)
}