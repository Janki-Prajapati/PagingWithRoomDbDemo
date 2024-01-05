package com.cheezycode.quickpagingdemo.models

import com.jp.test.pagingwithdbdemo.models.Result

data class QuoteList(
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
)