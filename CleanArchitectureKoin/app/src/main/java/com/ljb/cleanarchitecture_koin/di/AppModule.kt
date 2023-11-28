package com.ljb.cleanarchitecture_koin.di

import com.ljb.cleanarchitecture_koin.viewmodel.MainViewModel
import com.ljb.data.datasource.LocalDataSource
import com.ljb.data.repository.NumberRepositoryImpl
import com.ljb.domain.repository.NumberRepository
import com.ljb.domain.usecase.ClearNumbersUseCase
import com.ljb.domain.usecase.GetNumbersUseCase
import com.ljb.domain.usecase.InsertNumberUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    //viewModel - AAC 의 viewModel을 쓸 때, 생명주기를 AAC 의 viewModel 과 동일하게 사용 가능
}

val repositoryModule = module {
    single<NumberRepository> {
        NumberRepositoryImpl(get<LocalDataSource>())
    }
    //single - 단일 인스턴스. 싱글턴 처럼 동작
}

val usecaseModule = module {
    factory { GetNumbersUseCase(get()) }
    factory { InsertNumberUseCase(get()) }
    factory { ClearNumbersUseCase(get()) }
    //factory - 요청시 마다 새로운 인스턴스 반환
}