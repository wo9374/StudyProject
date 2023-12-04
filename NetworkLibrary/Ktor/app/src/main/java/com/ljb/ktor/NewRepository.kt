package com.ljb.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsApi(private val client: HttpClient){
    suspend fun getNews(query: String) : HttpResponse = client.get{
        parameter("query", query)
    }
}

class NewRepository{
    private val newsApi = NewsApi(ktorClient)

    suspend fun getSearchNews(query: String): Flow<NetworkState<List<NewsData>>> = flow{
        try {
            newsApi.getNews(query).run {
                if (status.isSuccess()){
                    val response = body<NewsResponse>() //자동 역직렬화 필수
                    emit(NetworkState.Success(response.items))
                }else{
                    emit(NetworkState.ApiError(status.description, status.value))
                }
            }
        }catch (e: Exception){
            emit(NetworkState.NetworkError(e))
        }
    }
}