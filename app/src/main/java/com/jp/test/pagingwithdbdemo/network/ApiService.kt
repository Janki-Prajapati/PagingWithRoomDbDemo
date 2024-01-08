package com.jp.test.pagingwithdbdemo.network

import com.cheezycode.quickpagingdemo.models.QuoteList
import com.jp.test.pagingwithdbdemo.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/quotes")
    suspend fun getQuotes(@Query("page") page: Int): QuoteList
}