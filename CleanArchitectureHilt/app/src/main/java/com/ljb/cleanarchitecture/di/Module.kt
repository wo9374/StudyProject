package com.ljb.cleanarchitecture.di

import com.ljb.domain.repository.NumberRepository
import com.ljb.domain.usecase.ClearNumbersUseCase
import com.ljb.domain.usecase.GetNumbersUseCase
import com.ljb.domain.usecase.InsertNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetNumbersUseCase(repository: NumberRepository) = GetNumbersUseCase(repository)

    @Provides
    fun provideInsertNumberUseCase(repository: NumberRepository) = InsertNumberUseCase(repository)

    @Provides
    fun provideClearNumbersUseCase(repository: NumberRepository) = ClearNumbersUseCase(repository)
}