package com.ljb.network

import com.ljb.designpattern.BuildConfig
import com.ljb.designpattern.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverApi {
    @GET(RetrofitService.NEWS_END_POINT)
    suspend fun getSearchNews(
        @Header("X-Naver-Client-Id") clientId : String = BuildConfig.NAVER_CLIENT_ID,
        @Header("X-Naver-Client-Secret") clientPw : String = BuildConfig.NAVER_CLIENT_SECRET,
        @Query("query") query : String,
        //@Query("display") display: Int,
        @Query("sort") sort : String,
    ): Response<NewsResponse>
}