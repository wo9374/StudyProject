package com.ljb.designpattern

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljb.designpattern.databinding.ItemNewsBinding
import com.ljb.extension.htmlToString

class NewsAdapter : ListAdapter<NewsData, NewsViewHolder>(NewsDiff()){
    lateinit var binding: ItemNewsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_news, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root){
    fun onBind(news: NewsData){
        binding.apply {
            title.text = news.title.htmlToString()
            description.text = news.description.htmlToString()
        }
    }
}

class NewsDiff: DiffUtil.ItemCallback<NewsData>(){
    override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
        return (oldItem.title == newItem.title)
    }

    override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
        return (oldItem.title == newItem.title)
    }
}