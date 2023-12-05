package com.ljb.unittest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String,
)