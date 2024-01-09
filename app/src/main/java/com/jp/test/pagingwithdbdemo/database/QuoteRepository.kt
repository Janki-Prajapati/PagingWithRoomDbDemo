package com.jp.test.pagingwithdbdemo.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jp.test.pagingwithdbdemo.database.repository.QuoteRemoteMediator
import com.jp.test.pagingwithdbdemo.network.ApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class QuoteRepository @Inject constructor(private val apiService: ApiService, private val quoteDatabase: QuoteDatabase) {

    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
        remoteMediator = QuoteRemoteMediator(apiService,quoteDatabase),
        pagingSourceFactory = { quoteDatabase.quoteDao().getQuotes() }).flow
}