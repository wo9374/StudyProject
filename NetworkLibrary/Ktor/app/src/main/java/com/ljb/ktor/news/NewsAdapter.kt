package com.ljb.ktor.news

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljb.ktor.R
import com.ljb.ktor.databinding.ItemNewsBinding
import com.ljb.ktor.dp
import com.ljb.ktor.htmlToString

class NewsAdapter : ListAdapter<NewsData, NewsViewHolder>(NewsDiff()){
    lateinit var binding: ItemNewsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_news, parent, false)
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

class NewsDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = 10.dp
        outRect.left = 10.dp
        outRect.right = 10.dp

        //val position = parent.getChildAdapterPosition(view) //각 아이템뷰의 순서 (index)
        //val totalItemCount = state.itemCount                //총 아이템 수
        //val scrollPosition = state.targetScrollPosition     //스크롤 됬을때 아이템 position
        //outRect.set(0,0,0,0)                                //left, top, bottom, right 한번에 주는 속성
    }
}