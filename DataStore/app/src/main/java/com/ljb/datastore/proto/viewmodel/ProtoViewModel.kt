package com.ljb.datastore.proto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.datastore.Sample
import com.ljb.datastore.proto.SampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProtoViewModel(private val repository: SampleRepository) : ViewModel() {
    val flow: Flow<Sample> = repository.flow

    fun setUserData(name:String, age:Int, gender: Sample.Gender, initData: Boolean){
        viewModelScope.launch { repository.setUserData(name, age, gender, initData) }
    }

    fun clearUserData(){
        viewModelScope.launch { repository.clearUserData() }
    }
}