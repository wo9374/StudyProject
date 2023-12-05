package com.ljb.unittest.local_3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ljb.unittest.NewsData

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _newsList = MutableLiveData<List<NewsData>>(emptyList())

    fun getNews() : LiveData<List<NewsData>>{
        _newsList.value = newsRepository.getNews()
        return _newsList
    }
    fun updateMovies() : LiveData<List<NewsData>> {
        _newsList.value = newsRepository.updateNews()
         return _newsList
    }
}


// Service 가 있다고 가정한 NewsRepository
interface NewsRepository{

    fun getNews() : List<NewsData> {
        /*Service.getSearchNews().run {
            if (isSuccessful) {
                return body()?.items
            }else{
                return emptyList()
            }
        }*/
        return emptyList()
    }

    fun updateNews() : List<NewsData> {
        return emptyList()
    }

}