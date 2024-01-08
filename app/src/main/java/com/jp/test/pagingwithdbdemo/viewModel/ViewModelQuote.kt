package com.jp.test.pagingwithdbdemo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cheezycode.quickpagingdemo.models.QuoteList
import com.jp.test.pagingwithdbdemo.database.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelQuote @Inject constructor(private val quoteRepository: QuoteRepository) :
    ViewModel() {
    val list = quoteRepository.getQuotes().cachedIn(viewModelScope)
}