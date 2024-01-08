package com.jp.test.pagingwithdbdemo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jp.test.pagingwithdbdemo.database.QuoteDatabase
import com.jp.test.pagingwithdbdemo.models.QuoteRemoteKeys
import com.jp.test.pagingwithdbdemo.models.Result
import com.jp.test.pagingwithdbdemo.network.ApiService

@ExperimentalPagingApi
class QuoteRemoteMediator(
    private val quoteApiService: ApiService,
    private val quoteDatabase: QuoteDatabase
) : RemoteMediator<Int, Result>() {

    val quoteDao = quoteDatabase.quoteDao()
    val remoteKeysDao = quoteDatabase.remoteKeysDao()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        //Fetch quotes from API
        //save these quotes + remoyte keys into DB
        //Logic for states - REFRESH, PREPEND, APPEND
        return try {

            val currentPage = when (loadType) {
                LoadType.REFRESH -> {

                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }
            val response = quoteApiService.getQuotes(currentPage)
            val endPaginationReached = response.totalPages == currentPage

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endPaginationReached) null else currentPage + 1

            quoteDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    quoteDao.deleteQuotes()
                    remoteKeysDao.deleteRemoteKeys()
                }

                quoteDao.addQuotes(response.results)
                val keys = response.results.map { quote ->
                    QuoteRemoteKeys(id = quote._id, prevPage = prevPage, nextPage = nextPage)
                }

                remoteKeysDao.addAllRemoteKeys(keys)
            }
            MediatorResult.Success(endPaginationReached)

        } catch (e: Exception) {
            println(">>> ${e.message}")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.anchorPosition
            ?.let { position ->
                state.closestItemToPosition(position)?._id?.let { id ->
                    remoteKeysDao.getRemoteKeys(
                        id
                    )
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { quote -> remoteKeysDao.getRemoteKeys(id = quote._id) }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { quote -> remoteKeysDao.getRemoteKeys(id = quote._id) }

    }
}