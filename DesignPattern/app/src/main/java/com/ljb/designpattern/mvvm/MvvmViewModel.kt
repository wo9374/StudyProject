package com.ljb.designpattern.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.designpattern.NewsData
import com.ljb.designpattern.NewsRepository
import com.ljb.extension.NetworkState
import com.ljb.extension.UiState
import com.ljb.extension.checkEmptyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MvvmViewModel : ViewModel() {
    private val newsRepository = NewsRepository()
    private var _newsList = MutableStateFlow<UiState<List<NewsData>>>(UiState.Loading)
    val newsList: StateFlow<UiState<List<NewsData>>> get() = _newsList

    fun getNews(query:String){
        viewModelScope.launch{
            newsRepository.getSearchNews(query).collectLatest { networkState->
                when(networkState){
                    is NetworkState.Success ->
                        _newsList.emit(checkEmptyData(networkState.data))

                    is NetworkState.Error ->
                        _newsList.emit(UiState.Fail(networkState.message))
                }
            }
        }
    }
}