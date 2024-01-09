package com.jp.test.pagingwithdbdemo.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cheezycode.quickpagingdemo.models.QuoteList
import com.jp.test.pagingwithdbdemo.models.Result

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuotes(quotes: List<Result>)

    @Query("SELECT * FROM Quote")
    fun getQuotes() : PagingSource<Int, Result>

    @Query("DELETE FROM quote")
    fun deleteQuotes()
}