package com.titan.newsappnative

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsAPI @Inject constructor(
    private val apiService: NewsApiService
) : ViewModel() {
    private val getHeadlines = NetworkCall<News>()
    private val getSearchResults = NetworkCall<News>()

    fun getHeadlines(): MutableLiveData<Resource<News>> {
        return getHeadlines.makeCall(apiService.getHeadlines())
    }

    fun getSearchResults(search: String): MutableLiveData<Resource<News>> {
        return getSearchResults.makeCall(apiService.getSearchResults(search))
    }

    override fun onCleared() {
        super.onCleared()
        getHeadlines.cancel()
        getSearchResults.cancel()
    }
}