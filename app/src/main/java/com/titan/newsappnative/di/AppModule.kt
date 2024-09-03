package com.titan.newsappnative.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDatabase
import com.titan.newsappnative.feature_news.data.data_source.SharedPreference
import com.titan.newsappnative.feature_news.data.remote.NewsApi
import com.titan.newsappnative.feature_news.domain.util.NetworkUtil
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        val timeout: Long = 6000
        val b = OkHttpClient.Builder()
        b.connectTimeout(timeout, TimeUnit.MILLISECONDS)
        b.readTimeout(timeout, TimeUnit.MILLISECONDS)
        b.writeTimeout(timeout, TimeUnit.MILLISECONDS)
        val logging = HttpLoggingInterceptor { message ->
            Log.i("OKHTTP:", message)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient: OkHttpClient.Builder =
            b.addInterceptor(logging).addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/json")
                builder.method(original.method, original.body)
                chain.proceed(builder.build())
            })
        okHttpClient.retryOnConnectionFailure(true)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): BookmarkDatabase =
        Room.databaseBuilder(
            context,
            BookmarkDatabase::class.java,
            context.packageName
        ).build()

    @Provides
    @Singleton
    fun bookmarks(database: BookmarkDatabase): BookmarkDao =
        database.bookmarkDao()

    @Provides
    @Singleton
    fun initSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("news", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun sharedPreference(sharedPreference: SharedPreferences): SharedPreference =
        SharedPreference(sharedPreference)

    @Provides
    fun networkUtil(@ApplicationContext context: Context): NetworkUtil = NetworkUtil(context)
}