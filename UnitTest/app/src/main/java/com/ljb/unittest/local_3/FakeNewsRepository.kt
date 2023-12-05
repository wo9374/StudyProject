package com.ljb.unittest.local_3

import com.ljb.unittest.NewsData

class FakeNewsRepository : NewsRepository {
    private val news = mutableListOf<NewsData>()

    init {
        news.add(NewsData(0,"overview1","","","",""))
        news.add(NewsData(1,"overview2","","","",""))
    }

    override suspend fun getNews(): List<NewsData> {
        return news
    }

    override suspend fun updateNews(): List<NewsData> {
        news.clear()
        news.add(NewsData(2,"overview3","","","",""))
        news.add(NewsData(3,"overview4","","","",""))
        return news
    }
}