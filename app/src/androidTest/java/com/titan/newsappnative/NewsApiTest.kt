package com.titan.newsappnative

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.GsonBuilder
import com.titan.newsappnative.feature_news.data.remote.NewsApi
import junit.framework.TestCase.assertNotNull
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


@RunWith(AndroidJUnit4::class)
class NewsApiTest {
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
        server.start(8000)
        val baseUrl = server.url("/").toString()
        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        apiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build().create(NewsApi::class.java)
    }

    @Test
    fun testHeadlinesAPI() {
        val mockedResponse = MockResponseFileReader("headlines/success.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )
        val response = runBlocking { apiService.getHeadlines().execute() }
        assertNotNull(response)
        assertNotNull(response.body()?.status)
        assertNotNull(response.body()?.articles)
    }

    @Test
    fun testSearchResultsAPI() {
        val mockedResponse = MockResponseFileReader("search_results/success.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )
        val response = runBlocking { apiService.getSearchResults("Bitcoin").execute() }
        assertNotNull(response)
        assertNotNull(response.body()?.status)
        assertNotNull(response.body()?.articles)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}