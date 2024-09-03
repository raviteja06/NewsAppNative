package com.titan.newsappnative.feature_news.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titan.newsappnative.base.Resource
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.model.News
import com.titan.newsappnative.feature_news.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repositoryImpl: NewsRepository) : ViewModel() {
    val searchResultsDataStream: MutableLiveData<Resource<News?>> = MutableLiveData()
    val highlightsDataStream: MutableLiveData<Resource<News?>> = MutableLiveData()

    fun getHeadlines() {
        viewModelScope.launch {
            highlightsDataStream.postValue(Resource.Loading())
            repositoryImpl.getHeadlines().enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    if (response.isSuccessful) {
                        highlightsDataStream.postValue(Resource.Success(response.body()))
                    } else {
                        highlightsDataStream.postValue(Resource.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    highlightsDataStream.postValue(Resource.Error(t.message))
                }
            })
        }
    }

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            searchResultsDataStream.postValue(Resource.Loading())
            repositoryImpl.getSearchResults(query).enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    if (response.isSuccessful) {
                        searchResultsDataStream.postValue(Resource.Success(response.body()))
                    } else {
                        searchResultsDataStream.postValue(Resource.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    searchResultsDataStream.postValue(Resource.Error(t.message))
                }
            })
        }
    }

    fun bookmarkArticle(bookmark: Bookmark) =
        viewModelScope.launch { repositoryImpl.bookmarkArticle(bookmark) }

    fun deleteBookmark(bookmark: Bookmark) =
        viewModelScope.launch { repositoryImpl.deleteFromBookmarks(bookmark) }

    fun isBookmarkSnackbarShown() = repositoryImpl.isBookmarkSnackBarShown()

    fun setBookmarkSnackbarShown() = repositoryImpl.setBookmarkSnackBarShown()
}