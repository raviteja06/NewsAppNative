package com.titan.newsappnative

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private const val TIMEOUT: Long = 6000

    private fun getRetrofit(
        accept: String = "application/json"
    ): Retrofit {
        val b = OkHttpClient.Builder()
        b.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        b.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        b.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        val logging = HttpLoggingInterceptor { message ->
            Log.i("OKHTTP:", message)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient: OkHttpClient.Builder =
            b.addInterceptor(logging).addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", accept)
                builder.method(original.method, original.body)
                chain.proceed(builder.build())
            })
        okHttpClient.retryOnConnectionFailure(true)

        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient.build())
            .build()
    }

    val apiService: NewsApiService = getRetrofit().create(NewsApiService::class.java)
}