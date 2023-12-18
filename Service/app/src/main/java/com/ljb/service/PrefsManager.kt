package com.ljb.service

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "service_prefs")
class PrefsManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        val FOREGROUND_SERVICE_RUNNING = booleanPreferencesKey("FOREGROUND")
    }

    suspend fun setService(running: Boolean) {
        dataStore.edit {
            it[FOREGROUND_SERVICE_RUNNING] = running
        }
    }

    suspend fun clearService() {
        dataStore.edit { it.clear() }
    }

    val foregroundRunningFlow: Flow<Boolean?> =
        dataStore.data.map {
            it[FOREGROUND_SERVICE_RUNNING]
        }
}