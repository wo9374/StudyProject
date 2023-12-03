package com.ljb.ktor

import android.util.Log
import com.ljb.ktor.ApiInfo.HEADER_ID
import com.ljb.ktor.ApiInfo.HEADER_SECRET
import com.ljb.ktor.ApiInfo.HOST
import com.ljb.ktor.ApiInfo.PATH
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object ApiInfo {
    //const val BASE_URL = "https://openapi.naver.com/v1/search/news.json"
    const val HOST = "openapi.naver.com"
    const val PATH = "/v1/search/news.json"

    const val HEADER_ID = "X-Naver-Client-Id"
    const val HEADER_SECRET = "X-Naver-Client-Secret"
}

val ktorClient = HttpClient(CIO) {   //CIO 미사용시 Android
    expectSuccess = true        //응답 유효성 검사 true/false
    //followRedirects  = false    //리다이렉트 true/false

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = HOST
            path(PATH)
        }

        headers {
            header(HEADER_ID, BuildConfig.NAVER_CLIENT_ID)
            header(HEADER_SECRET, BuildConfig.NAVER_CLIENT_SECRET)
        }
    }

    // install: client configuration block에 plugin을 가져옴
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true          // Json string을 읽기 편하게 만들어줌
                isLenient = true            // "" 따옴표 잘못된 건 무시하고 처리
                encodeDefaults = true       // null 인 값도 json에 포함 시킴
                ignoreUnknownKeys = true    // 모델에 없고, json에 있는 경우 해당 key 무시
            }
        )
    }
    install(Logging) {
        //logger = Logger.DEFAULT
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Ktor Logger", message)
            }
        }
        level = LogLevel.ALL
        //filter {  request -> request.url.host.contains("ktor.io") }
    }

    install(ResponseObserver) {
        onResponse { response -> Log.d("HTTP status: ", "${response.status.value}") }
    }
}