package com.titan.newsappnative.feature_news.presentation.activity

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.titan.newsappnative.R
import com.titan.newsappnative.base.Resource
import com.titan.newsappnative.databinding.ActivityMainBinding
import com.titan.newsappnative.di.BookmarkManager
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.domain.model.News
import com.titan.newsappnative.feature_news.domain.util.NetworkUtil
import com.titan.newsappnative.feature_news.presentation.adapter.NewsAdapter
import com.titan.newsappnative.feature_news.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BookmarkManager.BookmarkListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var bookmarkManager: BookmarkManager

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var networkUtil: NetworkUtil

    var searchQuery: String? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        callApi()
    }

    private fun setUp() {
        val divider =
            DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(baseContext, R.drawable.line_divider)
            ?.let { divider.setDrawable(it) }
        binding.recyclerView.addItemDecoration(divider)
        binding.recyclerView.adapter = newsAdapter
        binding.swipeRefresh.setOnRefreshListener {
            callApi()
        }
        viewModel.searchResultsDataStream.observe(this) { result ->
            processResult(result)
        }
        viewModel.highlightsDataStream.observe(this) { result ->
            processResult(result)
        }
    }

    private fun processResult(result: Resource<News?>) {
        when (result) {
            is Resource.Loading -> {
                binding.swipeRefresh.isRefreshing = true
            }

            is Resource.Success -> {
                binding.swipeRefresh.isRefreshing = false
                result.data?.articles?.let { newsAdapter.updateList(it) }
                binding.totalResults.text =
                    getString(R.string.total_results, result.data?.totalResults)
                if (!viewModel.isBookmarkSnackbarShown()) {
                    Snackbar.make(
                        binding.main,
                        getString(R.string.bookmark_toast),
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.setBookmarkSnackbarShown()
                }
            }

            is Resource.Error -> {
                binding.swipeRefresh.isRefreshing = false
                Snackbar.make(
                    binding.main,
                    result.message ?: getString(R.string.something_went_wrong),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.try_again)) {
                    callApi()
                }.show()
            }
        }
    }

    private fun callApi() {
        if (networkUtil.isOnline) {
            searchQuery?.let {
                if (it.isNotEmpty()) {
                    viewModel.getSearchResults(it)
                } else {
                    viewModel.getHeadlines()
                }
            } ?: viewModel.getHeadlines()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setMessage(getString(R.string.unable_to_connect_to_internet_to_fetch_news))
                .setTitle(getString(R.string.network_error))
                .setPositiveButton(
                    getString(R.string.ok)
                ) { _, _ ->
                    finish()
                }

            dialog = builder.create()
            dialog?.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val component = ComponentName(this, MainActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query
                callApi()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchQuery = newText
                return false
            }
        })
        val bookmarkView = menu.findItem(R.id.bookmarked) as MenuItem
        bookmarkView.setOnMenuItemClickListener {
            startActivity(Intent(this@MainActivity, BookmarkActivity::class.java))
            true
        }
        return true
    }

    override fun onBookmarked(bookmark: Bookmark) {
        viewModel.bookmarkArticle(bookmark)
        Snackbar.make(
            this.binding.main, getString(R.string.article_bookmarked), Toast.LENGTH_SHORT
        ).setAction(getString(R.string.undo)) {
            viewModel.deleteBookmark(bookmark)
        }.show()
    }

}