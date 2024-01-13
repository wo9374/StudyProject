package com.ljb.ktor.holiday

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HolidayInfo {
    const val HOST = "apis.data.go.kr"
    const val PATH = "/B090041/openapi/service/"
}

val holidayClient = HttpClient(Android) {   //CIO 미사용시 Android
    expectSuccess = true        //응답 유효성 검사 true/false
    //followRedirects  = false    //리다이렉트 true/false

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = HolidayInfo.HOST
            path(HolidayInfo.PATH)
        }
    }

    // install: client configuration block에 plugin을 가져옴
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true          // Json string을 읽기 편하게 만들어줌
                isLenient = true            // "" 따옴표 잘못된 건 무시하고 처리
                ignoreUnknownKeys = true    // 모델에 없고, json에 있는 경우 해당 key 무시
                encodeDefaults = true       // null 인 값도 json에 포함 시킴
            }
        )
    }
    install(Logging) {
        //logger = Logger.DEFAULT
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("MyTag", message)
            }
        }
        level = LogLevel.ALL
        //filter {  request -> request.url.host.contains("ktor.io") }
    }

    install(ResponseObserver) {
        onResponse { response -> Log.d("MyTag", "HTTP status: ${response.status.value}") }
    }
}