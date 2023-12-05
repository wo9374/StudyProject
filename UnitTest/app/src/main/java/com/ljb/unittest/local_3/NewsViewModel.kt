package com.ljb.unittest.local_3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ljb.unittest.NewsData

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    suspend fun getNews() = liveData {
        emit(newsRepository.getNews())
    }
    suspend fun updateMovies() = liveData {
        emit(newsRepository.updateNews())
    }
}


// Service 가 있다고 가정한 NewsRepository
interface NewsRepository{

    suspend fun getNews() : List<NewsData> {
        /*Service.getSearchNews().run {
            if (isSuccessful) {
                return body()?.items
            }else{
                return emptyList()
            }
        }*/
        return emptyList()
    }

    suspend fun updateNews() : List<NewsData> {
        return emptyList()
    }

}