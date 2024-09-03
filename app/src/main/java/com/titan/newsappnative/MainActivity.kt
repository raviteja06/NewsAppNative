package com.titan.newsappnative

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.titan.newsappnative.databinding.ActivityMainBinding
import com.titan.newsappnative.di.BookmarkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BookmarkManager.BookmarkListener {
    private lateinit var binding: ActivityMainBinding
    private val apiModel: NewsAPI by viewModels()

    @Inject
    lateinit var bookmarkManager: BookmarkManager

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var preference: SharedPreference

    @Inject
    lateinit var bookmarksDao: BookmarksDao

    var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
        getHeadlines()
    }

    private fun setUp() {
        val divider =
            DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(baseContext, R.drawable.line_divider)
            ?.let { divider.setDrawable(it) }
        binding.recyclerView.addItemDecoration(divider)
        binding.recyclerView.adapter = newsAdapter
        binding.swipeRefresh.setOnRefreshListener {
            searchQuery?.let {
                if (it.isNotEmpty()) {
                    getSearchResults(it)
                } else {
                    getHeadlines()
                }
            } ?: getHeadlines()
        }
    }

    private fun getHeadlines() {
        apiModel.getHeadlines().observe(this) { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.articles?.let { newsAdapter.updateList(it) }
                    binding.totalResults.text =
                        getString(R.string.total_results, result.data?.totalResults)
                    if (!preference.showedBookmarkToast) {
                        Snackbar.make(
                            binding.main,
                            getString(R.string.bookmark_toast),
                            Snackbar.LENGTH_LONG
                        ).show()
                        preference.showedBookmarkToast = true
                    }
                }

                Resource.Status.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    Snackbar.make(
                        binding.main,
                        result.error ?: getString(R.string.something_went_wrong),
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.try_again)) {
                        getHeadlines()
                    }.show()
                }

                Resource.Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                }
            }
        }
    }

    private fun getSearchResults(query: String) {
        apiModel.getSearchResults(query).observe(this) { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.articles?.let { newsAdapter.updateList(it) }
                }

                Resource.Status.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    Snackbar.make(
                        binding.main,
                        result.error ?: getString(R.string.something_went_wrong),
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.try_again)) {
                        getHeadlines()
                    }.show()
                }

                Resource.Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                }
            }
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
                getSearchResults(query)
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

    override fun onBookmarked(bookmark: Bookmarks) {
        Snackbar.make(
            this.binding.main, getString(R.string.article_bookmarked), Toast.LENGTH_SHORT
        ).setAction(getString(R.string.undo)) {
            CoroutineScope(Dispatchers.IO).launch {
                bookmarksDao.delete(bookmark)
            }
        }.show()
    }

}