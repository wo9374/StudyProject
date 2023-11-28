package com.ljb.cleanarchitecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.domain.usecase.ClearNumbersUseCase
import com.ljb.domain.usecase.GetNumbersUseCase
import com.ljb.domain.usecase.InsertNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNumbersUseCase: GetNumbersUseCase,
    private val insertNumberUseCase: InsertNumberUseCase,
    private val clearNumbersUseCase: ClearNumbersUseCase
    ) : ViewModel(){

    val numbers = getNumbersUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertNumber(value: Int) = viewModelScope.launch {
        insertNumberUseCase(value)
    }

    fun clearNumbers() = viewModelScope.launch {
        clearNumbersUseCase()
    }
}