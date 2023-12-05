package com.ljb.unittest.android_1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ljb.unittest.NewsData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * 계층 테스트 ( Android 관련 테스트(androidTest) )
 *
 * Room Unit Test (ViewModel + LiveData + Mockito + Junit4)
 * */


/*
   AndroidJUnitRunner

   Espresso 및 UI Automator 테스트 프레임워크를 사용하는 것을 비롯 Android 기기에서
   JUnit3, JUnit4 스타일 테스트 클래스를 실행 할수 있는 JUit 테스트 실행기

   테스트 패키지와 테스트 대상 앱을 기기에 로드하여 테스를 실행하고 테스트 결과를 보고하는 역할
 */
@RunWith(AndroidJUnit4::class)
class NewsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: NewsDao
    private lateinit var database: NewsDatabase

    //Room DB, DAO 미리 생성
    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).build()
        dao = database.newsDao()
    }

    // DAO 메소드들을 테스트한 후 db 커넥션 close()
    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun saveNewsTest() = runBlocking {
        val news = listOf(
            NewsData(1,"overview1","","","",""),
            NewsData(2,"overview2","","","",""),
        )
        dao.saveNews(news)

        val allNews = dao.getNews()
        assertThat(allNews).isEqualTo(news)
    }

    @Test
    fun deleteNewsTest() = runBlocking {
        val news = listOf(
            NewsData(3,"overview3","","","",""),
            NewsData(4,"overview4","","","",""),
        )
        dao.saveNews(news)
        dao.deleteAllNews()
        val allNews = dao.getNews()
        assertThat(allNews).isEmpty()
    }
}