package com.titan.newsappnative.feature_news.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.R
import com.titan.newsappnative.databinding.ActivityBookmarkBinding
import com.titan.newsappnative.di.BookmarkManager
import com.titan.newsappnative.feature_news.presentation.adapter.BookmarkAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity(), BookmarkManager.RemoveBookmarkListener {
    private lateinit var binding: ActivityBookmarkBinding

    @Inject
    lateinit var bookmark: BookmarkDao

    @Inject
    lateinit var bookmarkAdapter: BookmarkAdapter

    @Inject
    lateinit var bookmarkManager: BookmarkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
        getData()
    }

    private fun setUp() {
        val divider =
            DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(baseContext, R.drawable.line_divider)
            ?.let { divider.setDrawable(it) }
        binding.recyclerView.addItemDecoration(divider)
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            val bookmarks = bookmark.get()
            withContext(Dispatchers.Main) {
                binding.recyclerView.adapter = bookmarkAdapter
                bookmarkAdapter.update(bookmarks)
            }
        }
    }

    override fun onRemoved(position: Int, bookmark: Bookmark) {
        Snackbar.make(
            this.binding.main, getString(R.string.article_bookmarked_removed), Toast.LENGTH_SHORT
        ).setAction(getString(R.string.undo)) {
            bookmarkAdapter.add(position, bookmark)
        }.show()
    }
}