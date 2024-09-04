package com.titan.newsappnative

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDatabase
import com.titan.newsappnative.feature_news.data.data_source.SharedPreference
import com.titan.newsappnative.feature_news.data.remote.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, BookmarkDatabase::class.java
        ).allowMainThreadQueries().build()
}