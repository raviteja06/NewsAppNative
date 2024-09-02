package com.titan.newsappnative

import androidx.room.Entity

@Entity(tableName = "Bookmarks", primaryKeys = ["url"])
data class Bookmarks(
    val author: String,
    val title: String,
    val url: String
)
