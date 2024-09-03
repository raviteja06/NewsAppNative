package com.titan.newsappnative.feature_news.presentation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.databinding.ItemBookmarkBinding
import com.titan.newsappnative.di.BookmarkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class BookmarkAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var bookmarkListener: BookmarkManager.RemoveBookmarkListener
    private val bookmarksList: ArrayList<Bookmark> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderBinding(viewDataBinding)
    }

    override fun getItemCount(): Int {
        return bookmarksList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding =
            (holder as ViewHolderBinding).binding as ItemBookmarkBinding
        val item = bookmarksList[position]
        binding.title.text = item.title
        binding.author.text = item.author

        binding.deleteBookmark.setOnClickListener() {
            bookmarksList.removeAt(position)
            bookmarkListener.onRemoved(position, item)
            notifyItemRemoved(position)
        }
        binding.openArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            it.context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(bookmarks: List<Bookmark>) {
        bookmarksList.clear()
        bookmarks.forEach { bookmarksList.add(it) }
        notifyDataSetChanged()
    }

    fun add(position: Int, bookmark: Bookmark) {
        bookmarksList.add(position, bookmark)
        notifyItemInserted(position)
    }
}