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