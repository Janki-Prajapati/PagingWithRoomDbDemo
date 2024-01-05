package com.jp.test.pagingwithdbdemo.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jp.test.pagingwithdbdemo.QuotePagingSource
import com.jp.test.pagingwithdbdemo.models.ApiProducts
import com.jp.test.pagingwithdbdemo.utils.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(limit: Int, skip: Int): Flow<ViewState<ApiProducts>> {
        return flow { emit(ViewState.success(apiService.getProducts(limit, skip))) }.flowOn(
            Dispatchers.IO
        )
    }

    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { QuotePagingSource(apiService) }).flow
}