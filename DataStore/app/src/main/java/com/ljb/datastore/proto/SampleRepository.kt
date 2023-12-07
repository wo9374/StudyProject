package com.ljb.datastore.proto

import androidx.datastore.core.DataStore
import com.ljb.datastore.Sample
import kotlinx.coroutines.flow.Flow

class SampleRepository(private val sampleProtoDataStore: DataStore<Sample>) {
    // 읽기
    val flow : Flow<Sample> = sampleProtoDataStore.data

    // 쓰기 및 수정
    suspend fun setUserData(name:String, age:Int, gender: Boolean){
        sampleProtoDataStore.updateData { sample ->
            sample
                .toBuilder()
                .setName(name)
                .setAge(age)
                .setGender(gender)
                .setInitData(true)
                .build()
        }
    }

    suspend fun clearUserData(){
        sampleProtoDataStore.updateData { sample ->
            sample
                .toBuilder()
                .clearName()
                .clearAge()
                .clearGender()
                .clearInitData()
                .build()
        }
    }
}