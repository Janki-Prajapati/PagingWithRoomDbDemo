package com.jp.test.pagingwithdbdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jp.test.pagingwithdbdemo.databinding.ItemQuoteLayoutBinding
import com.jp.test.pagingwithdbdemo.models.Result

class AdapterQuoteList : PagingDataAdapter<Result, AdapterQuoteList.QuoteViewHolder>(COMPARATOR) {
    class QuoteViewHolder(private val binding: ItemQuoteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result?) {
            binding.apply {
                data = item
            }
        }

    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = ItemQuoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(view)
    }

}