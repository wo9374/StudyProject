package com.ljb.unittest.local_3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NewsViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        val fakeNewsRepository = FakeNewsRepository()
        viewModel = NewsViewModel(fakeNewsRepository)
    }

    @Test
    fun getNews_returnCurrentList(){
        val news = mutableListOf<NewsData>()
        news.add(NewsData("overview1","","","",""))
        news.add(NewsData("overview2","","","",""))

        val currentList = viewModel.getNews().getOrAwaitValue()
        assertThat(currentList).isEqualTo(news)
    }

    @Test
    fun updateNews_returnUpdatedList(){
        val news = mutableListOf<NewsData>()
        news.add(NewsData("overview3","","","",""))
        news.add(NewsData("overview4","","","",""))

        val updateList = viewModel.updateMovies().getOrAwaitValue()
        assertThat(updateList).isEqualTo(news)
    }
}