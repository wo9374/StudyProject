package com.ljb.ktor.holiday

import android.util.Log
import com.ljb.ktor.BuildConfig
import com.ljb.ktor.news.NetworkState
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HolidayRepository {

    suspend fun getHoliday(solYear: String, solMonth: String): Flow<NetworkState<KtorResponse>> =
        flow {
            try {
                holidayClient.get("SpcdeInfoService/getHoliDeInfo") {
                    parameter("ServiceKey", BuildConfig.DATA_API_KEY_DECODE)
                    parameter("_type", "json")
                    parameter("numOfRows", "100")
                    parameter("solYear", solYear)
                    parameter("solMonth", solMonth)
                }.run {
                    if (status.isSuccess()) {
                        val item = deserializationJsonString(bodyAsText())
                        Log.d("MyTag", "HolidayItem : $item")

                        val response = body<KtorResponse>()
                        emit(NetworkState.Success(response))
                    } else {
                        emit(NetworkState.ApiError(status.description, status.value))
                    }
                }
            } catch (e: Exception) {
                emit(NetworkState.NetworkError(e))
            }
        }
}

fun deserializationJsonString(jsonString: String) : HolidayItem{
    // JSON 문자열을 JsonElement로 변환
    val jsonElement: JsonElement = Json.parseToJsonElement(jsonString)

    // 필요한 body 데이터에 접근
    val bodyJsonObject =
        jsonElement.jsonObject["response"]?.jsonObject?.get("body")?.jsonObject

    // HolidayItem 클래스로 역직렬화
    val holidayItem = bodyJsonObject?.let {
        val itemElement = it["items"]?.jsonObject?.get("item")  //items 안 [] jsonObject 감싸져 있음
        val holidayList = itemElement?.let { element ->
            Json.decodeFromJsonElement(element)
        } ?: listOf<Holiday>()

        HolidayItem(
            holidayList,
            it["numOfRows"]?.jsonPrimitive?.intOrNull ?: 0,
            it["pageNo"]?.jsonPrimitive?.intOrNull ?: 0,
            it["totalCount"]?.jsonPrimitive?.intOrNull ?: 0
        )
    } ?: HolidayItem(emptyList(), 0, 0, 0)

    return holidayItem
}