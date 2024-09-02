package com.titan.newsappnative

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.titan.newsappnative.databinding.ActivityBookmarkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding

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
            val bookmarks = AppDatabase.instance.bookmarks().get()
            withContext(Dispatchers.Main) {
                binding.recyclerView.adapter =
                    BookmarkAdapter(arrayListOf<Bookmarks>().apply {
                        bookmarks.forEach { this.add(it) }
                    })
            }
        }
    }
}