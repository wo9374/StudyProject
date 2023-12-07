package com.ljb.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        val USER_AGE_KEY = intPreferencesKey("USER_AGE")
        val USER_NAME_KEY = stringPreferencesKey("USER_LAST_NAME")
        val USER_GENDER_KEY = booleanPreferencesKey("USER_GENDER")
    }

    //DataStore는 내부적으로 코루틴을 사용하기 때문에 User 데이터를 저장하는 storeUser()의 앞에 suspend
    suspend fun storeUser(age: Int, lastName: String, isMale: Boolean) {
        dataStore.edit {
            it[USER_AGE_KEY] = age
            it[USER_NAME_KEY] = lastName
            it[USER_GENDER_KEY] = isMale
        }
    }

    suspend fun clearUser(){
        dataStore.edit { it.clear() }
    }

    /**
     * 저장될 값들의 키를 정의하는 함수
     *
     * 타입에 맞는 함수를 쓰고 그 안에 키로 사용할 문자열을 넣어주면 된다.
     * 같은 이름의 키가 여러 개 있다면 ClassCastException이 발생
     * */
    val userAgeFlow: Flow<Int?> = dataStore.data.map { it[USER_AGE_KEY] }

    val userNameFlow: Flow<String?> = dataStore.data.map { it[USER_NAME_KEY] }

    val userGenderFlow: Flow<Boolean?> = dataStore.data.map { it[USER_GENDER_KEY] }
}