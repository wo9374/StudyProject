package com.ljb.designpattern

import com.ljb.extension.UiState
import com.ljb.network.RetrofitService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NewsRepository{
    suspend fun getSearchNews(keyword: String): Flow<UiState<NewsResponse>> = flow {
        RetrofitService.naverService.getSearchNews(query = keyword, sort = "sim").run {
            if (isSuccessful) {
                body()?.let { response ->
                    if (response.items.isEmpty())
                        emit(UiState.Empty)
                    else
                        emit(UiState.Complete(response))
                }
            } else
                emit(UiState.Fail(message()))
        }
    }
}