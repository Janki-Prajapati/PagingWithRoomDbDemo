package com.jp.test.pagingwithdbdemo.network

import com.cheezycode.quickpagingdemo.models.QuoteList
import com.jp.test.pagingwithdbdemo.utils.Constants
import com.jp.test.pagingwithdbdemo.models.ApiProducts
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.PRODUCTS)
    suspend fun getProducts(@Query("limit") limit : Int, @Query("skip") skip : Int) : ApiProducts

    @GET("/quotes")
    suspend fun getQuotes(@Query("page") page: Int): QuoteList
}