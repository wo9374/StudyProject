package com.ljb.datastore.proto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ljb.datastore.proto.SampleRepository

class ProtoViewModelFactory(private val repository: SampleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProtoViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProtoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}