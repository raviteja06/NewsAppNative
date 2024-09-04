package com.titan.newsappnative.feature_news.data.remote

import com.titan.newsappnative.feature_news.domain.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "2599b48c173f4f899166874a211fcfe2"
    ): Call<News>

    @GET("everything")
    fun getSearchResults(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "2599b48c173f4f899166874a211fcfe2"
    ): Call<News>
}