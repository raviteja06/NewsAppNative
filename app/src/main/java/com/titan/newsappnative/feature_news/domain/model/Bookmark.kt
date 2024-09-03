package com.titan.newsappnative.feature_news.domain.model

import androidx.room.Entity

@Entity(tableName = "Bookmark", primaryKeys = ["url"])
data class Bookmark(
    val author: String,
    val title: String,
    val url: String
)
