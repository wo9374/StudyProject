package com.ljb.designpattern.mvp

import com.ljb.designpattern.NewsRepository
import com.ljb.designpattern.NewsResponse
import com.ljb.extension.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

class MvpPresenter(
    private val view: MvpContract.View,
    private val newsRepository: NewsRepository
) : MvpContract.Presenter {

    override suspend fun loadData(query: String) {

        withContext(Dispatchers.Main){
            view.setData(UiState.Loading)
        }

        newsRepository.getSearchNews(query).collectLatest {
            withContext(Dispatchers.Main){
                view.setData(it)
            }
        }
    }
}

interface MvpContract {
    interface View {
        fun setData(uiState: UiState<NewsResponse>)
    }

    interface Presenter {
        suspend fun loadData(query: String)
    }
}