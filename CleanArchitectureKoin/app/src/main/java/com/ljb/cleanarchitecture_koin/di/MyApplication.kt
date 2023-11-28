package com.ljb.cleanarchitecture_koin.di

import android.app.Application
import com.ljb.data.datasource.LocalDataSource
import com.ljb.data.di.databaseModule
import com.ljb.data.repository.NumberRepositoryImpl
import com.ljb.domain.repository.NumberRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()     //Koin 로그를 Android Log 에 남김
            androidContext(this@MyApplication)  //context 의 인스턴스를 MyApplication 에 전달

            //실제로 사용 하고자 하는 객체
            modules(

                mutableListOf(
                    databaseModule,
                    repositoryModule,
                    usecaseModule,
                    viewModelModule
                )
                /**
                 * module 순서 중요
                 *
                 * 구동 순서
                 * [MainActivity]에서 [MainViewModel]이 생성될 때 [viewModelModule] 주입
                 * [MainViewModel]의 constructor [usecaseModule] 주입
                 * [GetNumbersUseCase] 각 UseCase 들의 constructor [NumberRepository] 필요 [repositoryModule] 주입
                 * [NumberRepositoryImpl]에 constructor [LocalDataSource] 필요 [databaseModule] 주입
                 *
                 * 미리 구성되어야 하기 때문에 역순으로
                 * databaseModule, repositoryModule, usecaseModule, viewModelModule 순으로 주입
                 *
                 * */
            )

            //단일 모듈
            //modules()
        }
    }
}