package com.jp.test.pagingwithdbdemo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jp.test.pagingwithdbdemo.network.ApiRepository
import com.jp.test.pagingwithdbdemo.utils.Status
import com.jp.test.pagingwithdbdemo.utils.ViewState
import com.jp.test.pagingwithdbdemo.models.ApiProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProduct @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    val viewState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            ApiProducts(), ""
        )
    )

    fun getProducts(limit: Int, skip: Int) {
        viewState.value = ViewState.loading()

        viewModelScope.launch(Dispatchers.IO){
            apiRepository.getProducts(limit, skip).catch {
                viewState.value = ViewState.error(it.message.toString())
            }.collect{
                viewState.value = ViewState.success(it.data)
            }
        }
    }

    val list = apiRepository.getQuotes().cachedIn(viewModelScope)
}