package com.ljb.data.di

import android.content.Context
import androidx.room.Room
import com.ljb.data.database.NumbersDao
import com.ljb.data.database.NumbersDatabase
import com.ljb.data.datasource.LocalDataSource
import com.ljb.data.datasource.LocalDataSourceImpl
import org.koin.dsl.module

val databaseModule = module {

    single {
        createDatabase(get())
    }

    single {
        createNumbersDao(get())
    }

    single {
        createLocalDataSource(get())
    }
}

fun createDatabase(appContext: Context): NumbersDatabase =
    Room.databaseBuilder(appContext, NumbersDatabase::class.java, "number_db").build()

fun createNumbersDao(database: NumbersDatabase) : NumbersDao = database.dao()

fun createLocalDataSource(dao: NumbersDao): LocalDataSource = LocalDataSourceImpl(dao)