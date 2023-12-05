package com.ljb.cleanarchitecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljb.cleanarchitecture.databinding.ItemNumberBinding
import com.ljb.domain.model.NumberModel

/**
 * RecyclerView에 설정해줄 Adatper의 구현 클래스
 */
class NumberListAdapter : ListAdapter<NumberModel, NumberViewHolder>(NumberDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val binding = ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NumberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.binding(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()
}

class NumberViewHolder(private val binding: ItemNumberBinding) : RecyclerView.ViewHolder(binding.root) {
    internal fun binding(item: NumberModel) {
        binding.tvNumber.text = item.value.toString()
    }
}

/**
 * [ListAdapter]가 받는 새로운 데이터 리스트를 기존의 데이터 리스트와 비교하는 DiffUtil
 */
class NumberDiffUtil : DiffUtil.ItemCallback<NumberModel>() {
    override fun areItemsTheSame(oldItem: NumberModel, newItem: NumberModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NumberModel, newItem: NumberModel): Boolean {
        return oldItem.value == newItem.value
    }
}