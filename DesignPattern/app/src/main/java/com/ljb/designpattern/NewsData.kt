package com.ljb.designpattern

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("items")
    val items : List<NewsData>
)

data class NewsData(

    @SerializedName("title")
    val title: String,

    @SerializedName("originallink")
    val originallink: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("pubDate")
    val pubDate: String,
)