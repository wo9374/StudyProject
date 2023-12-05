package com.ljb.unittest.local_3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ljb.unittest.NewsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * 로컬 단위 테스트 (Android 와 관련 없이 JVM 만 필요한 테스트, 알고리즘 테스트 등등)
 *
 * ViewModel + LiveData + 비동기로 데이터 불러오는 함수 테스트 + getOrAwaitValue() + Mockito + Junit4 + Truth
 * */
class NewsViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        val fakeNewsRepository = FakeNewsRepository()
        viewModel = NewsViewModel(fakeNewsRepository)
    }

    @Test
    fun getNews_returnCurrentList(){
        val news = mutableListOf<NewsData>()
        news.add(NewsData(0,"overview1","","","",""))
        news.add(NewsData(1,"overview2","","","",""))

        CoroutineScope(Dispatchers.IO).launch {
            val currentList = viewModel.getNews().getOrAwaitValue()
            assertThat(currentList).isEqualTo(news)
        }
    }

    @Test
    fun updateNews_returnUpdatedList(){
        val news = mutableListOf<NewsData>()
        news.add(NewsData(2,"overview3","","","",""))
        news.add(NewsData(3,"overview4","","","",""))

        CoroutineScope(Dispatchers.IO).launch{
            val updateList = viewModel.updateMovies().getOrAwaitValue()
            assertThat(updateList).isEqualTo(news)
        }
    }
}