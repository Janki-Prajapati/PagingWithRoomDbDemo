package com.jp.test.pagingwithdbdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jp.test.pagingwithdbdemo.databinding.ItemProductListBinding
import com.jp.test.pagingwithdbdemo.models.ApiProducts

class AdapterProductList(private val dataList: List<ApiProducts.Product?>?) :
    RecyclerView.Adapter<AdapterProductList.ProductHolder>() {
    inner class ProductHolder(private val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(products: ApiProducts.Product) {
            binding.apply {
                data = products
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view =
            ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        dataList?.get(position)?.let { holder.bind(it) }
    }
}