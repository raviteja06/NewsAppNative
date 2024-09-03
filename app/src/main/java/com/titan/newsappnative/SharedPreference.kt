package com.titan.newsappnative

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

const val PREF_BOOKMARK_SHOWED = "pref_bookmark_showed"

class SharedPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {

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