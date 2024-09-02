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

    @Query("SELECT * FROM Bookmarks WHERE url= :url")
    suspend fun getUrl(url: String): Bookmarks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: Bookmarks)

    @Delete
    suspend fun delete(bookmark: Bookmarks)

    @Query("DELETE FROM Bookmarks")
    suspend fun clear()
}