package com.titan.newsappnative

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM Bookmarks")
    suspend fun get(): List<Bookmarks>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: Bookmarks)

    @Delete
    suspend fun delete(bookmark: Bookmarks)
}