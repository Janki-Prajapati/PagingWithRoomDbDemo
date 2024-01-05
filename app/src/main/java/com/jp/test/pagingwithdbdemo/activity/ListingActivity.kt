package com.jp.test.pagingwithdbdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jp.test.pagingwithdbdemo.adapter.AdapterProductList
import com.jp.test.pagingwithdbdemo.adapter.AdapterQuoteList
import com.jp.test.pagingwithdbdemo.databinding.ActivityListingBinding
import com.jp.test.pagingwithdbdemo.models.ApiProducts
import com.jp.test.pagingwithdbdemo.utils.Status
import com.jp.test.pagingwithdbdemo.viewModel.ViewModelProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingBinding
    val viewModelProduct: ViewModelProduct by viewModels()
    private lateinit var adapterQuoteList: AdapterQuoteList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        init()
        initQuoteList()
    }

    private fun initQuoteList() {
        adapterQuoteList = AdapterQuoteList()
        binding.quoteList.adapter = adapterQuoteList

        lifecycleScope.launch {
            viewModelProduct.list.collect {
                adapterQuoteList.submitData(it)
            }
        }
    }

    private fun init() {
        viewModelProduct.getProducts(20, 0)
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModelProduct.viewState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvProductList.visibility = View.GONE
                        Log.i("PokeListFragment", "Loading...")
                    }

                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvProductList.visibility = View.VISIBLE
                        it.data?.let { apiResponse ->
                            val list: List<ApiProducts.Product?>? = apiResponse.products

                            binding.rvProductList.adapter = AdapterProductList(list)
                            Log.i("PokeListFragment", "Received poke list.")
                        }
                            ?: run {
                                Log.e("PokeListFragment", "Error: Failed to fetch poke list.")
                            }
                    }

                    // error occurred status
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvProductList.visibility = View.GONE
                        Toast.makeText(this@ListingActivity, "${it.message}", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("PokeListFragment", it.message.toString())
                    }
                }
            }
        }
    }
}