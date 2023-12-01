package com.ljb.designpattern.mvp

import com.ljb.designpattern.NewsData
import com.ljb.designpattern.NewsRepository
import com.ljb.extension.NetworkState
import com.ljb.extension.UiState
import com.ljb.extension.checkEmptyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

class MvpPresenter(
    private val view: MvpContract.View,
    private val newsRepository: NewsRepository
) : MvpContract.Presenter {

    override suspend fun loadData(query: String) {
        view.setData(UiState.Loading)

        newsRepository.getSearchNews(query).flowOn(Dispatchers.IO).collectLatest { networkState->
            when(networkState){
                is NetworkState.Success ->
                    view.setData(checkEmptyData(networkState.data))

                is NetworkState.Error ->
                    view.setData(UiState.Fail(networkState.message))
            }
        }
    }
}

interface MvpContract {
    interface View {
        fun setData(uiState: UiState<List<NewsData>>)
    }

    interface Presenter {
        suspend fun loadData(query: String)
    }
}