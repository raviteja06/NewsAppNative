package com.titan.newsappnative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.titan.newsappnative.databinding.ItemBookmarkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookmarkAdapter(private val bookmarksList: ArrayList<Bookmarks>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.instance.bookmarks().delete(item)
            }
            bookmarksList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}