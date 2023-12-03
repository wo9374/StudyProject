package com.ljb.ktor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsResponse(
    @SerialName("items")
    val items : List<NewsData>
)

//직렬화를 통해 json 객체로 파싱
@Serializable
data class NewsData(
    @SerialName("title") val title: String,
    @SerialName("originallink") val originallink: String,
    @SerialName("link") val link: String,
    @SerialName("description") val description: String,
    @SerialName("pubDate") val pubDate: String,
)


sealed class NetworkState<out T> {
    data class Success<out T>(val data: T) : NetworkState<T>()
    class NetworkError<T>(val throwable: Throwable) : NetworkState<T>()
    data class ApiError(val message: String?, val errorCode: Int,) : NetworkState<Nothing>()
}