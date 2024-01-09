package com.jp.test.pagingwithdbdemo.database.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jp.test.pagingwithdbdemo.database.QuoteDatabase
import com.jp.test.pagingwithdbdemo.models.QuoteRemoteKeys
import com.jp.test.pagingwithdbdemo.models.Result
import com.jp.test.pagingwithdbdemo.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class QuoteRemoteMediator(
    private val quoteApiService: ApiService,
    private val quoteDatabase: QuoteDatabase
) : RemoteMediator<Int, Result>() {

    private val STARTING_PAGE_INDEX = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    val quoteDao = quoteDatabase.quoteDao()
    val remoteKeysDao = quoteDatabase.remoteKeysDao()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        //Fetch quotes from API
        //save these quotes + remoyte keys into DB
        //Logic for states - REFRESH, PREPEND, APPEND

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }
        try {
            val response = quoteApiService.getQuotes(page)
            val endPaginationReached = response.totalPages == page

            quoteDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    quoteDao.deleteQuotes()
                    remoteKeysDao.deleteRemoteKeys()
                }

                val prevPage = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextPage = if (endPaginationReached) null else page + 1

                quoteDao.addQuotes(response.results)

                val keys = response.results.map { quote ->
                    QuoteRemoteKeys(id = quote._id, prevPage = prevPage, nextPage = nextPage)
                }

                remoteKeysDao.addAllRemoteKeys(keys)
            }
            return MediatorResult.Success(endPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Result>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextPage?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevPage ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextPage ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition
                ?.let { position ->
                    state.closestItemToPosition(position)?._id?.let { id ->
                        remoteKeysDao.getRemoteKeys(
                            id
                        )
                    }
                }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { quote -> remoteKeysDao.getRemoteKeys(id = quote._id) }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Result>): QuoteRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { quote -> remoteKeysDao.getRemoteKeys(id = quote._id) }

        }
    }
}