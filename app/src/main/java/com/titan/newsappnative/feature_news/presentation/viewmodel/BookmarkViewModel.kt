package com.titan.newsappnative.feature_news.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repositoryImpl: BookmarkRepository) : ViewModel() {
    val bookmarksDataStream: MutableLiveData<List<Bookmark>> = MutableLiveData()

    fun getBookmarks() {
        viewModelScope.launch {
            bookmarksDataStream.postValue(repositoryImpl.getBookmarks())
        }
    }

    fun bookmarkArticle(bookmark: Bookmark) =
        viewModelScope.launch { repositoryImpl.bookmarkArticle(bookmark) }

    fun deleteBookmark(bookmark: Bookmark) =
        viewModelScope.launch { repositoryImpl.deleteFromBookmarks(bookmark) }
}