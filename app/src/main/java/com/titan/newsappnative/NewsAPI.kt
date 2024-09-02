package com.titan.newsappnative

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class NewsAPI : ViewModel() {
    private val getHeadlines = NetworkCall<News>()
    private val getSearchResults = NetworkCall<News>()

    fun getHeadlines(): MutableLiveData<Resource<News>> {
        return getHeadlines.makeCall(RetrofitBuilder.apiService.getHeadlines())
    }

    fun getSearchResults(search: String): MutableLiveData<Resource<News>> {
        return getSearchResults.makeCall(RetrofitBuilder.apiService.getSearchResults(search))
    }

    override fun onCleared() {
        super.onCleared()
        getHeadlines.cancel()
        getSearchResults.cancel()
    }
}