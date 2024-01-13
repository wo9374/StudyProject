package com.ljb.ktor.holiday

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KtorResponse(
    @SerialName("response") val response: Response
){
    @Serializable
    data class Response(
        @SerialName("header") var header: Header,
        @SerialName("body") var body: HolidayBody
    ) {
        @Serializable
        data class Header(
            @SerialName("resultCode") var resultCode: String,
            @SerialName("resultMsg") var resultMsg: String
        )

        @Serializable
        data class HolidayBody(
            @SerialName("items") var items: HolidayItem,
            @SerialName("numOfRows") var numOfRows: Int,
            @SerialName("pageNo") var pageNo: Int,
            @SerialName("totalCount") var totalCount: Int
        ) {
            @Serializable
            data class HolidayItem(
                @SerialName("item") var item: List<HolidayData>
            ) {
                @Serializable
                data class HolidayData(
                    @SerialName("dateKind") var dateKind: String,
                    @SerialName("dateName") var dateName: String,
                    @SerialName("isHoliday") var isHoliday: String,
                    @SerialName("locdate") var locdate: String,
                    @SerialName("seq") var seq: Int
                )
            }
        }
    }
}

data class HolidayItem(
    val holidays: List<Holiday>,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

@Serializable
data class Holiday(
    val dateKind: String,
    val dateName: String,
    val isHoliday: String,
    val locdate: Int,
    val seq: Int
)