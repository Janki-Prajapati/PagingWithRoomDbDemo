package com.jp.test.pagingwithdbdemo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cheezycode.quickpagingdemo.models.QuoteList
import com.jp.test.pagingwithdbdemo.models.QuoteRemoteKeys
import com.jp.test.pagingwithdbdemo.models.Result

@Database(entities = [Result::class, QuoteRemoteKeys ::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao
    abstract fun remoteKeysDao() : RemoteKeysDao
}