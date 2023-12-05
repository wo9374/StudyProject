package com.ljb.unittest.android_1

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ljb.unittest.NewsData

@Database(entities = [NewsData::class], version = 1)
abstract class NewsDatabase : RoomDatabase(){
    abstract fun newsDao(): NewsDao
}