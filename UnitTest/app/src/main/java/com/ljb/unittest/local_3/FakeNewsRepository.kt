package com.ljb.unittest.local_3

class FakeNewsRepository : NewsRepository {
    private val news = mutableListOf<NewsData>()

    init {
        news.add(NewsData("overview1","","","",""))
        news.add(NewsData("overview2","","","",""))
    }

    override fun getNews(): List<NewsData> {
        return news
    }

    override fun updateNews(): List<NewsData> {
        news.clear()
        news.add(NewsData("overview3","","","",""))
        news.add(NewsData("overview4","","","",""))
        return news
    }
}