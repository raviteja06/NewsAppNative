package com.titan.newsappnative.feature_news.presentation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.titan.newsappnative.databinding.ItemNewsBinding
import com.titan.newsappnative.di.BookmarkManager
import com.titan.newsappnative.feature_news.domain.model.Article
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import javax.inject.Inject

class NewsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var bookmarkListener: BookmarkManager.BookmarkListener
    private val newsList = ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderBinding(viewDataBinding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(articles: List<Article>) {
        newsList.clear()
        newsList.addAll(articles)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding =
            (holder as ViewHolderBinding).binding as ItemNewsBinding
        val item = newsList[position]
        binding.title.text = item.title
        binding.author.text = item.author

        binding.main.setOnLongClickListener {
            val bookmark = Bookmark(
                item.author,
                item.title,
                item.url
            )
            bookmarkListener.onBookmarked(bookmark)
            return@setOnLongClickListener true
        }
        binding.openArticle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            it.context.startActivity(intent)
        }
    }
}

open class ViewHolderBinding(open var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)