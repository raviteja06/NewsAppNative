package com.titan.newsappnative

import android.content.Context
import android.content.SharedPreferences

const val PREF_BOOKMARK_SHOWED = "pref_bookmark_showed"

object SharedPreference {

    private val sharedPreferences =
        NewsApp.application.getSharedPreferences("news", Context.MODE_PRIVATE)

    fun subscribe(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unsubscribe(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    var showedBookmarkToast: Boolean
        get() = sharedPreferences.getBoolean(PREF_BOOKMARK_SHOWED, false)
        set(value) = sharedPreferences.edit().putBoolean(PREF_BOOKMARK_SHOWED, value).apply()
}