package com.titan.newsappnative

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.collect.Maps
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.titan.newsappnative.feature_news.data.remote.NewsApi
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


@RunWith(AndroidJUnit4::class)
class GetHighlightsAPITest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: NewsApi
    private val server = MockWebServer()
    private lateinit var mockedResponse: String
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Before
    fun init() {
        val BASE_URL = "https://newsapi.org/v2/"
        server.start(8000)
        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        apiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(NewsApi::class.java)
    }

    @Test
    fun testApiSuccess() {
        mockedResponse = MockResponseFileReader("headlines/success.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )
        val response = runBlocking { apiService.getHeadlines().execute() }
        assertNotNull(response)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}