package com.ljb.designpattern

import com.ljb.extension.NetworkState
import com.ljb.network.RetrofitService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NewsRepository{
    suspend fun getSearchNews(keyword: String): Flow<NetworkState<List<NewsData>>> = flow {
        RetrofitService.naverService.getSearchNews(query = keyword, sort = "sim").run {
            if (isSuccessful) {
                body()?.let { response ->
                    emit(NetworkState.Success(response.items))
                }
            } else{
                emit(NetworkState.Error(message(), code()) )
            }
        }
    }
}