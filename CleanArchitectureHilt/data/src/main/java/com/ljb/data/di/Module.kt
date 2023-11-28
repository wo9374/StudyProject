package com.ljb.data.di

import android.content.Context
import androidx.room.Room
import com.ljb.data.database.NumbersDao
import com.ljb.data.database.NumbersDatabase
import com.ljb.data.datasource.LocalDataSource
import com.ljb.data.datasource.LocalDataSourceImpl
import com.ljb.data.repository.NumberRepositoryImpl
import com.ljb.domain.repository.NumberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    /**
     * [NumberRepository] 인스턴스 생성 명시
     * */
    @Provides
    @Singleton
    fun provideNumberRepository(localDataSourceImpl: LocalDataSourceImpl): NumberRepository =
        NumberRepositoryImpl(localDataSourceImpl)
}

@Module
@InstallIn(SingletonComponent::class)
object DatasourceModule{
    /**
     * [LocalDataSource]의 인스턴스를 생성하는 방법을 명시.
     */
    @Provides
    @Singleton
    fun provideLocalDataSource(dao: NumbersDao): LocalDataSource = LocalDataSourceImpl(dao)
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    /**
     * [NumbersDatabase] 인스턴스를 생성하는 방법을 명시
     */
    @Provides
    @Singleton
    fun provideNumbersDatabase(@ApplicationContext appContext: Context): NumbersDatabase =
        Room.databaseBuilder(appContext, NumbersDatabase::class.java, "number_db").build()


    /**
     * [NumbersDao] 인스턴스를 생성하는 방법을 명시
     */
    @Provides
    @Singleton
    fun provideNumbersDao(database: NumbersDatabase) : NumbersDao = database.dao()
}