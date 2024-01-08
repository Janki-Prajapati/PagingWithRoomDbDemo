package com.jp.test.pagingwithdbdemo.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jp.test.pagingwithdbdemo.adapter.AdapterQuoteList
import com.jp.test.pagingwithdbdemo.databinding.ActivityListingBinding
import com.jp.test.pagingwithdbdemo.viewModel.ViewModelQuote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingBinding
    val viewModelQuote: ViewModelQuote by viewModels()
    private lateinit var adapterQuoteList: AdapterQuoteList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initQuoteList()
    }

    private fun initQuoteList() {
        adapterQuoteList = AdapterQuoteList()
        binding.quoteList.adapter = adapterQuoteList

        lifecycleScope.launch {
            viewModelQuote.list.collect {
                adapterQuoteList.submitData(it)
            }
        }
    }
}