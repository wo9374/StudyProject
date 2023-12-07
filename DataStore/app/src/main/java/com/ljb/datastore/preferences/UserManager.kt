package com.ljb.datastore.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * by keyword 사용 DataStore<Preferences> 구현
 * preferencesDataStore()에 맡기는 Context 확장 Property
 *
 * 하위에 타입 별로 DataStore 에 저장하거나 가져오는 함수를 구현해도 되지만 여기선 이것만 사용한다.
 * */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

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