package com.titan.newsappnative

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        init(this)
    }

    companion object {
        lateinit var application: Application
        fun init(app: Application) {
            application = app
        }
    }
}